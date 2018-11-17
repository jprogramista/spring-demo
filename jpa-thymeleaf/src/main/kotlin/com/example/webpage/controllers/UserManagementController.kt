package com.example.webpage.controllers

import com.example.webpage.dto.CreateUserDto
import com.example.webpage.facade.UserManagementFacade
import com.example.webpage.validator.UserManagementValidator
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.Valid


@Controller
@RequestMapping("/user")
class UserManagementController(private val userManagementFacade: UserManagementFacade) {

    val logger = LoggerFactory.getLogger(UserManagementController::class.java)

    @InitBinder
    fun addUserManagementValidator(webDataBinder: WebDataBinder) {
        webDataBinder.addValidators(UserManagementValidator(userManagementFacade))
    }

    @GetMapping(value = ["/create"])
    fun createUser(model: Model) : String {
        model.addAttribute("allRoles", userManagementFacade.getAllRoles())
        model.addAttribute("countries", userManagementFacade.getAllCountries())
        model.addAttribute("userDto", CreateUserDto())
        return "create-update-user"
    }

    @GetMapping(value = ["/edit/{userId}"])
    fun editUser(@PathVariable("userId") userId: Long, model: Model) : String {
        model.addAttribute("allRoles", userManagementFacade.getAllRoles())
        model.addAttribute("countries", userManagementFacade.getAllCountries())

        val employee = userManagementFacade.findEmployeeById(userId)!!
        model.addAttribute("userDto", CreateUserDto.from(employee))

        return "create-update-user"
    }

    @PostMapping(value = ["/save"])
    fun saveUser(@Valid @ModelAttribute("userDto") createUserDto: CreateUserDto, bindingResult: BindingResult, model: Model, redirectAttributes: RedirectAttributes) : String {
        logger.info("Saving user {}", createUserDto)
        logger.info("BindingResult errors: ${bindingResult.hasErrors()}")

        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", userManagementFacade.getAllRoles())
            model.addAttribute("countries", userManagementFacade.getAllCountries())
            return "create-update-user"
        }

        return "redirect:/user/list/all"
    }

    @GetMapping(value = ["/list/all"])
    fun listUsers(model: Model) : String {
        model.addAttribute("users", userManagementFacade.getAllEmployees())
        return "list-users"
    }

    @GetMapping(value = ["/list/{id}"])
    fun showUsers(@PathVariable("id") id: Long, model: Model) : String {
        return "user-details"
    }

    @GetMapping(value = ["/delete/{id}"])
    fun deleteUser(@PathVariable("id") id: Long) : String {
        return "redirect:/user/list/all"
    }
}
