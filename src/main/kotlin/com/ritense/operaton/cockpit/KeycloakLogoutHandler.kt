package com.ritense.operaton.cockpit

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.RedirectStrategy
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.stereotype.Service
import java.io.IOException

/**
 * Keycloak Logout Handler.
 */
@Service
class KeycloakLogoutHandler(@Value("\${spring.security.oauth2.client.provider.keycloak.authorization-uri:}") oauth2UserAuthorizationUri: String?) :
    LogoutSuccessHandler {
    /** Redirect strategy.  */
    private val redirectStrategy: RedirectStrategy = DefaultRedirectStrategy()

    /** Keycloak's logout URI.  */
    private var oauth2UserLogoutUri: String? = null

    /**
     * Default constructor.
     * @param oauth2UserAuthorizationUri configured keycloak authorization URI
     */
    init {
        if (!oauth2UserAuthorizationUri.isNullOrBlank()) {
            // in order to get the valid logout uri: simply replace "/auth" at the end of the user authorization uri with "/logout"
            this.oauth2UserLogoutUri = oauth2UserAuthorizationUri.replace("openid-connect/auth", "openid-connect/logout")
        }
    }

    /**
     * {@inheritDoc}
     */
    @Throws(IOException::class, ServletException::class)
    override fun onLogoutSuccess(request: HttpServletRequest, response: HttpServletResponse?, authentication: Authentication) {
        if (!oauth2UserLogoutUri.isNullOrBlank()) {
            // Calculate redirect URI for Keycloak, something like http://<host:port>/operaton
            val requestUrl = request.requestURL.toString()
            val redirectUri = requestUrl.substring(0, requestUrl.indexOf("/app"))
            // Complete logout URL
            val logoutUrl =
                "$oauth2UserLogoutUri?post_logout_redirect_uri=$redirectUri&id_token_hint=" + (authentication.principal as OidcUser).idToken
                    .tokenValue

            // Do logout by redirecting to Keycloak logout
            logger.debug { "Redirecting to logout URL $logoutUrl" }
            redirectStrategy.sendRedirect(request, response, logoutUrl)
        }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
