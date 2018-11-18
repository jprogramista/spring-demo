package com.example.webpage.controllers

import com.example.webpage.dto.CreateUserDto
import com.example.webpage.facade.UserManagementFacade
import com.example.webpage.model.Country
import com.example.webpage.model.Employee
import com.example.webpage.model.Role
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.AdditionalMatchers.lt
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyLong
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.util.LinkedMultiValueMap

@RunWith(SpringRunner::class)
@WebMvcTest(UserManagementController::class)
class UserManagementControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var userManagementFacade: UserManagementFacade

    /**
     * For Kotlin:
     * https://stackoverflow.com/questions/30305217/is-it-possible-to-use-mockito-in-kotlin
     */
    private fun <T> eq(obj: T): T = Mockito.eq<T>(obj)

    @Before
    fun init() {
        `when`(userManagementFacade.getAllCountries()).thenReturn(listOf())
        `when`(userManagementFacade.getAllRoles()).thenReturn(listOf())
        `when`(userManagementFacade.getCountry(eq("USA"))).thenReturn(Country("USA", "USA"))
        `when`(userManagementFacade.getRole(lt(100L))).thenReturn(Role(name = "role"))
        `when`(userManagementFacade.getRole(eq(100L))).thenReturn(null)
        `when`(userManagementFacade.getEmployee(eq(1L)))
                .thenReturn(Employee(email = "test@example.com", password = "", division = "division"))

    }

    @Test
    fun createUser() {
        mockMvc.perform(get("/user/create"))
                .andExpect(status().isOk)
                .andExpect(model().attributeExists("allRoles", "countries", "userDto"))
                .andExpect(view().name("create-update-user"))
    }

    @Test
    fun editUser() {
        `when`(userManagementFacade.findEmployeeById(anyLong())).thenReturn(Employee(email = "test@example.com", password = "password", division = "first division"))
        mockMvc.perform(get("/user/edit/1"))
                .andExpect(status().isOk)
                .andExpect(model().attributeExists("allRoles", "countries", "userDto"))
                .andExpect(model().attribute("userDto",
                        Matchers.hasProperty<CreateUserDto>("email", Matchers.equalTo("test@example.com"))))
                .andExpect(view().name("create-update-user"))
    }

    @Test
    fun saveUserNoEmail() {
        val map = mapOf("password"          to listOf("test@example.com"),
                        "confirmPassword"   to listOf("test@example.com"),
                        "division"          to listOf("test@example.com"))

        mockMvc.perform(post("/user/save").params(LinkedMultiValueMap(map)))
                .andExpect(model().hasErrors<Any>())
                .andExpect(model().attributeHasFieldErrorCode("userDto", "email", "NotBlank"))
                .andExpect(view().name("create-update-user"))
    }

    @Test
    fun saveUserNoWellFormedEmail() {
        val map = mapOf("email"             to listOf("testexample.com"),
                        "password"          to listOf("test@example.com"),
                        "confirmPassword"   to listOf("test@example.com"),
                        "division"          to listOf("test@example.com"))

        mockMvc.perform(post("/user/save").params(LinkedMultiValueMap(map)))
                .andExpect(model().hasErrors<Any>())
                .andExpect(model().attributeHasFieldErrorCode("userDto", "email", "Email"))
                .andExpect(view().name("create-update-user"))
    }

    @Test
    fun saveUserNoAllowedEmailDomain() {
        val map = mapOf("email"             to listOf("test@ample.com"),
                        "password"          to listOf("test@example.com"),
                        "confirmPassword"   to listOf("test@example.com"),
                        "division"          to listOf("test@example.com"))

        mockMvc.perform(post("/user/save").params(LinkedMultiValueMap(map)))
                .andExpect(model().hasErrors<Any>())
                .andExpect(model().attributeHasFieldErrorCode("userDto", "email", "AllowedDomainsOnly"))
                .andExpect(view().name("create-update-user"))
    }

    @Test
    fun saveUserNoPassword() {
        val map = mapOf("email"     to listOf("test@ample.com"),
                "confirmPassword"   to listOf("test@example.com"),
                "division"          to listOf("test@example.com"))

        mockMvc.perform(post("/user/save").params(LinkedMultiValueMap(map)))
                .andExpect(model().hasErrors<Any>())
                .andExpect(model().attributeHasFieldErrorCode("userDto", "password", "NotBlank"))
                .andExpect(view().name("create-update-user"))
    }

    @Test
    fun saveUserPasswordTooShort() {
        val map = mapOf("email"     to listOf("test@ample.com"),
                "password"          to listOf("test"),
                "confirmPassword"   to listOf("test@example.com"),
                "division"          to listOf("test@example.com"))

        mockMvc.perform(post("/user/save").params(LinkedMultiValueMap(map)))
                .andExpect(model().hasErrors<Any>())
                .andExpect(model().attributeHasFieldErrorCode("userDto", "password", "Size"))
                .andExpect(view().name("create-update-user"))
    }

    @Test
    fun saveUserPasswordTooLong() {
        val map = mapOf("email"     to listOf("test@ample.com"),
                "password"          to listOf("testtesttesttesttesttesttesttesttesttest"),
                "confirmPassword"   to listOf("test@example.com"),
                "division"          to listOf("test@example.com"))

        mockMvc.perform(post("/user/save").params(LinkedMultiValueMap(map)))
                .andExpect(model().hasErrors<Any>())
                .andExpect(model().attributeHasFieldErrorCode("userDto", "password", "Size"))
                .andExpect(view().name("create-update-user"))
    }

    @Test
    fun saveUserPasswordsDontMatch() {
        val map = mapOf("email"     to listOf("test@ample.com"),
                "password"          to listOf("testtesttesttestt"),
                "confirmPassword"   to listOf("test@example.com"),
                "division"          to listOf("test@example.com"))

        mockMvc.perform(post("/user/save").params(LinkedMultiValueMap(map)))
                .andExpect(model().hasErrors<Any>())
                .andExpect(model().attributeHasFieldErrorCode("userDto", "password", "error.passAndConfirm"))
                .andExpect(view().name("create-update-user"))
    }

    @Test
    fun saveUserNoDivision() {
        val map = mapOf("email"             to listOf("test@ample.com"),
                        "password"          to listOf("test@example.com"),
                        "confirmPassword"   to listOf("test@example.com"))

        mockMvc.perform(post("/user/save").params(LinkedMultiValueMap(map)))
                .andExpect(model().hasErrors<Any>())
                .andExpect(model().attributeHasFieldErrorCode("userDto", "division", "NotBlank"))
                .andExpect(view().name("create-update-user"))
    }

    @Test
    fun saveUserNoRole() {
        val map = mapOf("email"     to listOf("test@exaMple.com"),
                "password"          to listOf("test@example.com"),
                "confirmPassword"   to listOf("test@example.com"),
                "division"          to listOf("test@example.com"),
                "roles"             to listOf("1", "2", "100"))

        mockMvc.perform(post("/user/save").params(LinkedMultiValueMap(map)))
                .andExpect(model().hasErrors<Any>())
                .andExpect(model().attributeHasFieldErrorCode("userDto", "roles", "error.rolesUnknown"))
                .andExpect(view().name("create-update-user"))
    }

    @Test
    fun saveUserNoCountry() {
        val map = mapOf("email"     to listOf("test@exaMple.com"),
                "password"          to listOf("test@example.com"),
                "confirmPassword"   to listOf("test@example.com"),
                "division"          to listOf("test@example.com"),
                "address.country"   to listOf("COUNTRY"))

        mockMvc.perform(post("/user/save").params(LinkedMultiValueMap(map)))
                .andExpect(model().hasErrors<Any>())
                .andExpect(model().attributeHasFieldErrorCode("userDto", "address.country", "error.notKnownCountry"))
                .andExpect(view().name("create-update-user"))
    }

    @Test
    fun saveUserValid() {
        val map = mapOf("email"             to listOf("test@exaMple.com"),
                        "password"          to listOf("password"),
                        "confirmPassword"   to listOf("password"),
                        "division"          to listOf("division"),
                        "roles"             to listOf("1", "2"),
                        "address.country"   to listOf("USA"))

        mockMvc.perform(post("/user/save").params(LinkedMultiValueMap(map)))
                .andExpect(model().hasNoErrors<Any>())
                .andExpect(redirectedUrl("/user/list/all"))

    }

    @Test
    fun listUsers() {
        mockMvc.perform(get("/user/list/all"))
                .andExpect(status().isOk)
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name("list-users"))
    }

    @Test
    fun showUser() {
        mockMvc.perform(get("/user/list/1"))
                .andExpect(status().isOk)
                .andExpect(model().attributeExists("user", "jobHistoryDto"))
                .andExpect(model().attribute("jobHistoryDto",
                        Matchers.hasProperty<CreateUserDto>("userId", Matchers.equalTo(1L))))

                .andExpect(view().name("user-details"))
    }

    @Test
    fun addJobNoUserId() {
        val map = mapOf("userId"    to listOf<String>(),
                        "name"      to listOf())

        mockMvc.perform(post("/user/add/job").params(LinkedMultiValueMap(map)))
                .andExpect(view().name("error.html"))
    }

    @Test
    fun addJobNoName() {
        val map = mapOf("userId"    to listOf("1"),
                        "name"      to listOf())

        mockMvc.perform(post("/user/add/job").params(LinkedMultiValueMap(map)))
                .andExpect(model().hasErrors<Any>())
                .andExpect(model().attributeHasFieldErrorCode("jobHistoryDto", "name", "NotBlank"))
                .andExpect(view().name("user-details"))
    }

    @Test
    fun addJobValid() {
        val map = mapOf("userId"    to listOf("1"),
                        "name"      to listOf("name"))

        mockMvc.perform(post("/user/add/job").params(LinkedMultiValueMap(map)))
                .andExpect(model().hasNoErrors<Any>())
                .andExpect(redirectedUrl("/user/list/1"))
    }

    @Test
    fun deleteUser() {
        mockMvc.perform(get("/user/delete/1"))
                .andExpect(redirectedUrl("/user/list/all"))
    }
}