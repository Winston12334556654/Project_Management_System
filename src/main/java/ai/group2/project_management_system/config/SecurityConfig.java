package ai.group2.project_management_system.config;

import ai.group2.project_management_system.model.entity.User;
import ai.group2.project_management_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final UserRepository userRepository;
    private final String MY_KEY = "akatsuki-abcdEFGH1234-5678IJKLmnopqrstuvwxYZ";
    private final DataSource dataSource;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers(
                                        "/resources/**",
                                        "/static/**",
                                        "/assets/**",
                                        "/js/**",
                                        "/css/**",
                                        "/images/**",
                                        "userPhoto",
                                        "projectFiles"
                                ).permitAll()
                                .anyRequest()
                                .authenticated()

                )
                .formLogin(form -> {
                    form
                            .loginPage("/login")
                            .loginProcessingUrl("/process-login")
                            .usernameParameter("email")
                            .passwordParameter("password")
                            .successHandler(((request, response, authentication) -> {
                                response.sendRedirect("/home");
                            }))
                            .permitAll();
                })
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .clearAuthentication(true)
                                .logoutSuccessHandler(((request, response, authentication) ->
                                        response.sendRedirect("/login")
                                ))
                                .permitAll()
                )
                .exceptionHandling(
                        (exceptionHandling) -> exceptionHandling
                                .accessDeniedPage("/access-denied"))
                .rememberMe(remember -> remember
                        .key(MY_KEY )
                        .rememberMeParameter("remember-me")
                        .rememberMeServices(rememberMeServices())
                        .tokenValiditySeconds(60 * 60 * 24)//1 day
                )
                .sessionManagement(session -> session
                        .maximumSessions(1)//1 user 1 session
                        .maxSessionsPreventsLogin(false)//if user already login, then user can't login again
                        .expiredUrl("/login")//if user already login, then user can't login again
                        .sessionRegistry(sessionRegistry())//session registry
                )
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return email -> {
            User user=userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("user not found!"));
            log.info("\n\n\nuser -> {}\n\n\n", user);
            return user;
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        if (tokenRepository.getJdbcTemplate() == null) {
            tokenRepository.setJdbcTemplate(new JdbcTemplate(dataSource));
        }
        tokenRepository.getJdbcTemplate().execute("CREATE TABLE IF NOT EXISTS persistent_logins (" +
                "username VARCHAR(64) NOT NULL," +
                "series VARCHAR(64) PRIMARY KEY," +
                "token VARCHAR(64) NOT NULL," +
                "last_used TIMESTAMP NOT NULL" +
                ")");

        return tokenRepository;
    }

    @Bean
    public PersistentTokenBasedRememberMeServices rememberMeServices() {
        return new PersistentTokenBasedRememberMeServices(
                MY_KEY, userDetailsService(), persistentTokenRepository()
        );

//        @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
//                @Override
//        public AuthenticationManager authenticationManagerBean() throws Exception{
//            return super.authenticationManagerBean();
//        }
    }
}
