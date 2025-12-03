package com.ritense.operaton.cockpit

import jakarta.servlet.http.HttpServletRequest
import org.operaton.bpm.engine.ProcessEngine
import org.operaton.bpm.engine.rest.security.auth.AuthenticationResult
import org.operaton.bpm.engine.rest.security.auth.impl.ContainerBasedAuthenticationProvider
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.oidc.user.OidcUser

/**
 * OAuth2 Authentication Provider for usage with Keycloak and KeycloakIdentityProviderPlugin.
 */
class KeycloakAuthenticationProvider : ContainerBasedAuthenticationProvider() {
    override fun extractAuthenticatedUser(request: HttpServletRequest?, engine: ProcessEngine): AuthenticationResult {
        // Extract user-name-attribute of the OAuth2 token

        val authentication = SecurityContextHolder.getContext().authentication as? OAuth2AuthenticationToken
        val principal = authentication?.principal as? OidcUser

        val userId = principal?.name
        if (userId.isNullOrBlank()) {
            return AuthenticationResult.unsuccessful()
        }

        // Authentication successful
        val authenticationResult = AuthenticationResult(userId, true)
        authenticationResult.setGroups(getUserGroups(userId, engine))

        return authenticationResult
    }

    private fun getUserGroups(userId: String?, engine: ProcessEngine): List<String> {
        return engine.identityService.createGroupQuery().groupMember(userId).list()
            .map { it.id }
    }
}
