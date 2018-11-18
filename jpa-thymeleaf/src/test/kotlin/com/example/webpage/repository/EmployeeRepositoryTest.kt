package com.example.webpage.repository

import com.example.webpage.model.Employee
import com.example.webpage.model.Stackholder
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var employeeRepository: EmployeeRepository

    @Before
    fun init() {
        userRepository.saveAndFlush(Employee("test@example.com", "password", "First"))
        userRepository.saveAndFlush(Stackholder("stack@example.com", "password", "First", 100))
        userRepository.saveAndFlush(Employee("test2@example.com", "password", "Second"))
        userRepository.saveAndFlush(Employee("test3@example.com", "password", "Third"))
    }

    @Test
    fun employeeFetchedCorrectly() {
        val findAll = employeeRepository.findAll()
        assertThat(findAll).hasSize(3)
    }
}