package com.gtn.androidformengine.validation

import com.gtn.androidformengine.core.ValidationResult
import com.gtn.androidformengine.core.ValidationRule

class RequiredRule(
    private val message: String = "Field is required"
) : ValidationRule {
    override fun validate(value: String?, allValues: Map<String, String?>): ValidationResult {
        return if (value.isNullOrBlank()) {
            ValidationResult.Invalid(message)
        } else {
            ValidationResult.Valid
        }
    }
}
