package com.gtn.androidformengine.core

data class Field(
    val key: String,
    val type: FieldType,
    var label: String? = null,
    var hint: String? = null,
    val rules: MutableList<ValidationRule> = mutableListOf()
)
