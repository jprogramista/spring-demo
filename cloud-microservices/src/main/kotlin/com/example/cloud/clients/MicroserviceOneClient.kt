package com.example.cloud.clients

import org.springframework.cloud.netflix.ribbon.RibbonClient
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping

@FeignClient(name ="zuul-api-gateway")
@RibbonClient(name ="micro1-app")
@Profile("micro2")
interface MicroserviceOneClient {

    @RequestMapping("/micro1-app/micro1")
    fun micro1(@RequestHeader("Cookie") session: String) : Map<String, String>

}