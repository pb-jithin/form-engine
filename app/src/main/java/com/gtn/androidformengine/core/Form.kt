package com.gtn.androidformengine.core

data class Form(
    val steps: List<FormStep>
) {
    fun validate(values: Map<String, String?>): FormResult {
        val errors = mutableMapOf<String, String>()
        val resolvedValues = values.mapValues { it.value ?: "" }

        steps.flatMap { it.fields }.forEach { field ->
            if (errors.containsKey(field.key)) return@forEach
            val value = values[field.key]
            for (rule in field.rules) {
                when (val result = rule.validate(value, values)) {
                    is ValidationResult.Invalid -> {
                        errors[field.key] = result.message
                        break
                    }
                    ValidationResult.Valid -> Unit
                }
            }
        }

        return FormResult(
            isValid = errors.isEmpty(),
            values = resolvedValues,
            errors = errors
        )
    }
}
