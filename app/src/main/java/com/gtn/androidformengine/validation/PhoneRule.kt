package com.gtn.androidformengine.validation

import android.util.Patterns
import com.gtn.androidformengine.core.ValidationResult
import com.gtn.androidformengine.core.ValidationRule

class PhoneRule(
    private val message: String = "Invalid phone number"
) : ValidationRule {
    override fun validate(value: String?, allValues: Map<String, String?>): ValidationResult {
        if (value.isNullOrBlank()) return ValidationResult.Valid
        return if (Patterns.PHONE.matcher(value).matches()) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(message)
        }
    }
}
