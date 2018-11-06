package com.example.user.activation.repository

import com.example.user.activation.model.ActivationToken
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ActivationTokenRepository : CrudRepository<ActivationToken, Long> {

    fun findByUserIdAndExpiredIsFalse(userId: Long) : List<ActivationToken>
    fun findByUserIdAndTokenAndExpiredIsFalseAndUsedIsFalse(userId: Long, token: String): ActivationToken?

}