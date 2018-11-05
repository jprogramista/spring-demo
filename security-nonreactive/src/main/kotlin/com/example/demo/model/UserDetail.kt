package com.example.demo.model

import com.example.demo.dto.UserDetailDto
import javax.persistence.*

@Entity
@Table(name = "details")
data class UserDetail(@Id
                      @GeneratedValue(strategy = GenerationType.IDENTITY)
                      val id: Long? = null,
                      @ManyToOne
                      val user: User? = null,
                      val name: String,
                      val value: String) {

    fun toDto(): UserDetailDto {
        return UserDetailDto(this.name, this.value)
    }
}