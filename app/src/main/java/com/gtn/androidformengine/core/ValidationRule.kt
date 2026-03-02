package com.gtn.androidformengine.core

interface ValidationRule {
    fun validate(value: String?, allValues: Map<String, String?> = emptyMap()): ValidationResult
}
