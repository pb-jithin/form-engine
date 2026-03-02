package com.gtn.androidformengine.ui

import android.content.Context
import android.text.InputType
import android.view.View
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.gtn.androidformengine.R
import com.gtn.androidformengine.core.Field
import com.gtn.androidformengine.core.FieldType

class MaterialInputRenderer : FieldRenderer {
    override fun createView(
        context: Context,
        field: Field,
        initialValue: String?,
        onValueChanged: (String) -> Unit
    ): View {
        val layout = TextInputLayout(context).apply {
            isErrorEnabled = true
            hint = field.label ?: field.key
            setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = context.resources.getDimensionPixelSize(R.dimen.field_spacing)
            }
        }

        val editText = TextInputEditText(context).apply {
            setText(initialValue ?: "")
            maxLines = 1
            setSingleLine(true)
            inputType = when (field.type) {
                FieldType.PASSWORD -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                FieldType.INPUT -> InputType.TYPE_CLASS_TEXT
            }
            addTextChangedListener { editable ->
                onValueChanged(editable?.toString().orEmpty())
            }
        }
        layout.addView(editText)
        layout.tag = editText
        return layout
    }

    override fun getValue(view: View): String? {
        val editText = view.tag as? TextInputEditText
        return editText?.text?.toString()
    }

    override fun setError(view: View, message: String?) {
        val layout = view as? TextInputLayout
        layout?.error = message
    }
}
