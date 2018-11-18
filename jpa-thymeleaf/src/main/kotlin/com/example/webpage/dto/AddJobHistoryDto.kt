package com.example.webpage.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class AddJobHistoryDto(var id: Long? = null,
                            @field:NotNull
                            var userId: Long? = null,
                            @field:NotBlank
                            var name: String? = null,
                            var description: String? = null)
