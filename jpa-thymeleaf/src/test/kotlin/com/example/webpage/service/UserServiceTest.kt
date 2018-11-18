package com.example.webpage.service

import com.example.webpage.model.Employee
import com.example.webpage.repository.EmployeeRepository
import com.example.webpage.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserServiceTest {

    private val userRepository = mock(UserRepository::class.java)
    private val employeeRepository = mock(EmployeeRepository::class.java)

    private val userService = JpaUserService(userRepository, employeeRepository)

    @Test
    fun getAllEmployeesReturnsCorrectNumberOfEmployees() {

        `when`(employeeRepository.findAll()).thenReturn(listOf(
                Employee("test@example.com", "password", "division"),
                Employee("test@example.com", "password", "division"),
                Employee("test@example.com", "password", "division")
        ))
        assertThat(userService.getAllEmployees()).hasSize(3)

    }
}