package com.ritense.operaton.cockpit

import org.operaton.bpm.webapp.impl.security.auth.ContainerBasedAuthenticationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher
import org.springframework.web.context.request.RequestContextListener
import org.springframework.web.filter.ForwardedHeaderFilter

/**
 * Operaton Web application SSO configuration for usage with KeycloakIdentityProviderPlugin.
 */
@ConditionalOnMissingClass("org.springframework.test.context.junit.jupiter.SpringExtension")
@EnableWebSecurity
@Configuration
class WebAppSecurityConfig @Autowired constructor(
    private val keycloakLogoutHandler: KeycloakLogoutHandler
) {
    @Bean
    @Order(2)
    @Throws(Exception::class)
    fun httpSecurity(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { csrf: CsrfConfigurer<HttpSecurity> ->
                csrf
                    .ignoringRequestMatchers(
                        antMatcher("/api/**"),
                        antMatcher("/engine-rest/**")
                    )
                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            }
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers(
                        antMatcher("/api/admin/**"),
                        antMatcher("/api/cockpit/**"),
                        antMatcher("/api/engine/**"),
                        antMatcher("/api/tasklist/**"),
                        antMatcher("/api/welcome/**"),
                        antMatcher("/app/admin/**"),
                        antMatcher("/app/cockpit/**"),
                        antMatcher("/app/tasklist/**"),
                        antMatcher("/app/welcome/**"),
                        antMatcher("/assets/**"),
                        antMatcher("/favicon.ico"),
                        antMatcher("/lib/**")
                    )
                    .authenticated()
                    .anyRequest()
                    .permitAll()
            }
            .oauth2Login(Customizer.withDefaults<OAuth2LoginConfigurer<HttpSecurity>>())
            .logout { logout: LogoutConfigurer<HttpSecurity> ->
                logout
                    .logoutRequestMatcher(antMatcher("/app/**/logout"))
                    .logoutSuccessHandler(keycloakLogoutHandler)
            }
            .build()
    }

    @Bean
    fun containerBasedAuthenticationFilter(): FilterRegistrationBean<*> {
        val filterRegistration = FilterRegistrationBean(ContainerBasedAuthenticationFilter())
        filterRegistration.initParameters = mapOf(
            "authentication-provider" to KeycloakAuthenticationProvider::class.java.name,
        )
        filterRegistration.order = 201 // make sure the filter is registered after the Spring Security Filter Chain
        filterRegistration.addUrlPatterns("/app/*")
        return filterRegistration
    }

    // The ForwardedHeaderFilter is required to correctly assemble the redirect URL for OAUth2 login.
    // Without the filter, Spring generates an HTTP URL even though the container route is accessed through HTTPS.
    @Bean
    fun forwardedHeaderFilter(): FilterRegistrationBean<ForwardedHeaderFilter?> {
        val filterRegistrationBean = FilterRegistrationBean(ForwardedHeaderFilter())
        filterRegistrationBean.order = Ordered.HIGHEST_PRECEDENCE
        return filterRegistrationBean
    }

    @Bean
    @Order(0)
    fun requestContextListener(): RequestContextListener {
        return RequestContextListener()
    }
}
