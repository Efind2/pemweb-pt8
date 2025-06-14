package com.example.pert7no1.CustomView

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Space
import android.widget.TextView
import android.widget.Toast
import com.example.pert7no1.R
import com.example.pert7no1.DB.KoneksiDB
import com.example.pert7no1.PostActivity
import com.example.pert7no1.ReplyActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class DashboardView (context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private val container = LinearLayout(context).apply {
        orientation = HORIZONTAL
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        gravity = Gravity.CENTER_VERTICAL
    }

    private val iconProfile = ImageView(context).apply {
        setImageResource(R.drawable.profile_circle_svgrepo_com)
        layoutParams = LayoutParams(100, 100)
    }

    private val spacer1 = Space(context).apply {
        layoutParams = LayoutParams(0, 0, 1f)
    }

    private val iconX = ImageView(context).apply {
        setImageResource(R.drawable.ic_x)
        layoutParams = LayoutParams(100, 100)
    }

    private val spacer2 = Space(context).apply {
        layoutParams = LayoutParams(0, 0, 1f)
    }

    private val iconElip = ImageView(context).apply {
        setImageResource(R.drawable.elipsis_v_svgrepo_com)
        layoutParams = LayoutParams(100, 100)
    }
    private val tabContainer = LinearLayout(context).apply {
        orientation = HORIZONTAL
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        gravity = Gravity.CENTER
    }
    private val textUntuk = TextView(context).apply {
        text = "Untuk Anda"
        setTypeface(null, Typeface.BOLD)
        textSize = 18f
        setPadding(30, 30, 50, 30)
        setTextColor(resources.getColor(R.color.white, null))

        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            rightMargin = 200
        }
    }

    private val textMengikuti = TextView(context).apply {
        text = "Mengikuti"
        setTypeface(null, Typeface.NORMAL)
        textSize = 18f
        setPadding(30, 30, 30, 30)
        setTextColor(resources.getColor(R.color.white, null))
    }

    val garis = View(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            2 // tinggi garis
        ).apply {
            topMargin = 16
            bottomMargin = 16
        }
        setBackgroundColor(resources.getColor(R.color.white, null))
    }

    fun getPostinganDanReplyFromJson(context: Context): List<Postingan> {
        val inputStream = context.assets.open("dataDummy.json")
        val reader = InputStreamReader(inputStream)
        val type = object : TypeToken<List<Postingan>>() {}.type
        return Gson().fromJson(reader, type)
    }

    init {
        orientation = VERTICAL
        setBackgroundColor(resources.getColor(R.color.black, null))

        val rootLayout = FrameLayout(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }

        val scrollView = ScrollView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }

        val contentLayout = LinearLayout(context).apply {
            orientation = VERTICAL
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        }

        // ✨ Tombol post
        val postButton = ImageView(context).apply {
            setImageResource(R.drawable.icon_tambah)
            layoutParams = FrameLayout.LayoutParams(150, 150).apply {
                gravity = Gravity.BOTTOM or Gravity.END
                bottomMargin = 60
                rightMargin = 40
            }
            setPadding(30, 30, 30, 30)
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(Color.parseColor("#1DA1F2"))
            }
            elevation = 10f
            isClickable = true
            isFocusable = true

            setOnClickListener {
                Toast.makeText(context, "Tombol Post ditekan", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, PostActivity::class.java)
                context.startActivity(intent)
            }
        }

        container.addView(iconProfile)
        container.addView(spacer1)
        container.addView(iconX)
        container.addView(spacer2)
        container.addView(iconElip)

        tabContainer.addView(textUntuk)
        tabContainer.addView(textMengikuti)
        addView(container, createLayoutParams())
        addView(tabContainer)
        addView(garis)

        scrollView.addView(contentLayout)
        rootLayout.addView(scrollView)
        rootLayout.addView(postButton)
        addView(rootLayout)

        // Load postingan & reply
        Thread {
            try {
                val data = getPostinganDanReply()
                (context as? android.app.Activity)?.runOnUiThread {
                    for (post in data) {
                        val postContainer = LinearLayout(context).apply {
                            orientation = VERTICAL
                            setPadding(30, 30, 30, 30)
                            setBackgroundColor(Color.DKGRAY)
                            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                                setMargins(30, 30, 30, 30)
                            }
                        }

                        val usernameText = TextView(context).apply {
                            text = "@${post.username}"
                            setTextColor(Color.WHITE)
                            setTypeface(null, Typeface.BOLD)
                        }

                        val isiText = TextView(context).apply {
                            text = post.isi
                            setTextColor(Color.WHITE)
                            setPadding(0, 10, 0, 10)
                        }

                        val tanggalText = TextView(context).apply {
                            text = post.tanggal
                            setTextColor(Color.GRAY)
                            textSize = 12f
                        }

                        val replyIcon = ImageView(context).apply {
                            setImageResource(R.drawable.reply_icon)
                            setPadding(0, 15, 0, 10)
                            layoutParams = LayoutParams(60, 60).apply {
                                gravity = Gravity.START
                            }

                            setOnClickListener {
                                val intent = Intent(context, ReplyActivity::class.java)
                                intent.putExtra("id_postingan", post.id)
                                context.startActivity(intent)
                            }
                        }

                        postContainer.addView(usernameText)
                        postContainer.addView(isiText)
                        postContainer.addView(tanggalText)
                        postContainer.addView(replyIcon)

                        for (reply in post.replies) {
                            val replyText = TextView(context).apply {
                                text = "↳ ${reply.username}: ${reply.isi}"
                                setTextColor(Color.LTGRAY)
                                setPadding(40, 10, 10, 10)
                                textSize = 14f
                            }
                            postContainer.addView(replyText)
                        }

                        contentLayout.addView(postContainer)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    fun getPostinganDanReply(): List<Postingan> {
        val listPostingan = mutableListOf<Postingan>()
        val koneksi = KoneksiDB.connection() ?: return emptyList()

        val sqlPostingan = "SELECT * FROM postingan ORDER BY tanggal_post DESC"
        val stmt = koneksi.createStatement()
        val rs = stmt.executeQuery(sqlPostingan)

        while (rs.next()) {
            val idPostingan = rs.getInt("id")
            val username = rs.getString("username")
            val isi = rs.getString("isi")
            val tanggal = rs.getTimestamp("tanggal_post").toString()

            val listReply = mutableListOf<Reply>()
            val rsReply = koneksi.prepareStatement("SELECT * FROM reply WHERE id_postingan = ? ORDER BY tanggal_reply ASC").apply {
                setInt(1, idPostingan)
            }.executeQuery()

            while (rsReply.next()) {
                listReply.add(
                    Reply(
                        username = rsReply.getString("username"),
                        isi = rsReply.getString("isi"),
                        tanggal = rsReply.getTimestamp("tanggal_reply").toString()
                    )
                )
            }

            listPostingan.add(
                Postingan(
                    id = idPostingan,
                    username = username,
                    isi = isi,
                    tanggal = tanggal,
                    replies = listReply
                )
            )
        }

        rs.close()
        stmt.close()
        koneksi.close()

        return listPostingan
    }

    private fun createLayoutParams(): LayoutParams {
        return LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            setMargins(20, 20, 20, 50)
        }
    }

    data class Reply(
        val username: String,
        val isi: String,
        val tanggal: String
    )

    data class Postingan(
        val id: Int,
        val username: String,
        val isi: String,
        val tanggal: String,
        val replies: List<Reply>
    )
}