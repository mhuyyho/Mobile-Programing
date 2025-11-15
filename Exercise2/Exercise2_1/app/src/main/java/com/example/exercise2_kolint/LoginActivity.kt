package com.example.exercise2_kolint

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.exercise2_kolint.R

class LoginActivity : AppCompatActivity() {

    companion object {
        // mode constants
        private const val MODE_TEXT = 0
        private const val MODE_EMAIL = 1
        private const val MODE_PASSWORD = 2
        private const val MODE_NUMBER = 3
        private const val MODE_PHONE = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val root = findViewById<View>(R.id.login)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val systemBars: Insets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // find the included root views by the id you set on <include>
        val emailInclude = findViewById<View?>(R.id.include_email)
        val passwordInclude = findViewById<View?>(R.id.include_password)

        // configure each include (icon, hint, input type)
        configureLinearInput(emailInclude, R.drawable.email, "Email", MODE_EMAIL)
        configureLinearInput(passwordInclude, R.drawable.password, "Password", MODE_PASSWORD)

        val facebookInclude = findViewById<View?>(R.id.include_socialFB)
        val googleInclude = findViewById<View?>(R.id.include_socialGG)

        configureViewSocials(facebookInclude, R.drawable.facebook, "Facebook")
        configureViewSocials(googleInclude, R.drawable.google, "Google")

        val registerTV = findViewById<TextView?>(R.id.tv_register)
        registerTV?.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }



    private fun configureViewSocials(includeRoot: View?, iconRes: Int, text: String?) {
        if (includeRoot == null) return

        val tv = includeRoot.findViewById<TextView?>(R.id.tvLabel)
        val iv = includeRoot.findViewById<ImageView?>(R.id.ivSocial)

        if (iv != null && iconRes != 0) {
            iv.setImageResource(iconRes)
        }
        if (tv != null && text != null) {
            tv.text = text
        }
    }

    private fun configureLinearInput(
        includeRoot: View?,
        iconRes: Int,
        hint: String?,
        mode: Int
    ) {
        if (includeRoot == null) return

        val iv = includeRoot.findViewById<ImageView?>(R.id.iv_icon)
        val et = includeRoot.findViewById<EditText?>(R.id.et_input)
        val divider = includeRoot.findViewById<View?>(R.id.input_divider)

        if (iv != null && iconRes != 0) {
            iv.setImageResource(iconRes)
        }
        if (et != null) {
            et.hint = hint ?: ""
            applyModeToEditText(et, mode)
        }
        // Optional: show/hide divider
        divider?.visibility = View.VISIBLE
    }

    private fun applyModeToEditText(et: EditText, mode: Int) {
        when (mode) {
            MODE_EMAIL -> {
                et.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                et.transformationMethod = null
            }

            MODE_PASSWORD -> {
                // both mask the input and set proper inputType
                et.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                et.transformationMethod = PasswordTransformationMethod.getInstance()
            }

            MODE_NUMBER -> {
                et.inputType = InputType.TYPE_CLASS_NUMBER
                et.transformationMethod = null
            }

            MODE_PHONE -> {
                et.inputType = InputType.TYPE_CLASS_PHONE
                et.transformationMethod = null
            }

            MODE_TEXT -> {
                et.inputType = InputType.TYPE_CLASS_TEXT
                et.transformationMethod = null
            }

            else -> {
                et.inputType = InputType.TYPE_CLASS_TEXT
                et.transformationMethod = null
            }
        }
    }
}