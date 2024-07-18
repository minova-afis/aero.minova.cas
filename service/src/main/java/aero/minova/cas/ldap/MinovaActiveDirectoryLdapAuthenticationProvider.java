package aero.minova.cas.ldap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.InitialLdapContext;

import org.springframework.core.log.LogMessage;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.ldap.CommunicationException;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.support.DefaultDirObjectFactory;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.SpringSecurityLdapTemplate;
import org.springframework.security.ldap.authentication.AbstractLdapAuthenticationProvider;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Kopie von {@link org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider}, um auch alternative UPN Suffixe zu
 * unterstützen. Nur die Methode {@link #bindAsUser(String, String)} wurde angepasst.
 */
public final class MinovaActiveDirectoryLdapAuthenticationProvider extends AbstractLdapAuthenticationProvider {

	private static final Pattern SUB_ERROR_CODE = Pattern.compile(".*data\\s([0-9a-f]{3,4}).*");

	// Error codes
	private static final int USERNAME_NOT_FOUND = 0x525;

	private static final int INVALID_PASSWORD = 0x52e;

	private static final int NOT_PERMITTED = 0x530;

	private static final int PASSWORD_EXPIRED = 0x532;

	private static final int ACCOUNT_DISABLED = 0x533;

	private static final int ACCOUNT_EXPIRED = 0x701;

	private static final int PASSWORD_NEEDS_RESET = 0x773;

	private static final int ACCOUNT_LOCKED = 0x775;

	private final String domain;

	private final String rootDn;

	private final String url;

	private boolean convertSubErrorCodesToExceptions;

	private String searchFilter = "(&(objectClass=user)(userPrincipalName={0}))";

	private Map<String, Object> contextEnvironmentProperties = new HashMap<>();

	// Only used to allow tests to substitute a mock LdapContext
	ContextFactory contextFactory = new ContextFactory();

	/**
	 * @param domain
	 *            the domain name (may be null or empty)
	 * @param url
	 *            an LDAP url (or multiple URLs)
	 * @param rootDn
	 *            the root DN (may be null or empty)
	 */
	public MinovaActiveDirectoryLdapAuthenticationProvider(String domain, String url, String rootDn) {
		Assert.isTrue(StringUtils.hasText(url), "Url cannot be empty");
		this.domain = StringUtils.hasText(domain) ? domain.toLowerCase() : null;
		this.url = url;
		this.rootDn = StringUtils.hasText(rootDn) ? rootDn.toLowerCase() : null;
	}

	/**
	 * @param domain
	 *            the domain name (may be null or empty)
	 * @param url
	 *            an LDAP url (or multiple URLs)
	 */
	public MinovaActiveDirectoryLdapAuthenticationProvider(String domain, String url) {
		Assert.isTrue(StringUtils.hasText(url), "Url cannot be empty");
		this.domain = StringUtils.hasText(domain) ? domain.toLowerCase() : null;
		this.url = url;
		this.rootDn = (this.domain != null) ? rootDnFromDomain(this.domain) : null;
	}

	@Override
	protected DirContextOperations doAuthentication(UsernamePasswordAuthenticationToken auth) {
		String username = auth.getName();
		String password = (String) auth.getCredentials();
		DirContext ctx = null;
		try {
			ctx = bindAsUser(username, password);
			return searchForUser(ctx, username);
		} catch (CommunicationException ex) {
			throw badLdapConnection(ex);
		} catch (NamingException ex) {
			this.logger.error("Failed to locate directory entry for authenticated user: " + username, ex);
			throw badCredentials(ex);
		} finally {
			LdapUtils.closeContext(ctx);
		}
	}

	/**
	 * Creates the user authority list from the values of the {@code memberOf} attribute obtained from the user's Active Directory entry.
	 */
	@Override
	protected Collection<? extends GrantedAuthority> loadUserAuthorities(DirContextOperations userData, String username, String password) {
		String[] groups = userData.getStringAttributes("memberOf");
		if (groups == null) {
			this.logger.debug("No values for 'memberOf' attribute.");
			return AuthorityUtils.NO_AUTHORITIES;
		}
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("'memberOf' attribute values: " + Arrays.asList(groups));
		}
		List<GrantedAuthority> authorities = new ArrayList<>(groups.length);
		for (String group : groups) {
			authorities.add(new SimpleGrantedAuthority(new DistinguishedName(group).removeLast().getValue()));
		}
		return authorities;
	}

	private DirContext bindAsUser(String username, String password) {
		// TODO. add DNS lookup based on domain
		final String bindUrl = this.url;
		Hashtable<String, Object> env = new Hashtable<>();
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		String bindPrincipal = createBindPrincipal(username);
		env.put(Context.SECURITY_PRINCIPAL, bindPrincipal);
		env.put(Context.PROVIDER_URL, bindUrl);
		env.put(Context.SECURITY_CREDENTIALS, password);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.OBJECT_FACTORIES, DefaultDirObjectFactory.class.getName());
		env.putAll(this.contextEnvironmentProperties);
		try {
			return this.contextFactory.createContext(env);
		} catch (NamingException ex) {
			if ((ex instanceof AuthenticationException) || (ex instanceof OperationNotSupportedException)) {
				handleBindException(bindPrincipal, ex);
				throw badCredentials(ex);
			}
			throw LdapUtils.convertLdapException(ex);
		}
	}

	private void handleBindException(String bindPrincipal, NamingException exception) {
		this.logger.debug(LogMessage.format("Authentication for %s failed:%s", bindPrincipal, exception));
		handleResolveObj(exception);
		int subErrorCode = parseSubErrorCode(exception.getMessage());
		if (subErrorCode <= 0) {
			this.logger.debug("Failed to locate AD-specific sub-error code in message");
			return;
		}
		this.logger.info(LogMessage.of(() -> "Active Directory authentication failed: " + subCodeToLogMessage(subErrorCode)));
		if (this.convertSubErrorCodesToExceptions) {
			raiseExceptionForErrorCode(subErrorCode, exception);
		}
	}

	private void handleResolveObj(NamingException exception) {
		Object resolvedObj = exception.getResolvedObj();
		boolean serializable = resolvedObj instanceof Serializable;
		if (resolvedObj != null && !serializable) {
			exception.setResolvedObj(null);
		}
	}

	private int parseSubErrorCode(String message) {
		Matcher matcher = SUB_ERROR_CODE.matcher(message);
		if (matcher.matches()) {
			return Integer.parseInt(matcher.group(1), 16);
		}
		return -1;
	}

	private void raiseExceptionForErrorCode(int code, NamingException exception) {
		String hexString = Integer.toHexString(code);
		Throwable cause = new ActiveDirectoryAuthenticationException(hexString, exception.getMessage(), exception);
		switch (code) {
		case PASSWORD_EXPIRED:
			throw new CredentialsExpiredException(this.messages.getMessage("LdapAuthenticationProvider.credentialsExpired", "User credentials have expired"),
					cause);
		case ACCOUNT_DISABLED:
			throw new DisabledException(this.messages.getMessage("LdapAuthenticationProvider.disabled", "User is disabled"), cause);
		case ACCOUNT_EXPIRED:
			throw new AccountExpiredException(this.messages.getMessage("LdapAuthenticationProvider.expired", "User account has expired"), cause);
		case ACCOUNT_LOCKED:
			throw new LockedException(this.messages.getMessage("LdapAuthenticationProvider.locked", "User account is locked"), cause);
		default:
			throw badCredentials(cause);
		}
	}

	private String subCodeToLogMessage(int code) {
		switch (code) {
		case USERNAME_NOT_FOUND:
			return "User was not found in directory";
		case INVALID_PASSWORD:
			return "Supplied password was invalid";
		case NOT_PERMITTED:
			return "User not permitted to logon at this time";
		case PASSWORD_EXPIRED:
			return "Password has expired";
		case ACCOUNT_DISABLED:
			return "Account is disabled";
		case ACCOUNT_EXPIRED:
			return "Account expired";
		case PASSWORD_NEEDS_RESET:
			return "User must reset password";
		case ACCOUNT_LOCKED:
			return "Account locked";
		}
		return "Unknown (error code " + Integer.toHexString(code) + ")";
	}

	private BadCredentialsException badCredentials() {
		return new BadCredentialsException(this.messages.getMessage("LdapAuthenticationProvider.badCredentials", "Bad credentials"));
	}

	private BadCredentialsException badCredentials(Throwable cause) {
		return (BadCredentialsException) badCredentials().initCause(cause);
	}

	private InternalAuthenticationServiceException badLdapConnection(Throwable cause) {
		return new InternalAuthenticationServiceException(
				this.messages.getMessage("LdapAuthenticationProvider.badLdapConnection", "Connection to LDAP server failed."), cause);
	}

	private DirContextOperations searchForUser(DirContext context, String username) throws NamingException {
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String bindPrincipal = createBindPrincipal(username);
		String searchRoot = (this.rootDn != null) ? this.rootDn : searchRootFromPrincipal(bindPrincipal);

		try {
			return SpringSecurityLdapTemplate.searchForSingleEntryInternal(context, searchControls, searchRoot, this.searchFilter,
					new Object[] { bindPrincipal, username });
		} catch (CommunicationException ex) {
			throw badLdapConnection(ex);
		} catch (IncorrectResultSizeDataAccessException ex) {
			// Search should never return multiple results if properly configured -
			if (ex.getActualSize() != 0) {
				throw ex;
			}
			// If we found no results, then the username/password did not match
			UsernameNotFoundException userNameNotFoundException = new UsernameNotFoundException("User " + username + " not found in directory.", ex);
			throw badCredentials(userNameNotFoundException);
		}
	}

	private String searchRootFromPrincipal(String bindPrincipal) {
		int atChar = bindPrincipal.lastIndexOf('@');
		if (atChar < 0) {
			this.logger.debug("User principal '" + bindPrincipal + "' does not contain the domain, and no domain has been configured");
			throw badCredentials();
		}
		return rootDnFromDomain(bindPrincipal.substring(atChar + 1, bindPrincipal.length()));
	}

	private String rootDnFromDomain(String domain) {
		String[] tokens = StringUtils.tokenizeToStringArray(domain, ".");
		StringBuilder root = new StringBuilder();
		for (String token : tokens) {
			if (root.length() > 0) {
				root.append(',');
			}
			root.append("dc=").append(token);
		}
		return root.toString();
	}

	/**
	 * Hier unterstützen wir auch alternative UPN Suffixe
	 * 
	 * @param username
	 * @return
	 */
	String createBindPrincipal(String username) {
		if (this.domain == null || username.toLowerCase().endsWith(this.domain) || username.contains("@")) {
			return username;
		}
		return username + "@" + this.domain;
	}

	/**
	 * By default, a failed authentication (LDAP error 49) will result in a {@code BadCredentialsException}.
	 * <p>
	 * If this property is set to {@code true}, the exception message from a failed bind attempt will be parsed for the AD-specific error code and a
	 * {@link CredentialsExpiredException}, {@link DisabledException}, {@link AccountExpiredException} or {@link LockedException} will be thrown for the
	 * corresponding codes. All other codes will result in the default {@code BadCredentialsException}.
	 * 
	 * @param convertSubErrorCodesToExceptions
	 *            {@code true} to raise an exception based on the AD error code.
	 */
	public void setConvertSubErrorCodesToExceptions(boolean convertSubErrorCodesToExceptions) {
		this.convertSubErrorCodesToExceptions = convertSubErrorCodesToExceptions;
	}

	/**
	 * The LDAP filter string to search for the user being authenticated. Occurrences of {0} are replaced with the {@code username@domain}. Occurrences of {1}
	 * are replaced with the {@code username} only.
	 * <p>
	 * Defaults to: {@code (&(objectClass=user)(userPrincipalName={0}))}
	 * </p>
	 * 
	 * @param searchFilter
	 *            the filter string
	 * @since 3.2.6
	 */
	public void setSearchFilter(String searchFilter) {
		Assert.hasText(searchFilter, "searchFilter must have text");
		this.searchFilter = searchFilter;
	}

	/**
	 * Allows a custom environment properties to be used to create initial LDAP context.
	 * 
	 * @param environment
	 *            the additional environment parameters to use when creating the LDAP Context
	 */
	public void setContextEnvironmentProperties(Map<String, Object> environment) {
		Assert.notEmpty(environment, "environment must not be empty");
		this.contextEnvironmentProperties = new Hashtable<>(environment);
	}

	static class ContextFactory {

		DirContext createContext(Hashtable<?, ?> env) throws NamingException {
			return new InitialLdapContext(env, null);
		}

	}

	@SuppressWarnings("serial")
	public final class ActiveDirectoryAuthenticationException extends org.springframework.security.core.AuthenticationException {

		private final String dataCode;

		ActiveDirectoryAuthenticationException(String dataCode, String message, Throwable cause) {
			super(message, cause);
			this.dataCode = dataCode;
		}

		public String getDataCode() {
			return this.dataCode;
		}

	}

}
