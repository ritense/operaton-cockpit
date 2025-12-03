package com.ritense.operaton.cockpit

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.env.Environment
import java.net.InetAddress

@SpringBootApplication
class OperatonCockpitApplication


fun main(args: Array<String>) {
    val applicationContext = runApplication<OperatonCockpitApplication>(*args)
    val environment: Environment = applicationContext.environment
    val logger = KotlinLogging.logger {}

    logger.info {
        """
        Application '${environment.getProperty("spring.application.name")}' is running!
        Active profile(s): [${environment.getProperty("spring.profiles.active")}].
        Local URL: [http://127.0.0.1:${environment.getProperty("server.port")}].
        External URL: [http://${InetAddress.getLocalHost().hostAddress}:${environment.getProperty("server.port")}]
        """.trimIndent()
    }
}
