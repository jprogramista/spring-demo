package com.example.cloud.controllers

import com.example.cloud.clients.MicroserviceOneClient
import com.example.cloud.properties.VaultProperties
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest


@RestController
@Profile("micro2")
class MicroserviceTwoController(@Value("\${server.port}") val port: Int,
                                val microserviceOneClient: MicroserviceOneClient,
                                val vaultProperties: VaultProperties) {

    val logger = LoggerFactory.getLogger(MicroserviceTwoController::class.java)

    @GetMapping("/micro2")
    @ResponseBody
    fun getMicroserviceOneData(httpServletRequest: HttpServletRequest, @CookieValue("SESSION") session: String) : Map<String, String> {

        val cookies = httpServletRequest.cookies?.joinToString { cookie -> cookie.name + " = " + cookie.value }
        logger.info("Cookies 2: $cookies")
        logger.info("@CookieValue(\"SESSION\"): $session")

        val result = microserviceOneClient.micro1("SESSION=$session")
        val respTransformed = result.map { (k, v) -> k.toString() + " " + v.toString() }?.joinToString()
        return mapOf(
                "transformed"           to respTransformed.orEmpty(),
                "microservice2port"     to port.toString(),
                "microservice2session"  to httpServletRequest.session.id,
                "vaultUser"             to vaultProperties.username.orEmpty(),
                "vaultPassword"         to vaultProperties.password.orEmpty())
    }


}