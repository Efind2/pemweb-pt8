package com.example.pert7no1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
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

class ReplyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reply)

        val idPostingan = intent.getIntExtra("id_postingan", -1)
        val sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "Guest")

        val btnPost = findViewById<Button>(R.id.btnPost1)
        val etPost = findViewById<EditText>(R.id.etPostContent1)
        val btnClose = findViewById<ImageButton>(R.id.btnClose1)

        btnClose.setOnClickListener {
            finish()
        }

        btnPost.setOnClickListener {
            val replyText = etPost.text.toString().trim()

            if (replyText.isEmpty()) {
                Toast.makeText(this, "Balasan tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            GlobalScope.launch(Dispatchers.IO) {
                val connection = KoneksiDB.connection()
                if (connection != null) {
                    val stmt = connection.prepareStatement("""
                        INSERT INTO reply (id_postingan, username, isi)
                        VALUES (?, ?, ?)
                    """.trimIndent())
                    stmt.setInt(1, idPostingan)
                    stmt.setString(2, username)
                    stmt.setString(3, replyText)
                    stmt.executeUpdate()
                    stmt.close()
                    connection.close()

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ReplyActivity, "Reply berhasil dikirim", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@ReplyActivity, dashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ReplyActivity, "Koneksi gagal", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main1)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}