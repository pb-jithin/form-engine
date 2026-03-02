package com.gtn.androidformengine.core

data class FormResult(
    val isValid: Boolean,
    val values: Map<String, String>,
    val errors: Map<String, String>
) {
    operator fun get(key: String): String? = values[key]
}
