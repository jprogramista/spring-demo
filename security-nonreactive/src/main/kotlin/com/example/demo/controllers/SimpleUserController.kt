package com.example.demo.controllers

import com.example.demo.dto.PagedResponseDto
import com.example.demo.dto.UserDto
import com.example.demo.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class SimpleUserController(val userService: UserService) {

    @GetMapping("/data")
    fun getUserData() : Map<String, Long> {
        return userService.userData()
    }

    @GetMapping("/all")
    fun getAllEnabledUsers() : PagedResponseDto<UserDto> {
        val results = userService.getAllEnabledUsers()
        return PagedResponseDto(results, results.size.toLong(), 1)
    }
}