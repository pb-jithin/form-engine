package com.gtn.androidformengine.validation

import com.gtn.androidformengine.core.ValidationResult
import com.gtn.androidformengine.core.ValidationRule

class RegexRule(
    private val pattern: Regex,
    private val message: String = "Invalid format"
) : ValidationRule {
    override fun validate(value: String?, allValues: Map<String, String?>): ValidationResult {
        if (value.isNullOrEmpty()) return ValidationResult.Valid
        return if (pattern.matches(value)) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(message)
        }
    }
}
