package aero.minova.core.application.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

import aero.minova.core.application.system.controller.SqlViewController;
import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.Table;
import lombok.val;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//	@Value("${security_ldap_domain:minova.com}")
//	private String domain;
//
//	@Value("${security_ldap_address:ldap://mindcsrv.minova.com:3268/}")
//	private String ldapServerAddress;

	@Autowired
	SqlViewController svc;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/img/**", "/js/**", "/theme/**", "/index", "/login", "/layout").permitAll();
		http.authorizeRequests().anyRequest().fullyAuthenticated();
		http.logout().logoutUrl("/logout").logoutSuccessUrl("/");
		http.formLogin()//
				.loginPage("/login")//
				.defaultSuccessUrl("/")//
				.permitAll();
		http.httpBasic();
		http.csrf().disable(); // TODO Entferne dies. Vereinfacht zur Zeit die Loginseite.
		http.logout().permitAll();
//		http.requiresChannel().anyRequest().requiresSecure();
	}

	@Bean
	public SpringSecurityDialect springSecurityDialect() {
		return new SpringSecurityDialect();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()//
				.withUser("user").password(passwordEncoder().encode("password")).roles("dispatcher")//
				.and()//
				.withUser("admin").password(passwordEncoder().encode("rqgzxTf71EAx8chvchMi")).roles("admin", "dispo");
//		if (ldapServerAddress != null && !ldapServerAddress.trim().isEmpty()) {
//			auth.authenticationProvider(new ActiveDirectoryLdapAuthenticationProvider(domain, ldapServerAddress))
//	            .ldapAuthentication().userDetailsContextMapper(userDetailsContextMapper());
//		}
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean("ldapUser")
	public UserDetailsContextMapper userDetailsContextMapper() throws RuntimeException {
		return new LdapUserDetailsMapper() {
			@Override
			public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities)
					throws RuntimeException {
				Table tUser = new Table();
				tUser.setName("xtcasUser");
				List<Column> columns = new ArrayList<>();
				columns.add(new Column("KeyText", DataType.STRING));
				columns.add(new Column("SecurityToken", DataType.STRING));
				columns.add(new Column("Memberships", DataType.STRING));
				tUser.setColumns(columns);
				Row userEntry = new Row();
				userEntry.setValues(Arrays.asList(new aero.minova.core.application.system.domain.Value(username),
						new aero.minova.core.application.system.domain.Value(""), new aero.minova.core.application.system.domain.Value("")));
				tUser.addRow(userEntry);

				// dabei sollte nur eine ROW rauskommen, da jeder User eindeutig sein müsste
				Table membershipsFromUser = svc.getTableForSecurityCheck(tUser);
				List<String> userSecurityTokens = new ArrayList<>();

				if (membershipsFromUser.getRows().size() > 0) {
					String result = membershipsFromUser.getRows().get(0).getValues().get(2).getStringValue();

					// alle SecurityTokens werden in der Datenbank mit Leerzeile und Raute voneinander getrennt
					userSecurityTokens = Stream.of(result.split("#"))//
							.map(String::trim)//
							.collect(Collectors.toList());

					// überprüfen, ob der einzigartige userSecurityToken bereits in der Liste der Memberships vorhanden war, wenn nicht, dann hinzufügen
					String uniqueUserToken = membershipsFromUser.getRows().get(0).getValues().get(1).getStringValue().trim();
					if (!userSecurityTokens.contains(uniqueUserToken))
						userSecurityTokens.add(uniqueUserToken);
				} else {
					// falls der User nicht in der Datenbank gefunden wurde, wird sein Benutzername als einzigartiger userSecurityToken verwendet
					userSecurityTokens.add(username);
				}

				// füge die authorities hinzu, welche aus dem Active Directory kommen
				for (GrantedAuthority ga : authorities) {
					userSecurityTokens.add(ga.getAuthority().substring(5));
				}

				// die Berechtigungen der Gruppen noch herausfinden
				Table groups = new Table();
				groups.setName("xtcasUserGroup");
				List<Column> groupcolumns = new ArrayList<>();
				groupcolumns.add(new Column("KeyText", DataType.STRING));
				groupcolumns.add(new Column("SecurityToken", DataType.STRING));
				groups.setColumns(groupcolumns);
				for (String s : userSecurityTokens) {
					if (!s.trim().equals("")) {
						Row tokens = new Row();
						tokens.setValues(Arrays.asList(new aero.minova.core.application.system.domain.Value(s.trim()),
								new aero.minova.core.application.system.domain.Value("NOT NULL")));
						groups.addRow(tokens);
					}
				}
				if (groups.getRows().size() > 0) {
					List<Row> groupTokens = svc.getTableForSecurityCheck(groups).getRows();
					List<String> groupSecurityTokens = new ArrayList<>();
					for (Row r : groupTokens) {
						String memberships = r.getValues().get(1).getStringValue();
						// alle SecurityToken einer Gruppe der Liste hinzufügen
						val membershipsAsList = Stream.of(memberships.split("#"))//
								.map(String::trim)//
								.collect(Collectors.toList());
						groupSecurityTokens.addAll(membershipsAsList);
					}

					// verschiedene Rollen/Gruppen können dieselbe Berechtigung haben, deshalb rausfiltern
					for (String string : groupSecurityTokens) {
						if (!userSecurityTokens.contains(string))
							userSecurityTokens.add(string);
					}
				}

				Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
				for (String string : userSecurityTokens) {
					if (!string.equals(""))
						grantedAuthorities.add(new SimpleGrantedAuthority(string));
				}

				return super.mapUserFromContext(ctx, username, grantedAuthorities);
			}
		};
	}

}