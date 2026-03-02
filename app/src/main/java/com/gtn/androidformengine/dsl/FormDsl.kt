package com.gtn.androidformengine.dsl

import com.gtn.androidformengine.core.Field
import com.gtn.androidformengine.core.FieldType
import com.gtn.androidformengine.core.Form
import com.gtn.androidformengine.core.FormStep
import com.gtn.androidformengine.core.ValidationRule
import com.gtn.androidformengine.validation.EmailRule
import com.gtn.androidformengine.validation.MatchesFieldRule
import com.gtn.androidformengine.validation.MaxLengthRule
import com.gtn.androidformengine.validation.MinLengthRule
import com.gtn.androidformengine.validation.PasswordRule
import com.gtn.androidformengine.validation.PhoneRule
import com.gtn.androidformengine.validation.RegexRule
import com.gtn.androidformengine.validation.RequiredRule

class FieldBuilder internal constructor(
    private val formField: Field
) {
    var label: String?
        get() = formField.label
        set(value) {
            formField.label = value
        }

    var hint: String?
        get() = formField.hint
        set(value) {
            formField.hint = value
        }

    fun required(message: String = "Field is required") {
        formField.rules.add(RequiredRule(message))
    }

    fun email(message: String = "Invalid email address") {
        formField.rules.add(EmailRule(message))
    }

    fun phone(message: String = "Invalid phone number") {
        formField.rules.add(PhoneRule(message))
    }

    fun password(message: String = "Password must be at least 8 characters and include a number") {
        formField.rules.add(PasswordRule(message))
    }

    fun minLength(length: Int, message: String = "Minimum length is $length") {
        formField.rules.add(MinLengthRule(length, message))
    }

    fun maxLength(length: Int, message: String = "Maximum length is $length") {
        formField.rules.add(MaxLengthRule(length, message))
    }

    fun regex(pattern: String, message: String = "Invalid format") {
        formField.rules.add(RegexRule(pattern.toRegex(), message))
    }

    fun matches(fieldKey: String, message: String = "Fields do not match") {
        formField.rules.add(MatchesFieldRule(fieldKey, message))
    }

    fun addRule(rule: ValidationRule) {
        formField.rules.add(rule)
    }

    internal fun build(): Field = formField
}

class StepBuilder internal constructor(
    private val title: String?
) {
    private val fields = mutableListOf<Field>()

    fun input(key: String, block: FieldBuilder.() -> Unit = {}) {
        val builder = FieldBuilder(Field(key, FieldType.INPUT))
        builder.block()
        fields.add(builder.build())
    }

    fun password(key: String, block: FieldBuilder.() -> Unit = {}) {
        val builder = FieldBuilder(Field(key, FieldType.PASSWORD))
        builder.block()
        fields.add(builder.build())
    }

    internal fun build(): FormStep = FormStep(title, fields.toList())
}

class FormBuilder internal constructor() {
    private val steps = mutableListOf<FormStep>()

    fun step(title: String? = null, block: StepBuilder.() -> Unit) {
        val builder = StepBuilder(title)
        builder.block()
        steps.add(builder.build())
    }

    internal fun build(): Form = Form(steps.toList())
}

fun form(block: StepBuilder.() -> Unit): Form {
    val builder = StepBuilder(null)
    builder.block()
    return Form(listOf(builder.build()))
}

fun multiStepForm(block: FormBuilder.() -> Unit): Form {
    val builder = FormBuilder()
    builder.block()
    return builder.build()
}
