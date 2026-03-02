package com.gtn.androidformengine.json

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
import org.json.JSONArray
import org.json.JSONObject

object JsonFormParser {
    fun parse(jsonString: String): Form {
        val root = JSONObject(jsonString)
        val stepsJson = root.optJSONArray("steps") ?: JSONArray()
        val steps = mutableListOf<FormStep>()

        for (i in 0 until stepsJson.length()) {
            val stepJson = stepsJson.getJSONObject(i)
            val title = stepJson.optString("title", null)
            val fieldsJson = stepJson.optJSONArray("fields") ?: JSONArray()
            val fields = mutableListOf<Field>()

            for (j in 0 until fieldsJson.length()) {
                val fieldJson = fieldsJson.getJSONObject(j)
                val type = parseFieldType(fieldJson.optString("type", "input"))
                val key = fieldJson.getString("key")
                val field = Field(
                    key = key,
                    type = type,
                    label = fieldJson.optString("label", null),
                    hint = fieldJson.optString("hint", null)
                )
                val rulesJson = fieldJson.optJSONArray("rules") ?: JSONArray()
                for (k in 0 until rulesJson.length()) {
                    val rule = parseRule(rulesJson.get(k))
                    if (rule != null) field.rules.add(rule)
                }
                fields.add(field)
            }

            steps.add(FormStep(title, fields))
        }

        return Form(steps)
    }

    private fun parseFieldType(raw: String): FieldType {
        return when (raw.lowercase()) {
            "password" -> FieldType.PASSWORD
            else -> FieldType.INPUT
        }
    }

    private fun parseRule(raw: Any): ValidationRule? {
        return when (raw) {
            is String -> parseRuleString(raw)
            is JSONObject -> parseRuleObject(raw)
            else -> null
        }
    }

    private fun parseRuleString(raw: String): ValidationRule? {
        return when {
            raw.equals("required", true) -> RequiredRule()
            raw.equals("email", true) -> EmailRule()
            raw.equals("phone", true) -> PhoneRule()
            raw.equals("password", true) -> PasswordRule()
            raw.startsWith("minLength", true) -> {
                val value = raw.extractNumber()
                if (value != null) MinLengthRule(value) else null
            }
            raw.startsWith("maxLength", true) -> {
                val value = raw.extractNumber()
                if (value != null) MaxLengthRule(value) else null
            }
            raw.startsWith("regex", true) -> {
                val pattern = raw.extractBetween("(", ")")
                if (pattern != null) RegexRule(pattern.toRegex()) else null
            }
            raw.startsWith("matches", true) -> {
                val key = raw.extractBetween("(", ")")
                if (key != null) MatchesFieldRule(key) else null
            }
            else -> null
        }
    }

    private fun parseRuleObject(obj: JSONObject): ValidationRule? {
        val name = obj.optString("name", obj.optString("type", "")).lowercase()
        val message = obj.optString("message", "")
        return when (name) {
            "required" -> RequiredRule(message.ifBlank { "Field is required" })
            "email" -> EmailRule(message.ifBlank { "Invalid email address" })
            "phone" -> PhoneRule(message.ifBlank { "Invalid phone number" })
            "password" -> PasswordRule(message.ifBlank { "Password must be at least 8 characters and include a number" })
            "minlength" -> MinLengthRule(obj.optInt("value", 0), message.ifBlank { "Minimum length is ${obj.optInt("value", 0)}" })
            "maxlength" -> MaxLengthRule(obj.optInt("value", 0), message.ifBlank { "Maximum length is ${obj.optInt("value", 0)}" })
            "regex" -> {
                val pattern = obj.optString("pattern", "")
                if (pattern.isBlank()) null else RegexRule(pattern.toRegex(), message.ifBlank { "Invalid format" })
            }
            "matches" -> {
                val fieldKey = obj.optString("fieldKey", "")
                if (fieldKey.isBlank()) null else MatchesFieldRule(fieldKey, message.ifBlank { "Fields do not match" })
            }
            else -> null
        }
    }

    private fun String.extractNumber(): Int? {
        val value = extractBetween("(", ")")?.toIntOrNull()
        return value
    }

    private fun String.extractBetween(start: String, end: String): String? {
        val startIndex = indexOf(start)
        val endIndex = lastIndexOf(end)
        if (startIndex == -1 || endIndex == -1 || endIndex <= startIndex) return null
        return substring(startIndex + 1, endIndex).trim().ifBlank { null }
    }
}
