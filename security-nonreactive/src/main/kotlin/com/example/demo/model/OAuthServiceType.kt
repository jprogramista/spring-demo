package com.example.demo.model

enum class OAuthServiceType(val idKey: String, val email: String) {
    GitHub("id", "email"),
    Twitter("", ""),
    Facebook("id", "email"),
    Google("sub", "email"),
    LinkedIn("", "");


    fun getServiceId(details: Map<*, *>) : String {
        return details[idKey].toString()
    }

    fun getEmail(details: Map<*, *>) : String {
        return requireNotNull(details[email]).toString()
    }
}