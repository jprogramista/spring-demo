package com.example.demo.service

import com.example.demo.model.OAuthServiceType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class OAuthService(val userService: UserService, val authorityService: AuthorityService,
                   @Value( "\${github.client.clientId:#{null}}") val github: String?,
                   @Value( "\${google.client.clientId:#{null}}") val google: String?,
                   @Value( "\${facebook.client.clientId:#{null}}") val facebook: String?,
                   @Value( "\${linkedIn.client.clientId:#{null}}") val linkedIn: String?,
                   @Value( "\${twitter.client.clientId:#{null}}") val twitter: String?) {

    val registeredClients: MutableMap<String, OAuthServiceType> = mutableMapOf()

    @PostConstruct
    fun registerClients() {
        github?.let { key -> registeredClients[key] = OAuthServiceType.GitHub }
        google?.let { key ->  registeredClients[key] = OAuthServiceType.Google }
        facebook?.let { key ->  registeredClients[key] = OAuthServiceType.Facebook }
        linkedIn?.let { key ->  registeredClients[key] = OAuthServiceType.LinkedIn }
        twitter?.let { key -> registeredClients[key] = OAuthServiceType.Twitter }
    }

    fun getByClientId(clientId: String) : OAuthServiceType? {
        return registeredClients[clientId]
    }
}