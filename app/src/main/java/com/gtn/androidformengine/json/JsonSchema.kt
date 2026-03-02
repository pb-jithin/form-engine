package com.gtn.androidformengine.json

data class JsonSchema(
    val steps: List<JsonStep>
)

data class JsonStep(
    val title: String? = null,
    val fields: List<JsonField>
)

data class JsonField(
    val type: String,
    val key: String,
    val label: String? = null,
    val hint: String? = null,
    val rules: List<Any> = emptyList()
)
