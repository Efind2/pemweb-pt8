package com.example.pert7no1.CustomView

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.pert7no1.R
import com.example.pert7no1.login_activity
import com.example.pert7no1.regisActivity

class PertamaView (context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs){
    private val iconX = ImageView(context).apply {
        setImageResource(R.drawable.ic_x)
        layoutParams = LayoutParams(100, 100).apply {
            setMargins(0, 0,0, 0)
            orientation = VERTICAL
            gravity = Gravity.TOP
            gravity = Gravity.CENTER_HORIZONTAL
        }
    }
    private val textUtama = TextView(context).apply {
        text = "Ketahui apa yang terjadi di dunia saat ini."
        gravity = Gravity.START
        setTypeface(null, Typeface.BOLD)
        textSize = 27f
        setPadding(80, 450, 80, 0)
        setTextColor(resources.getColor(R.color.white, null))
    }
    private val googleButton = Button(context).apply {
        text = "Lanjutkan dengan Google"
        isAllCaps = false
        gravity = Gravity.CENTER
        setPadding(20, 10, 20, 10)

        val drawable = ContextCompat.getDrawable(context, R.drawable.google_color_svgrepo_com)
        drawable?.setBounds(0, 0, 50, 50)

        val spannable = SpannableString("  Lanjutkan dengan Google")
        val imageSpan = ImageSpan(drawable!!, ImageSpan.ALIGN_BASELINE)
        spannable.setSpan(imageSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

        text = spannable
        background = createRoundedBackground()
    }
    private val textAtau = TextView(context).apply {
        textSize = 12f
        gravity = Gravity.CENTER
        setTextColor(ContextCompat.getColor(context, R.color.grey_700))

        val text = " atau "
        val spannable = SpannableString("────────────────── $text ──────────────────")
        spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.grey_700)), 0, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        setText(spannable)
    }
    private val createButton = Button(context).apply {
        text = "Buat akun"
        isAllCaps = false
        gravity = Gravity.CENTER
        setPadding(20, 10, 20, 10)
        background = createRoundedBackground()

        setOnClickListener{
            val intent = Intent(context, regisActivity::class.java)
            context.startActivity(intent)
        }
    }
    private val textPanjang = TextView(context).apply {
        textSize = 14f
        gravity = Gravity.START
        setPadding(80, 70, 80, 0)
        setTextColor(ContextCompat.getColor(context, R.color.grey_700))

        val fullText = "Dengan mendaftar, Anda menyetujui Persyaratan, Kebijakan Privasi, dan Ketentuan Kuki"
        val spannable = SpannableString(fullText)
        val highlightColor = ContextCompat.getColor(context, R.color.twitter_blue)

        spannable.setSpan(ForegroundColorSpan(highlightColor), fullText.indexOf("Persyaratan"), fullText.indexOf("Persyaratan") + "Persyaratan".length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(highlightColor), fullText.indexOf("Kebijakan Privasi"), fullText.indexOf("Kebijakan Privasi") + "Kebijakan Privasi".length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(highlightColor), fullText.indexOf("Ketentuan Kuki"), fullText.indexOf("Ketentuan Kuki") + "Ketentuan Kuki".length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text = spannable
    }
    private val textMasuk = TextView(context).apply {
        textSize = 16f
        gravity = Gravity.START
        setPadding(80, 100, 80, 0)
        setTextColor(ContextCompat.getColor(context, R.color.grey_700))
        movementMethod = LinkMovementMethod.getInstance()

        val fullText = "Sudah punya akun? Masuk"
        val spannable = SpannableString(fullText)
        val highlightColor = ContextCompat.getColor(context, R.color.twitter_blue)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(context, login_activity::class.java)
                context.startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = highlightColor
                ds.isUnderlineText = false
            }
        }

        spannable.setSpan(clickableSpan, fullText.indexOf("Masuk"), fullText.indexOf("Masuk") + "Masuk".length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text = spannable
    }

    init {
        setBackgroundColor(resources.getColor(R.color.black, null))

        addView(iconX)
        addView(textUtama, createLayoutParams())
        addView(googleButton, createLayoutParams().apply {
            setMargins(80, 400, 80, 0)
        })
        addView(textAtau, createLayoutParams())
        addView(createButton, createLayoutParams().apply {
            setMargins(80, 0, 80, 0)
        })
        addView(textPanjang, createLayoutParams())
        addView(textMasuk, createLayoutParams())
    }

    private fun createLayoutParams(): LayoutParams {
        return LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            setMargins(20, 20, 20, 20)
        }
    }
    private fun createRoundedBackground(): GradientDrawable {
        return GradientDrawable().apply {
            setColor(Color.WHITE)
            setStroke(2, Color.GRAY)
            cornerRadius = 100f
        }
    }
}