package com.example.demo.feign

import feign.Param
import feign.RequestLine
import org.springframework.web.bind.annotation.RequestParam

interface AuthenticationClient {

    @RequestLine("POST /activate?userId={userId}&token={token}")
    fun activate(@Param("userId") userId: Long, @Param("token") token: String): Boolean

}