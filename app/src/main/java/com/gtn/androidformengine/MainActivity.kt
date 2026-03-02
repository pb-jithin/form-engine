package com.gtn.androidformengine

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gtn.androidformengine.dsl.multiStepForm
import com.gtn.androidformengine.ui.FormView

class MainActivity : AppCompatActivity() {
    private lateinit var formView: FormView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        formView = findViewById(R.id.formView)
        val form = multiStepForm {
            step("Personal Info") {
                input("name") { required() }
                input("email") { email(); required() }
                input("phone") { phone() }
            }
            step("Security") {
                password("password") { minLength(8); password() }
                password("confirm_password") { matches("password") }
            }
        }

        formView.setForm(form) { result ->
            if (result.isValid) {
                Toast.makeText(this, "Form submitted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please fix errors", Toast.LENGTH_SHORT).show()
            }
        }

        if (savedInstanceState != null) {
            formView.restoreState(savedInstanceState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        formView.saveState(outState)
        super.onSaveInstanceState(outState)
    }
}
