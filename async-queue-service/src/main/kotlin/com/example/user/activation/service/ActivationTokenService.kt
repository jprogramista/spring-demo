package com.example.user.activation.service

import com.example.common.dto.ActivationMessageDto
import com.example.user.activation.model.ActivationToken
import com.example.user.activation.repository.ActivationTokenRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class ActivationTokenService(val activationTokenRepository: ActivationTokenRepository) {

    fun createActivationToken(activationMessageDto: ActivationMessageDto) : ActivationToken {
        activationTokenRepository.findByUserIdAndExpiredIsFalse(activationMessageDto.userId)
                .forEach {token ->
                    run {
                        activationTokenRepository.save(token.copy(expired = true))
                    }
                }
        val token = ActivationToken(userId = activationMessageDto.userId, token = UUID.randomUUID().toString(), created = LocalDateTime.now())
        return activationTokenRepository.save(token)
    }

    fun activateUser(userId: Long, token: String): Boolean {
        val tokenRetrieved = activationTokenRepository.findByUserIdAndTokenAndExpiredIsFalseAndUsedIsFalse(userId, token)
        return tokenRetrieved?.let {
            val activationToken = it.copy(used = true, expired = true)
            activationTokenRepository.save(activationToken)
            true
        } ?: false
    }
}