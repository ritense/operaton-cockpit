package com.ritense.operaton.cockpit

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.stereotype.Service

/**
 * Keycloak Logout Handler.
 */
@Service
class KeycloakLogoutHandler(
    clientRegistrationRepository: ClientRegistrationRepository
) : LogoutSuccessHandler {

    private val delegate: OidcClientInitiatedLogoutSuccessHandler =
        OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository).apply {
            setPostLogoutRedirectUri("{baseUrl}")
        }

    override fun onLogoutSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication?
    ) {
        logger.debug { "Logging out via Keycloak OIDC end_session_endpoint" }
        delegate.onLogoutSuccess(request, response, authentication)
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
