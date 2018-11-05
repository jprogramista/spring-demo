package com.example.demo.service

import com.example.demo.dto.UserDto
import com.example.demo.model.OAuthServiceType
import com.example.demo.model.User
import com.example.demo.repository.UserRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
@PreAuthorize("hasRole('USER')")
class UserService(private val userRepository: UserRepository, private val passwordEncoder: PasswordEncoder) {

    fun userData() : Map<String, Long> {
        return mapOf("all" to userRepository.count(), "enabled" to userRepository.countByEnabledIsTrue())
    }

    @PreAuthorize("hasRole('ADMIN')")
    fun getAllEnabledUsers() : List<UserDto> {
        return userRepository.findByEnabledIsTrue().map { user -> user.toDto() }
    }

    fun getOauthUser(oAuthServiceType: OAuthServiceType, oAuthId: String): User? {
        return userRepository.findFirstByOauthServiceTypeAndOauthId(oAuthServiceType, oAuthId)
    }

    fun save(user: User): User {
        return userRepository.save(user)
    }

    @PreAuthorize("permitAll()")
    fun signUp(email: String, password: String): User {
        val user = User(email = email, password = passwordEncoder.encode(password), enabled = false, created = LocalDateTime.now())
        return userRepository.save(user)
    }
}