package aero.minova.cas.ldap;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

import aero.minova.cas.CustomLogger;

public class MultipleLdapDomainsAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	CustomLogger customLogger;

	List<MinovaActiveDirectoryLdapAuthenticationProvider> providers = new ArrayList<>();

	public MultipleLdapDomainsAuthenticationProvider(List<String> domains, List<String> ldapServerAddresses,
			UserDetailsContextMapper userDetailsContextMapper) {

		if (domains.size() == ldapServerAddresses.size()) {
			for (int i = 0; i < domains.size(); i++) {
				addProvider(domains.get(i), ldapServerAddresses.get(i), userDetailsContextMapper);
			}
		} else if (ldapServerAddresses.size() == 1) {
			for (int i = 0; i < domains.size(); i++) {
				addProvider(domains.get(i), ldapServerAddresses.get(0), userDetailsContextMapper);
			}
		} else if (domains.size() == 1) {
			for (int i = 0; i < ldapServerAddresses.size(); i++) {
				addProvider(domains.get(0), ldapServerAddresses.get(i), userDetailsContextMapper);
			}
		} else {
			throw new RuntimeException(
					"Number of LDAP domains and addresses don't match. Either configure one address to use with all domains, one domain to use with all addresses, or have the same number of domains and addresses.");
		}
	}

	private void addProvider(String domain, String url, UserDetailsContextMapper userDetailsContextMapper) {
		MinovaActiveDirectoryLdapAuthenticationProvider provider = new MinovaActiveDirectoryLdapAuthenticationProvider(domain, url);
		provider.setConvertSubErrorCodesToExceptions(true);
		provider.setUserDetailsContextMapper(userDetailsContextMapper);
		providers.add(provider);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		List<Exception> exceptions = new ArrayList<>();
		for (MinovaActiveDirectoryLdapAuthenticationProvider provider : providers) {
			try {
				Authentication authenticate = provider.authenticate(authentication);
				if (authenticate != null) {
					return authenticate;
				}
			} catch (Exception e) {
				exceptions.add(e);
			}
		}

		customLogger.logError("Authentication failed for all ActiveDirectoryLdapAuthenticationProviders");
		for (Exception e : exceptions) {
			customLogger.logError(e);
		}
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		List<Exception> exceptions = new ArrayList<>();
		for (MinovaActiveDirectoryLdapAuthenticationProvider provider : providers) {
			try {
				if (provider.supports(authentication)) {
					return true;
				}
			} catch (Exception e) {
				exceptions.add(e);
			}
		}

		customLogger.logError("Checking if authentication is supported failed for all ActiveDirectoryLdapAuthenticationProviders");
		for (Exception e : exceptions) {
			customLogger.logError(e);
		}
		return false;
	}

}
