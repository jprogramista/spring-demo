package com.example.demo.repository

import com.example.demo.model.OAuthServiceType
import com.example.demo.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(path = "users")
interface UserRepository : CrudRepository<User, Long> {

    fun findByEnabledIsTrue() : List<User>

    fun countByEnabledIsTrue() : Long

    fun findFirstByOauthServiceTypeAndOauthId(oAuthServiceType: OAuthServiceType, oAuthId: String): User?
}