package com.example.cloud.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("vault")
class VaultProperties {

    var username: String? = null

    var password: String? = null

}