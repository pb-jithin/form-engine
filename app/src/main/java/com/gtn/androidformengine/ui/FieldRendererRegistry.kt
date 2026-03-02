package com.gtn.androidformengine.ui

import com.gtn.androidformengine.core.FieldType

object FieldRendererRegistry {
    private val renderers = mutableMapOf<FieldType, FieldRenderer>()

    fun register(type: FieldType, renderer: FieldRenderer) {
        renderers[type] = renderer
    }

    fun rendererFor(type: FieldType): FieldRenderer? = renderers[type]

    internal fun ensureDefaults() {
        if (renderers.isEmpty()) {
            val defaultRenderer = MaterialInputRenderer()
            register(FieldType.INPUT, defaultRenderer)
            register(FieldType.PASSWORD, defaultRenderer)
        }
    }
}
