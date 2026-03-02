package com.gtn.androidformengine.validation

import com.gtn.androidformengine.core.ValidationResult
import com.gtn.androidformengine.core.ValidationRule

class MatchesFieldRule(
    private val fieldKey: String,
    private val message: String = "Fields do not match"
) : ValidationRule {
    override fun validate(value: String?, allValues: Map<String, String?>): ValidationResult {
        val otherValue = allValues[fieldKey]
        if (value.isNullOrEmpty() || otherValue.isNullOrEmpty()) return ValidationResult.Valid
        return if (value == otherValue) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(message)
        }
    }
}
