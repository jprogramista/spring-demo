package com.example.webpage

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebPageJpaThymeleafApplication

fun main(args: Array<String>) {
    runApplication<WebPageJpaThymeleafApplication>(*args)
}