package com.example.webpage.service

import com.example.webpage.model.Employee
import com.example.webpage.model.User
import com.example.webpage.repository.EmployeeRepository
import com.example.webpage.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class JpaUserService(val userRepository: UserRepository, val employeeRepository: EmployeeRepository) : UserService {

    override fun delete(id: Long) {
        userRepository.deleteById(id)
    }

    override fun findEmployeeById(userId: Long): Employee? {
        return employeeRepository.findById(userId).orElse(null)
    }

    override fun getAllEmployees(): List<Employee> {
        return employeeRepository.findAll()
    }

    override fun save(user: User) {
        userRepository.saveAndFlush(user)
    }
}