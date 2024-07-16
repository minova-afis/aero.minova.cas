package aero.minova.cas.ldap;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.userdetails.LdapUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import aero.minova.cas.CustomLogger;

public class MultipleLdapServerAddressesUserDetailsManager implements UserDetailsManager {

	@Autowired
	CustomLogger customLogger;

	List<LdapUserDetailsManager> managers = new ArrayList<>();

	public MultipleLdapServerAddressesUserDetailsManager(List<String> ldapServerAddresses) {
		for (String address : ldapServerAddresses) {
			DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(address);
			contextSource.afterPropertiesSet();
			managers.add(new LdapUserDetailsManager(contextSource));
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<Exception> exceptions = new ArrayList<>();
		for (LdapUserDetailsManager manager : managers) {

			try {
				UserDetails userByUsername = manager.loadUserByUsername(username);
				if (userByUsername != null) {
					return userByUsername;
				}
			} catch (Exception e) {
				exceptions.add(e);
			}
		}

		customLogger.logError("LoadUserByUsername failed for all LdapUserDetailsManagers");
		for (Exception e : exceptions) {
			customLogger.logError(e);
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
		List<Exception> exceptions = new ArrayList<>();
		for (LdapUserDetailsManager manager : managers) {
			try {
				if (manager.userExists(username)) {
					return true;
				}
			} catch (Exception e) {
				exceptions.add(e);
			}
		}

		customLogger.logError("Checking if user with name '" + username + "' exists failed for all LdapUserDetailsManagers");
		for (Exception e : exceptions) {
			customLogger.logError(e);
		}

		return false;
	}

}
