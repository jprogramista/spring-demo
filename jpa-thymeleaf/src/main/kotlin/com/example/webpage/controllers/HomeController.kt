package com.example.webpage.controllers

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/")
class HomeController {

    @GetMapping("")
    fun home() : String {
        return "redirect:/user/list/all"
    }

}

@ControllerAdvice
class AllControllersAdvice(@Value("\${spring.application.name}") val appName: String) {

    @ExceptionHandler(Exception::class)
    fun handleError() : String {
        return "error.html"
    }

    @ModelAttribute("appName")
    fun allModelsData() : String {
        return appName
    }
}