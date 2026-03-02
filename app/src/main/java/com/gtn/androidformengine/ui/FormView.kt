package com.gtn.androidformengine.ui

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.gtn.androidformengine.R
import com.gtn.androidformengine.core.Field
import com.gtn.androidformengine.core.Form
import com.gtn.androidformengine.core.FormResult

class FormView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    data class FormViewState(
        val currentStepIndex: Int,
        val values: Map<String, String>
    )

    private var form: Form? = null
    private var onSubmit: ((FormResult) -> Unit)? = null
    private var currentStepIndex = 0
    private val fieldViews = mutableMapOf<String, View>()
    private val values = mutableMapOf<String, String>()

    init {
        orientation = VERTICAL
    }

    fun setForm(form: Form, onSubmit: (FormResult) -> Unit) {
        this.form = form
        this.onSubmit = onSubmit
        currentStepIndex = 0
        renderStep()
    }

    fun saveState(): FormViewState {
        return FormViewState(
            currentStepIndex = currentStepIndex,
            values = values.toMap()
        )
    }

    fun restoreState(state: FormViewState) {
        currentStepIndex = state.currentStepIndex
        values.clear()
        values.putAll(state.values)
        renderStep()
    }

    fun saveState(outState: Bundle, key: String = "form_view_state") {
        val bundle = Bundle()
        bundle.putInt("currentStepIndex", currentStepIndex)
        bundle.putSerializable("values", HashMap(values))
        outState.putBundle(key, bundle)
    }

    fun restoreState(savedInstanceState: Bundle, key: String = "form_view_state") {
        val bundle = savedInstanceState.getBundle(key) ?: return
        currentStepIndex = bundle.getInt("currentStepIndex", 0)
        val restored = bundle.getSerializable("values") as? HashMap<*, *>
        values.clear()
        restored?.forEach { (k, v) ->
            if (k is String && v is String) {
                values[k] = v
            }
        }
        renderStep()
    }

    private fun renderStep() {
        removeAllViews()
        FieldRendererRegistry.ensureDefaults()
        val form = form ?: return
        val steps = form.steps
        if (steps.isEmpty()) return

        val step = steps[currentStepIndex]
        if (!step.title.isNullOrBlank()) {
            val titleView = TextView(context).apply {
                text = step.title
                textSize = 18f
            }
            addView(titleView)
        }

        val fieldsContainer = LinearLayout(context).apply {
            orientation = VERTICAL
        }
        addView(fieldsContainer)

        fieldViews.clear()
        for (field in step.fields) {
            val renderer = FieldRendererRegistry.rendererFor(field.type) ?: continue
            val view = renderer.createView(
                context = context,
                field = field,
                initialValue = values[field.key]
            ) { newValue ->
                values[field.key] = newValue
            }
            fieldsContainer.addView(view)
            fieldViews[field.key] = view
        }

        addView(buildButtonRow(step, steps.size))
    }

    private fun buildButtonRow(step: com.gtn.androidformengine.core.FormStep, totalSteps: Int): View {
        val row = LinearLayout(context).apply {
            orientation = HORIZONTAL
            gravity = Gravity.END
        }

        val buttonSpacing = context.resources.getDimensionPixelSize(R.dimen.button_spacing)

        if (totalSteps > 1) {
            val backButton = MaterialButton(context).apply {
                text = "Back"
                isEnabled = currentStepIndex > 0
                setOnClickListener {
                    if (currentStepIndex > 0) {
                        currentStepIndex--
                        renderStep()
                    }
                }
            }
            row.addView(backButton)
        }

        val nextLabel = if (currentStepIndex == totalSteps - 1) "Submit" else "Next"
        val nextButton = MaterialButton(context).apply {
            text = nextLabel
            setOnClickListener {
                if (validateStep(step)) {
                    if (currentStepIndex == totalSteps - 1) {
                        submitForm()
                    } else {
                        currentStepIndex++
                        renderStep()
                    }
                }
            }
        }
        val nextParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            marginStart = buttonSpacing
        }
        row.addView(nextButton, nextParams)

        return row
    }

    private fun collectAllValues(): Map<String, String?> {
        val collected = mutableMapOf<String, String?>()
        collected.putAll(values)
        fieldViews.forEach { (key, view) ->
            val field = findFieldByKey(key) ?: return@forEach
            val renderer = FieldRendererRegistry.rendererFor(field.type) ?: return@forEach
            collected[key] = renderer.getValue(view)
        }
        return collected
    }

    private fun findFieldByKey(key: String): Field? {
        val form = form ?: return null
        return form.steps.flatMap { it.fields }.firstOrNull { it.key == key }
    }

    private fun validateStep(step: com.gtn.androidformengine.core.FormStep): Boolean {
        val form = form ?: return false
        val result = form.validate(collectAllValues())
        var isStepValid = true

        for (field in step.fields) {
            val error = result.errors[field.key]
            val view = fieldViews[field.key]
            val renderer = FieldRendererRegistry.rendererFor(field.type)
            if (view != null && renderer != null) {
                renderer.setError(view, error)
            }
            if (error != null) {
                isStepValid = false
            }
        }

        return isStepValid
    }

    private fun submitForm() {
        val form = form ?: return
        val result = form.validate(collectAllValues())
        onSubmit?.invoke(result)
    }
}
