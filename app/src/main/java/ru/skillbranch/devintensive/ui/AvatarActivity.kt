package ru.skillbranch.devintensive.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_avatar.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.ui.custom.AvatarImageView

private const val viewId = 10

class AvatarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar)

/*        val view = AvatarImageView(this).apply {
            id = viewId
            layoutParams = LinearLayout.LayoutParams(320, 320)
            setImageResource(R.drawable.avatarka)
        }
        container.addView(view)*/

        btn_color.setOnClickListener {
            aiv.setBorderColor((AvatarImageView.bgColors).random())
        }

        btn_width.setOnClickListener {
            aiv.setBorderWidth((2..10).random())
        }
    }

}
