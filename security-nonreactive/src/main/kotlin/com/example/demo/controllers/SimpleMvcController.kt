package com.example.demo.controllers

import com.example.demo.dto.SignUpDto
import com.example.demo.service.OAuthService
import com.example.demo.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.security.Principal
import javax.validation.Valid

@Controller
class SimpleMvcController(val oAuthService: OAuthService, val userService: UserService) {

    @GetMapping("/")
    fun index(model: Model, principal: Principal?) : String {

        model.addAttribute("name", principal?.name)

        model.addAttribute("registeredClients", oAuthService.registeredClients.values.map { value -> value.name }.toList())

        return "index"
    }

    @GetMapping("/sign-up")
    fun signUpForm(signUpDto : SignUpDto) : String {
        return "sign-up"
    }

    @PostMapping("/sign-up")
    fun signUp(@Valid signUpDto : SignUpDto, bindingResult: BindingResult, model: Model, redirectAttributes: RedirectAttributes) : String {

        if (bindingResult.hasErrors()) {
            return "sign-up"
        }

        return try {
            userService.signUp(signUpDto.email, signUpDto.password)
            redirectAttributes.addFlashAttribute("info", "user.registered")
            "redirect:/"
        } catch( e: Exception) {
            "sign-up"
        }

    }

}