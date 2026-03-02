package com.gtn.androidformengine.ui

import android.content.Context
import android.view.View
import com.gtn.androidformengine.core.Field

interface FieldRenderer {
    fun createView(
        context: Context,
        field: Field,
        initialValue: String?,
        onValueChanged: (String) -> Unit
    ): View

    fun getValue(view: View): String?

    fun setError(view: View, message: String?)
}
