package com.gtn.androidformengine.validation

import com.gtn.androidformengine.core.ValidationResult
import com.gtn.androidformengine.core.ValidationRule

class MinLengthRule(
    private val minLength: Int,
    private val message: String = "Minimum length is $minLength"
) : ValidationRule {
    override fun validate(value: String?, allValues: Map<String, String?>): ValidationResult {
        if (value.isNullOrEmpty()) return ValidationResult.Valid
        return if (value.length >= minLength) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(message)
        }
    }
}
