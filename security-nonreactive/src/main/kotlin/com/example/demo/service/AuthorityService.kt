package com.example.demo.service

import com.example.demo.model.Authority
import com.example.demo.repository.AuthorityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AuthorityService(@Autowired private val authorityRepository: AuthorityRepository) {

    fun save(authority: Authority) : Authority {
        return authorityRepository.save(authority)
    }

    fun getAuthoritiesByUserId(userId: Long) : List<Authority> {
        return authorityRepository.findByUserId(userId)
    }
}