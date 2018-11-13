package com.example.cloud.controllers

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

@RestController
@Profile("micro1")
class MicroserviceOneController(@Value("\${server.port}") val port: Int) {

    val logger = LoggerFactory.getLogger(MicroserviceOneController::class.java)

    @GetMapping("/micro1")
    @ResponseBody
    fun getData(httpServletRequest: HttpServletRequest) : Map<String, String> {
        val cookies = httpServletRequest.cookies?.joinToString { cookie -> cookie.name + " = " + cookie.value }
        logger.info("Cookies 1: $cookies")

        val map = mapOf("currentTime"           to LocalDateTime.now().toString(),
                        "microservice1port"     to port.toString(),
                        "microservice1session"  to httpServletRequest.session.id)
        return map
    }
}