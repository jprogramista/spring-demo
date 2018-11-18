package com.example.webpage.repository

import com.example.webpage.model.Employee
import com.example.webpage.model.User
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.stereotype.Repository

@NoRepositoryBean
interface BaseRepository<T : User> : JpaRepository<T, Long>

@Repository
interface UserRepository : BaseRepository<User>

@Repository
interface EmployeeRepository : BaseRepository<Employee>