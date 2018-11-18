package com.example.webpage

import com.example.webpage.model.Address
import com.example.webpage.model.Country
import com.example.webpage.model.Employee
import com.example.webpage.model.Role
import com.example.webpage.repository.CountryRepository
import com.example.webpage.repository.RoleRepository
import com.example.webpage.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class CommandLineAppStartupRunner(val roleRepository: RoleRepository,
                                  val countryRepository: CountryRepository,
                                  val userRepository: UserRepository) : CommandLineRunner {

    override fun run(vararg args: String?) {
        val role = Role(name = "Regular User")
        roleRepository.saveAndFlush(role)
        roleRepository.saveAndFlush(Role(name = "Test Role"))

        countryRepository.saveAll(listOf(Country("USA", "United States"), Country("GBR", "United Kingdom")))

        val country = countryRepository.findById("USA")

        for (i in 1 .. 10) {
            val employee = Employee(email = "test$i@example.com",
                                    password = "password",
                                    division = "First division",
                                    roles = listOf(role),
                                    address = Address("Street 1", "Street 2", "11111", "City", country.get()))
            userRepository.saveAndFlush(employee)
        }
    }

}