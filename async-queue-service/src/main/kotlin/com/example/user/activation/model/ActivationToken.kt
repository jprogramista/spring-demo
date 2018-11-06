package com.example.user.activation.model

import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "activation_tokens")
data class ActivationToken(        @Id
                                   @GeneratedValue(strategy = GenerationType.IDENTITY)
                                   val id: Long? = null,
                                   val userId: Long,
                                   val token: String,
                                   @CreationTimestamp
                                   val created: LocalDateTime,
                                   val used: Boolean = false,
                                   val expired: Boolean = false)