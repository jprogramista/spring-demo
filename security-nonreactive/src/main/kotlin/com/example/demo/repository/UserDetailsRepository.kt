package com.example.demo.repository

import com.example.demo.model.UserDetail
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "details", path = "details")
interface UserDetailsRepository : PagingAndSortingRepository<UserDetail, Long> {

    fun findByUserId(@Param("user") id: Long) : List<UserDetail>

}