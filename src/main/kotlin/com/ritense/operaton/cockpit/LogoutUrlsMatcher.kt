package com.ritense.operaton.cockpit

import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.web.server.autoconfigure.ServerProperties
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.stereotype.Component

@Component
class LogoutUrlsMatcher(serverProperties: ServerProperties) : RequestMatcher {
    val logoutUrls: MutableList<*> = mutableListOf<String?>(
        "${serverProperties.servlet.contextPath}/app/tasklist/default/logout",
        "${serverProperties.servlet.contextPath}/app/cockpit/default/logout",
        "${serverProperties.servlet.contextPath}/app/admin/default/logout",
        "${serverProperties.servlet.contextPath}/app/welcome/default/logout"
    )

    override fun matches(request: HttpServletRequest): Boolean {
        return logoutUrls.stream()
            .filter { lu -> request.requestURI.equals(lu as String?, ignoreCase = true) }
            .findFirst()
            .isPresent
    }
}
