package com.gtn.androidformengine.validation

import com.gtn.androidformengine.core.ValidationResult
import com.gtn.androidformengine.core.ValidationRule

class MaxLengthRule(
    private val maxLength: Int,
    private val message: String = "Maximum length is $maxLength"
) : ValidationRule {
    override fun validate(value: String?, allValues: Map<String, String?>): ValidationResult {
        if (value.isNullOrEmpty()) return ValidationResult.Valid
        return if (value.length <= maxLength) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(message)
        }
    }
}
