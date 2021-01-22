package aero.minova.core.application.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

import aero.minova.core.application.system.controller.SqlViewController;
import aero.minova.core.application.system.domain.*;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${security_ldap_domain:minova.com}")
	private String domain;

	@Value("${security_ldap_address:ldap://mindcsrv.minova.com:3268/}")
	private String ldapServerAddress;
	
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
				.withUser("user").password(passwordEncoder().encode("password")).roles("dispatcher")
				.and()
				.withUser("admin").password(passwordEncoder().encode("rqgzxTf71EAx8chvchMi")).roles("admin");
//		if (ldapServerAddress != null && !ldapServerAddress.trim().isEmpty()) {
//			auth.authenticationProvider(new ActiveDirectoryLdapAuthenticationProvider(domain, ldapServerAddress))
//	            .ldapAuthentication().userDetailsContextMapper(userDetailsContextMapper());
//		}
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
    public UserDetailsContextMapper userDetailsContextMapper() {
        return new LdapUserDetailsMapper() {
            @Override
            public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities)  {
        		Table foo = new Table();
        		foo.setName("tUser");
        		List<Column>columns = new ArrayList<>(); 
        		columns.add(new Column("KeyText", DataType.STRING));
        		columns.add(new Column("SecurityToken", DataType.STRING));
        		columns.add(new Column("Memberships", DataType.STRING));
        		foo.setColumns(columns);
        		Row bar = new Row();
        		bar.setValues(Arrays.asList(new aero.minova.core.application.system.domain.Value(username),new aero.minova.core.application.system.domain.Value(""),new aero.minova.core.application.system.domain.Value("")));
        		foo.addRow(bar);
        		
        	    //dabei sollte nur eine ROW rauskommen, da jeder User eindeutig sein müsste
        		String result = svc.getSecurityView(foo).getRows().get(0).getValues().get(2).getStringValue();
        		
        		//alle SecurityTokens werden in der Datenbank mit Leerzeile und Raute voneinander getrennt
        		List<String> userSecurityTokens = new ArrayList<>();
        		userSecurityTokens = Stream.of(result.split("#"))//
        	      .map (elem -> new String(elem).trim())//
        	      .collect(Collectors.toList());
        		
        		//füge die authorities hinzu, welche aus dem Active Directory kommen
        		for (GrantedAuthority ga : authorities) {
					userSecurityTokens.add(ga.getAuthority().substring(5));
				}
        				
        		//die Berechtigungen der Gruppen noch herausfinden
        		Table groups = new Table();
        		groups.setName("tUserGroup");
        		List<Column>groupcolumns = new ArrayList<>(); 
        		groupcolumns.add(new Column("KeyText", DataType.STRING));
        		groupcolumns.add(new Column("SecurityToken", DataType.STRING));
        		groups.setColumns(groupcolumns);
        		for (String s : userSecurityTokens) {
        			Row tokens = new Row();
        			tokens.setValues(Arrays.asList(new aero.minova.core.application.system.domain.Value(s),new aero.minova.core.application.system.domain.Value("NOT NULL")));
        			groups.addRow(tokens);
        		}
        		List<Row> groupTokens = svc.getSecurityView(groups).getRows();
        		for (Row r : groupTokens) {
        			userSecurityTokens.addAll(Arrays.asList(r.getValues().get(1).getStringValue().trim().split("#")));
        		}
        		
        	    Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        		for (String string : userSecurityTokens) {
        			if(!string.equals("")) {
        				SimpleGrantedAuthority sga = new SimpleGrantedAuthority(string);
        				if(!grantedAuthorities.contains(sga))
        					grantedAuthorities.add(sga);
        			}
        		}
        		
                return super.mapUserFromContext(ctx, username, grantedAuthorities);
            }
        };
    }
	
	
}