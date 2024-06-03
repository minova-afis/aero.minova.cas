package aero.minova.cas;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

public class MultipleLdapDomainsAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	CustomLogger customLogger;

	List<ActiveDirectoryLdapAuthenticationProvider> providers = new ArrayList<>();

	public MultipleLdapDomainsAuthenticationProvider(List<String> domains, List<String> ldapServerAddresses,
			UserDetailsContextMapper userDetailsContextMapper) {

		if (ldapServerAddresses.size() > 1 && ldapServerAddresses.size() != domains.size()) {
			throw new RuntimeException(
					"Number of LDAP domains and addresses don't match. Either configure one address to use with all domains, or have the same number of domains and addresses.");
		}

		for (int i = 0; i < domains.size(); i++) {

			// Wenn nur genau eine Adresse gegeben ist diese für alle Domänen nutzen
			// Ansonsten erste Adresse mit erster Domäne, zweite mit zweiter, ...
			String address = ldapServerAddresses.size() == 1 ? ldapServerAddresses.get(0) : ldapServerAddresses.get(i);

			ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider(domains.get(i), address);
			provider.setConvertSubErrorCodesToExceptions(true);
			provider.setUserDetailsContextMapper(userDetailsContextMapper);

			providers.add(provider);
		}
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		List<Exception> exceptions = new ArrayList<>();
		for (ActiveDirectoryLdapAuthenticationProvider provider : providers) {
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
		for (ActiveDirectoryLdapAuthenticationProvider provider : providers) {
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
