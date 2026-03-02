package com.gtn.androidformengine.validation

import com.gtn.androidformengine.core.ValidationResult
import com.gtn.androidformengine.core.ValidationRule

class PasswordRule(
    private val message: String = "Password must be at least 8 characters and include a number"
) : ValidationRule {
    private val pattern = Regex("^(?=.*\\d).{8,}$")

    override fun validate(value: String?, allValues: Map<String, String?>): ValidationResult {
        if (value.isNullOrEmpty()) return ValidationResult.Valid
        return if (pattern.matches(value)) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(message)
        }
    }
}
