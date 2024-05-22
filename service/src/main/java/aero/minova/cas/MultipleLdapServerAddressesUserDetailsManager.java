package aero.minova.cas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.userdetails.LdapUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

public class MultipleLdapServerAddressesUserDetailsManager implements UserDetailsManager {

	List<LdapUserDetailsManager> managers = new ArrayList<>();

	public MultipleLdapServerAddressesUserDetailsManager(String ldapServerAddresses) {
		List<String> addressList = Arrays.asList(ldapServerAddresses.split(SecurityConfig.MULTIPLE_LDAP_CONFIGURATIONS_SEPERATOR));

		for (String address : addressList) {
			DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(address);
			contextSource.afterPropertiesSet();
			managers.add(new LdapUserDetailsManager(contextSource));
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		for (LdapUserDetailsManager manager : managers) {

			try {
				UserDetails userByUsername = manager.loadUserByUsername(username);
				if (userByUsername != null) {
					return userByUsername;
				}
			} catch (Exception e) {
				// Nächsten probieren
			}
		}

		return null;
	}

	@Override
	public void createUser(UserDetails user) {
		if (managers.size() == 1) {
			managers.get(0).createUser(user);
		} else {
			throw new UnsupportedOperationException("Can't create User when more than one ldap server address is configured");
		}
	}

	@Override
	public void updateUser(UserDetails user) {
		if (managers.size() == 1) {
			managers.get(0).updateUser(user);
		} else {
			throw new UnsupportedOperationException("Can't update User when more than one ldap server address is configured");
		}
	}

	@Override
	public void deleteUser(String username) {
		if (managers.size() == 1) {
			managers.get(0).deleteUser(username);
		} else {
			throw new UnsupportedOperationException("Can't delete User when more than one ldap server address is configured");
		}
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		if (managers.size() == 1) {
			managers.get(0).changePassword(oldPassword, newPassword);
		} else {
			throw new UnsupportedOperationException("Can't change password when more than one ldap server address is configured");
		}
	}

	@Override
	public boolean userExists(String username) {
		for (LdapUserDetailsManager manager : managers) {
			try {
				if (manager.userExists(username)) {
					return true;
				}
			} catch (Exception e) {
				// Nächsten probieren
			}
		}

		return false;
	}

}
