package com.example.webpage.facade

import com.example.webpage.model.Country
import com.example.webpage.model.Employee
import com.example.webpage.model.Role
import com.example.webpage.service.CountryService
import com.example.webpage.service.RoleService
import com.example.webpage.service.UserService
import org.springframework.stereotype.Service

interface UserManagementFacade {
    fun getAllRoles(): List<Role>
    fun getAllCountries(): List<Country>
    fun getAllEmployees(): List<Employee>
    fun findEmployeeById(userId: Long): Employee?
    fun getCountry(abbr: String): Country?
    fun getRole(id: Long): Role?
}

@Service
class JpaUserManagementFacade(val userService: UserService,
                              val roleService: RoleService,
                              val countryService: CountryService) : UserManagementFacade {
    override fun getRole(id: Long): Role? {
        return roleService.getRole(id)
    }

    override fun getCountry(abbr: String): Country? {
        return countryService.findById(abbr)
    }

    override fun findEmployeeById(userId: Long): Employee? {
        return userService.findEmployeeById(userId)
    }

    override fun getAllRoles() : List<Role> {
        return roleService.findAll()
    }

    override fun getAllCountries() : List<Country> {
        return countryService.findAll()
    }

    override fun getAllEmployees() : List<Employee> {
        return userService.getAllEmployees()
    }
}