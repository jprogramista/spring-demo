package com.example.cloud

import com.example.cloud.properties.VaultProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.example.cloud.clients")
@EnableConfigurationProperties(VaultProperties::class)
class CloudApplication

fun main(args: Array<String>) {
    runApplication<CloudApplication>(*args)
}