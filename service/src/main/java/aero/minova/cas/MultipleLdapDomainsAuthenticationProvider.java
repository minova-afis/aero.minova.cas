package aero.minova.cas;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

public class MultipleLdapDomainsAuthenticationProvider implements AuthenticationProvider {

	List<ActiveDirectoryLdapAuthenticationProvider> providers = new ArrayList<>();

	public MultipleLdapDomainsAuthenticationProvider(List<String> domains, List<String> ldapServerAddresses,
			UserDetailsContextMapper userDetailsContextMapper) {

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

		for (ActiveDirectoryLdapAuthenticationProvider provider : providers) {

			try {
				Authentication authenticate = provider.authenticate(authentication);
				if (authenticate != null) {
					return authenticate;
				}
			} catch (Exception e) {
				// Nächsten probieren
			}
		}

		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		for (ActiveDirectoryLdapAuthenticationProvider provider : providers) {
			try {
				if (provider.supports(authentication)) {
					return true;
				}
			} catch (Exception e) {
				// Nächsten probieren
			}
		}
		return false;
	}

}
