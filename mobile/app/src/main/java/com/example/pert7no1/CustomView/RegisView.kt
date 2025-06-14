package com.example.pert7no1.CustomView

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.text.InputType
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.pert7no1.DB.KoneksiDB
import com.example.pert7no1.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisView (context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs){
    private val iconX = ImageView(context).apply {
        setImageResource(R.drawable.ic_x)
        layoutParams = LinearLayout.LayoutParams(100, 100).apply {
            gravity = Gravity.CENTER_HORIZONTAL
        }
    }
    private val textUtama = TextView(context).apply {
        text = "Buat akun anda"
        gravity = Gravity.START
        setTypeface(null, Typeface.BOLD)
        textSize = 23f
        setPadding(30, 80, 30, 0)
        setTextColor(resources.getColor(R.color.white, null))
    }
    fun setUsername(username: String) {
        usernameField.setText(username)
        usernameField.isEnabled = false
        usernameField.isFocusable = false
    }
    private val usernameField = EditText(context).apply {
        hint = "Username"
        setHintTextColor(Color.LTGRAY)
        setTextColor(Color.WHITE)
        setHighlightColor(Color.WHITE)
        setPadding(40, 40, 40, 40)
        background = createRoundedBackground()
        setSingleLine(true)
        ellipsize = TextUtils.TruncateAt.END
    }

    private val passwordField = EditText(context).apply {
        hint = "Password"
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        setHintTextColor(Color.LTGRAY)
        setTextColor(Color.WHITE)
        setHighlightColor(Color.WHITE)
        setPadding(40, 40, 40, 40)
        background = createRoundedBackground()
    }
    private val bottomContainer = LinearLayout(context).apply {
        orientation = VERTICAL
        gravity = Gravity.BOTTOM
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }
    private val buttonContainer = LinearLayout(context).apply {
        orientation = HORIZONTAL
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            setMargins(50, 1100, 50, 50)
        }
    }
    private val spacer = View(context).apply {
        layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f)
    }
    private val nextButton = Button(context).apply {
        text = "Buat"
        isAllCaps = false
        gravity = Gravity.CENTER
        setPadding(20, 10, 20, 10)
        background = createRoundedBackgroundButton()
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            weight = 0f
        }

        setOnClickListener {
            val user = usernameField.text.toString().trim()
            val pw = passwordField.text.toString().trim()

            if (user.isEmpty()) {
                showPopupDialog(false, "Username tidak boleh kosong")
                return@setOnClickListener
            }

            if (pw.isEmpty()) {
                showPopupDialog(false, "Password tidak boleh kosong")
                return@setOnClickListener
            }

            isUsernameTaken(user) { isExists ->
                if (isExists) {
                    showPopupDialog(false, "Username sudah digunakan")
                } else {
                    insertUser(user, pw) { isSuccess ->
                        showPopupDialog(isSuccess, if (isSuccess) "Berhasil membuat akun" else "Gagal membuat akun")
                    }
                }
            }
        }
    }

    //pop up notif
    fun showPopupDialog(success: Boolean, message: String) {
        if (success) {
            Toast.makeText(context, "✅ $message", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "❌ $message", Toast.LENGTH_SHORT).show()
        }
    }


    init{
        orientation = VERTICAL
        setBackgroundColor(resources.getColor(R.color.black, null))

        addView(iconX)
        addView(textUtama, createLayoutParams())
        addView(usernameField, createLayoutParams().apply {
            setPadding(50, 0, 50, 0)
        })
        addView(passwordField, createLayoutParams().apply {
            setPadding(50, 0, 50, 0)
        })

        buttonContainer.addView(spacer)
        buttonContainer.addView(nextButton)
        bottomContainer.addView(buttonContainer)
        addView(bottomContainer)
    }

    private fun createLayoutParams(): LayoutParams {
        return LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            setMargins(20, 20, 20, 20)
        }
    }
    private fun createRoundedBackground(): GradientDrawable {
        return GradientDrawable().apply {
            setColor(Color.BLACK)
            setStroke(2, Color.GRAY)
            cornerRadius = 10f
        }
    }
    private fun createRoundedBackgroundButton(): GradientDrawable {
        return GradientDrawable().apply {
            setColor(Color.WHITE)
            setStroke(2, Color.GRAY)
            cornerRadius = 100f
        }
    }
    private fun createRoundedBackgroundButtonLupa(): GradientDrawable {
        return GradientDrawable().apply {
            setColor(Color.BLACK)
            setStroke(2, Color.WHITE)
            cornerRadius = 100f
        }
    }

    fun isUsernameTaken(username: String, onResult: (Boolean) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val connection = KoneksiDB.connection()
                if (connection != null) {
                    val statement = connection.prepareStatement("""SELECT * FROM "user" WHERE username = ?""")
                    statement.setString(1, username)
                    val resultSet = statement.executeQuery()

                    val exists = resultSet.next()

                    withContext(Dispatchers.Main) {
                        onResult(exists)
                    }

                    resultSet.close()
                    statement.close()
                    connection.close()
                } else {
                    withContext(Dispatchers.Main) {
                        onResult(false)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onResult(false)
                }
            }
        }
    }

    //method select
    fun insertUser(username: String, password: String, onResult: (Boolean) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val connection = KoneksiDB.connection()
                if (connection != null) {
                    val statement = connection.prepareStatement(
                        """INSERT INTO "user" (username, password) VALUES (?, ?)"""
                    )
                    statement.setString(1, username)
                    statement.setString(2, password)

                    val rowsInserted = statement.executeUpdate()

                    withContext(Dispatchers.Main) {
                        onResult(rowsInserted > 0)
                    }

                    statement.close()
                    connection.close()
                } else {
                    withContext(Dispatchers.Main) {
                        onResult(false)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onResult(false)
                }
            }
        }
    }
}