package aero.minova.core.application.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
				.withUser("user").password(passwordEncoder().encode("password")).roles("USER")
				.and()
				.withUser("admin").password(passwordEncoder().encode("rqgzxTf71EAx8chvchMi")).roles("ADMIN")
				.and()
				.withUser("test").password(passwordEncoder().encode("Minova+0")).roles("Dispatcher","ADMIN");
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
        		foo.setName("vCASUserRoles");
        		List<Column>columns = new ArrayList<>(); 
        		columns.add(new Column("KeyText", DataType.STRING));
        		columns.add(new Column("UserSecurityToken", DataType.STRING));
        		columns.add(new Column("KeyLong", DataType.INTEGER));
        		foo.setColumns(columns);
        		Row bar = new Row();
        		bar.setValues(Arrays.asList(new aero.minova.core.application.system.domain.Value(username),new aero.minova.core.application.system.domain.Value(""),new aero.minova.core.application.system.domain.Value("")));
        		foo.addRow(bar);
        		
        	    Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        		
        		List<Row> result = svc.getIndexViewUnsecure(foo).getRows();
        		
        		//fügt alle gefundenen UserGroups als deren KeyLong hinzu
        		for (Row row : result) {
        			if(!grantedAuthorities.contains(new SimpleGrantedAuthority(row.getValues().get(2).getStringValue()))){
        				grantedAuthorities.add(new SimpleGrantedAuthority(row.getValues().get(2).getStringValue()));
        		    }
        		}
        		//fügt SecurityToken hinzu
        		grantedAuthorities.add(new SimpleGrantedAuthority(result.get(0).getValues().get(1).getStringValue()));
        		
                return super.mapUserFromContext(ctx, username, grantedAuthorities);
            }
        };
    }
	
	
}