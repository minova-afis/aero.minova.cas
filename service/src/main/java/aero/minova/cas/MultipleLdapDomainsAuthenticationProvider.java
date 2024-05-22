package aero.minova.cas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

public class MultipleLdapDomainsAuthenticationProvider implements AuthenticationProvider {

	List<ActiveDirectoryLdapAuthenticationProvider> providers = new ArrayList<>();

	public MultipleLdapDomainsAuthenticationProvider(String domains, String ldapServerAddresses, UserDetailsContextMapper userDetailsContextMapper) {

		List<String> domainList = Arrays.asList(domains.split(SecurityConfig.MULTIPLE_LDAP_CONFIGURATIONS_SEPERATOR));
		List<String> addressList = Arrays.asList(ldapServerAddresses.split(SecurityConfig.MULTIPLE_LDAP_CONFIGURATIONS_SEPERATOR));

		for (int i = 0; i < domainList.size(); i++) {

			String address = addressList.size() == 1 ? addressList.get(0) : addressList.get(i);

			ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider(domainList.get(i), address);
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
