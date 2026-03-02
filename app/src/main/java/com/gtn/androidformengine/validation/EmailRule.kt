package com.gtn.androidformengine.validation

import android.util.Patterns
import com.gtn.androidformengine.core.ValidationResult
import com.gtn.androidformengine.core.ValidationRule

class EmailRule(
    private val message: String = "Invalid email address"
) : ValidationRule {
    override fun validate(value: String?, allValues: Map<String, String?>): ValidationResult {
        if (value.isNullOrBlank()) return ValidationResult.Valid
        return if (Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(message)
        }
    }
}
