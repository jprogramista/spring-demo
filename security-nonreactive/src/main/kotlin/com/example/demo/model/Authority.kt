package com.example.demo.model

import org.springframework.security.core.GrantedAuthority
import javax.persistence.*

@Entity
@Table(name = "authorities")
data class Authority(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val email : String? = null,
    val userId : Long,
    val role: String
) : GrantedAuthority {
    override fun getAuthority(): String {
        return role
    }
}