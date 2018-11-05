package com.example.demo.configuration.oauth

import com.example.demo.model.Authority
import com.example.demo.model.User
import com.example.demo.service.OAuthService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import java.io.IOException
import java.lang.IllegalStateException
import java.time.LocalDateTime
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class OAuthSuccessHandler(defaultTargetUrl: String, private val oAuthService: OAuthService) : SimpleUrlAuthenticationSuccessHandler(defaultTargetUrl) {

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(request: HttpServletRequest,
                                         response: HttpServletResponse,
                                         authenticationObj: Authentication) {

        val authentication = authenticationObj as OAuth2Authentication
        val clientId = authentication.oAuth2Request.clientId

        val details = authentication.userAuthentication.details as LinkedHashMap<*, *>
        val oAuthServiceType = oAuthService.getByClientId(clientId) ?: throw IllegalStateException()
        val oAuthId = oAuthServiceType.getServiceId(details)
        val user = oAuthService.userService.getOauthUser(oAuthServiceType, oAuthId)
        val localAuthentication = if (user == null) {
            val localUser = oAuthService.userService.save(User(oauthServiceType = oAuthServiceType, oauthId = oAuthId, created = LocalDateTime.now(), enabled = true, email = oAuthServiceType.getEmail(details)))
            val authority = oAuthService.authorityService.save(Authority(email = localUser.email, userId = localUser.id!!, role = "ROLE_USER"))
            UsernamePasswordAuthenticationToken(localUser.id, "N/A", listOf(authority))
        } else {
            val authorities = oAuthService.authorityService.getAuthoritiesByUserId(user.id!!)
            UsernamePasswordAuthenticationToken(user.id, "N/A", authorities)
        }

        SecurityContextHolder.getContext().authentication = localAuthentication

        super.onAuthenticationSuccess(request, response, localAuthentication)
    }
}