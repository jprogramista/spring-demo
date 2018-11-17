package com.example.webpage.service

import com.example.webpage.model.Employee
import com.example.webpage.model.User

interface UserService {

    fun getAllEmployees() : List<Employee>

    fun save(user: User)

    fun findEmployeeById(userId: Long): Employee?
}