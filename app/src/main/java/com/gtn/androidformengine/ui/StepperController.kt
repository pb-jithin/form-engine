package com.gtn.androidformengine.ui

import com.gtn.androidformengine.core.Form
import com.gtn.androidformengine.core.FormStep

class StepperController(
    private val form: Form
) {
    var currentStepIndex: Int = 0
        private set

    val hasNext: Boolean
        get() = currentStepIndex < form.steps.size - 1

    val hasPrevious: Boolean
        get() = currentStepIndex > 0

    fun currentStep(): FormStep = form.steps[currentStepIndex]

    fun next(): FormStep {
        if (hasNext) currentStepIndex++
        return currentStep()
    }

    fun previous(): FormStep {
        if (hasPrevious) currentStepIndex--
        return currentStep()
    }
}
