package com.example.webpage.repository

import com.example.webpage.model.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.junit4.SpringRunner
import javax.validation.ConstraintViolationException

@RunWith(SpringRunner::class)
@DataJpaTest
class UserRepositoryTest {

    @Rule
    @JvmField
    final val expectedException : ExpectedException = ExpectedException.none()

    @Autowired
    lateinit var countryRepository: CountryRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Before
    fun init() {
        val country = Country("USA", "United States")
        countryRepository.save(country)
        val roles = listOf(Role(name = "Role 1"), Role(name = "Role 2"), Role(name = "Role 3"))
        roleRepository.saveAll(roles)
    }

    @Test
    fun saveEmployeeNoAddress() {
        val employee = Employee("test@example.com", "password", "First Division")
        val saved = userRepository.save(employee)
        assertThat(saved.id).isNotNull()
    }

    @Test
    fun saveEmployeeSameEmail() {
        expectedException.expect(DataIntegrityViolationException::class.java)
        val employee = Employee("test@example.com", "password", "First Division")
        userRepository.save(employee)
        val employee2 = Employee("test@example.com", "password", "First Division")
        userRepository.saveAndFlush(employee2)
    }

    @Test
    fun saveEmployeeWithAddress() {
        val country = countryRepository.findById("USA")
        assertThat(country).isPresent

        val address = Address("street 1", "street 2", "90909", "London", country.get())
        val employee = Employee("test@example.com", "password", "First Division", address)

        userRepository.save(employee)
        val findAll = userRepository.findAll()

        assertThat(findAll).hasSize(1)
        val user = findAll.first() as Employee
        val userAddress = user.address
        assertThat(userAddress).isSameAs(address)

        // transaction is rollbacked but id incremented with stored procedure: call next value for hibernate_sequence
        // assertThat(saved).hasFieldOrPropertyWithValue("id", 2L)
    }

    @Test
    fun saveEmployeeWithRoles() {
        val roles = roleRepository.findAll().toList()
        val employee = Employee("test@example.com", "password", "First Division", roles = roles)
        userRepository.save(employee)
        val findAll = userRepository.findAll()

        assertThat(findAll).hasSize(1)
        val user = findAll.first() as Employee
        assertThat(user.roles).containsAll(roles)
    }

    @Test
    fun saveEmployeeWithJobHistory() {
        val employee = Employee("test@example.com", "password", "First Division",
                jobHistory = listOf(
                        JobHistory(name = "Job 1"),
                        JobHistory(name = "Job 2", description = "Description"),
                        JobHistory(name = "Job 3")
                ))
        userRepository.save(employee)
        val findAll = userRepository.findAll()

        assertThat(findAll).hasSize(1)
        val user = findAll.first() as Employee
        assertThat(user.jobHistory).hasSize(3)
    }

    @Test
    fun checkUpdateTimestamp() {
        val employee = Employee("test@example.com", "password", "First Division")
        userRepository.save(employee)
        val saved = userRepository.findAll().first() as Employee
        assertThat(saved.updatedAt).isNotNull()
        val updatedAt = saved.updatedAt
        val employeeCopy = saved.copy()
        userRepository.save(employeeCopy)
        val savedAfter = userRepository.findAll().first()
        assertThat(updatedAt).isBefore(savedAfter.updatedAt)
    }

    @Test(expected = ConstraintViolationException::class)
    fun emptyEmailValidation() {
        val employee = Employee("", "password", "First Division")
        userRepository.saveAndFlush(employee)
    }

    @Test(expected = ConstraintViolationException::class)
    fun notEmailValidation() {
        val employee = Employee("notanemail", "password", "First Division")
        userRepository.saveAndFlush(employee)
    }

}