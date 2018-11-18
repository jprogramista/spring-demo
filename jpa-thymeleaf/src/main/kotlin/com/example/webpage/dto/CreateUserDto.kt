package com.example.webpage.dto

import com.example.webpage.model.Address
import com.example.webpage.model.Employee
import com.example.webpage.validator.AllowedDomainsOnly
import javax.validation.constraints.Email
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class JobHistoryDto(var id: Long? = null, var name: String? = null, var description: String? = null)

data class AddressDto(var streetFirstLine: String? = null,
                   var streetSecondaryLine: String? = null,
                   var postalCode: String? = null,
                   var city : String? = null,
                   var country: String? = null)

data class CreateUserDto(var id: Long? = null,
                         @field:NotBlank @field:Email
                         @field:AllowedDomainsOnly
                         var email: String? = null,
                         @field:NotBlank @field:Size(min = 6, max = 20)
                         var password: String? = null,
                         var confirmPassword: String? = null,
                         @field:NotBlank
                         var division: String? = null,
                         var address: AddressDto? = null,
                         var roles: List<Long>? = null,
                         var jobHistories: List<JobHistoryDto>? = null
) {
    companion object {
        fun from(employee: Employee) : CreateUserDto {
            return with(employee) {
                CreateUserDto(id = id, email = email, division = division,
                        address = addressDtoFrom(address),
                        roles = roles?.map { role -> role.id!! })
            }
        }

        private fun addressDtoFrom(address: Address?) : AddressDto? {
            return address?.let {
                with(it) {
                    AddressDto(streetFirstLine = streetFirstLine,
                            streetSecondaryLine = streetSecondaryLine,
                            postalCode = postalCode,
                            city = city,
                            country = country?.abbr)
                }
            }
        }
    }
}

