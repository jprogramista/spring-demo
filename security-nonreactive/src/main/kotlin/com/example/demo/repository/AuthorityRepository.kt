package com.example.demo.repository

import com.example.demo.model.Authority
import org.springframework.data.repository.CrudRepository

interface AuthorityRepository : CrudRepository<Authority, Long> {

    fun findByUserId(userId: Long) : List<Authority>

}