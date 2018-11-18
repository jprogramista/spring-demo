package com.example.webpage.validator

import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [AllowedDomainsValidator::class])
annotation class AllowedDomainsOnly(val message : String = "allowed domains only",
                                    val groups: Array<KClass<*>> = [],
                                    val payload: Array<KClass<*>> = []) // should extend javax.validation.Payload ?

class AllowedDomainsValidator : ConstraintValidator<AllowedDomainsOnly, String> {

    private val allowedDomains = setOf("example.com", "gmail.com")

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return value?.let {
            val parts = it.split("@")
            return if (parts.size == 2) {
                allowedDomains.contains(parts[1].toLowerCase())
            } else {
                // skip validation
                true
            }
        } ?: true
    }
}