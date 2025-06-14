package com.example.pert7no1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.pert7no1.DB.KoneksiDB

class PostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_post)

        val sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "Guest")

        val btnPost = findViewById<Button>(R.id.btnPost)
        val etPost = findViewById<EditText>(R.id.etPostContent)

        btnPost.setOnClickListener {
            val content = etPost.text.toString().trim()

            if (content.isEmpty()) {
                Toast.makeText(this, "Isi postingan tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            GlobalScope.launch(Dispatchers.IO) {
                val connection = KoneksiDB.connection()
                if (connection != null) {
                    val statement = connection.prepareStatement(
                        """INSERT INTO postingan (username, isi) VALUES (?, ?)"""
                    )
                    statement.setString(1, username)
                    statement.setString(2, content)
                    statement.executeUpdate()
                    statement.close()
                    connection.close()

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@PostActivity, "Postingan berhasil!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@PostActivity, dashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}