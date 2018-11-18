package com.example.webpage.facade

import com.example.webpage.dto.AddJobHistoryDto
import com.example.webpage.dto.CreateUserDto
import com.example.webpage.model.*
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
    fun getEmployee(id: Long): Employee?
    fun addJob(addJobHistoryDto: AddJobHistoryDto)
    fun deleteUser(id: Long)
    fun save(createUserDto: CreateUserDto)
}

@Service
class JpaUserManagementFacade(val userService: UserService,
                              val roleService: RoleService,
                              val countryService: CountryService) : UserManagementFacade {

    //TODO test
    override fun save(createUserDto: CreateUserDto) {
        val employee = createUserDto.let { employeeDto ->
            val address = employeeDto.address?.let {
                Address(streetFirstLine = it.streetFirstLine,
                        streetSecondaryLine = it.streetSecondaryLine,
                        postalCode = it.postalCode,
                        city = it.city,
                        country = it.country?.let { abbr -> countryService.findById(abbr) })
            }
            val roles = employeeDto.roles?.let { ids -> roleService.getRoles(ids)}
            Employee(email = employeeDto.email!!, password = employeeDto.password!!, division = employeeDto.division!!,
                            address = address, roles = roles)
        }
        userService.save(employee)
    }

    override fun deleteUser(id: Long) {
        userService.delete(id)
    }

    //TODO test
    override fun addJob(addJobHistoryDto: AddJobHistoryDto) {
        val employee = userService.findEmployeeById(addJobHistoryDto.userId!!)
        employee?.also {
            val currentJobHistory = it.jobHistory ?: listOf()
            val newJobHistory = JobHistory(name = addJobHistoryDto.name!!, description = addJobHistoryDto.description)
            val employeeModified = it.copy(newJobHistory = currentJobHistory + newJobHistory)
            userService.save(employeeModified)
        }

    }

    override fun getEmployee(id: Long): Employee? {
        return userService.findEmployeeById(id)
    }

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