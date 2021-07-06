package maja.zmaja.assetsservice.security;

import maja.zmaja.assetsservice.enums.UserRole;
import maja.zmaja.assetsservice.security.jwt.AuthEntryPointJwt;
import maja.zmaja.assetsservice.security.jwt.AuthTokenFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import  maja.zmaja.assetsservice.security.service.*;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity( securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsServiceSecurity userDetailsServiceSecurity;
    
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsServiceSecurity).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public 
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    
//        http = http.cors().and().csrf().disable();
//        
//        http = http
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and();
//        http = http
//                .exceptionHandling()
//                .authenticationEntryPoint(
//                        (request, response, ex) -> {
//                            response.sendError(
//                                    HttpServletResponse.SC_UNAUTHORIZED,
//                                    ex.getMessage()
//                            );
//                        }
//                )
//                .and();
//        
//        http.authorizeRequests()
//                .antMatchers("/api/**").permitAll()
//                .anyRequest().authenticated();
//        
//        http.addFilterBefore(
//                authenticationJwtTokenFilter(),
//                UsernamePasswordAuthenticationFilter.class
//        );
//
//        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/api/**").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
    
    }




