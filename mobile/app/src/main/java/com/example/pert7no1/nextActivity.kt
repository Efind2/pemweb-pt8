package com.example.pert7no1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class nextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_next)

        val nextView = findViewById<com.example.pert7no1.CustomView.NextView>(R.id.next1)
        val username = intent.getStringExtra("username")
        if (username != null) {
            nextView.setUsername(username)
        }

        val sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val username1 = sharedPref.getString("username", "Guest")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.next)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}