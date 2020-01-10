package ru.skillbranch.devintensive

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.getRootView
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.extensions.isKeyboardOpen
import ru.skillbranch.devintensive.models.Bender

class MainActivity : AppCompatActivity(), View.OnClickListener {
//    var benderImage: ImageView? = null
    private lateinit var benderImage: ImageView
    private lateinit var textTxt: TextView
    private lateinit var messageEt: EditText
    private lateinit var sendBtn: ImageView
    private lateinit var benderObj: Bender
    private var freshIdle: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        benderImage = findViewById(R.id.iv_bender)
        benderImage = iv_bender
        textTxt = tv_text
        messageEt = et_message
        sendBtn = iv_send

        val status = savedInstanceState?.getString("STATUS")
                                    ?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString("QUESTION")
                                    ?: Bender.Question.NAME.name
        freshIdle = savedInstanceState?.getBoolean("FRESH_IDLE") ?: true

        benderObj = Bender(Bender.Status.valueOf(status),
                            Bender.Question.valueOf(question))

        Log.d("M_MainActivity", "onCreate $status $question")
        val (r, g, b) = benderObj.status.color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)

//        textTxt.setText(benderObj.askQuestion())
        textTxt.text = benderObj.askQuestion()
        sendBtn.setOnClickListener(this)
        btn_test_keyboard.setOnClickListener(this)
        messageEt.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when(actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    sendMessage(messageEt.text.toString())
                    true
                } else -> false
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("M_MainActivity", "onRestart")
    }

    override fun onStart() {
        super.onStart()
        Log.d("M_MainActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("M_MainActivity", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("M_MainActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("M_MainActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("M_MainActivity", "onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("STATUS", benderObj.status.name)
        outState.putString("QUESTION", benderObj.question.name)
        outState.putBoolean("FRESH_IDLE", freshIdle)
        Log.d("M_MainActivity", "onSaveInstanceState " +
                            "${benderObj.status.name} ${benderObj.question.name}")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_send -> sendMessage(messageEt.text.toString())
            R.id.btn_test_keyboard -> Toast.makeText(
                this, "Keyboard is open -> " +
                        "${isKeyboardOpen()}", Toast.LENGTH_LONG).show()
        }
    }

    private fun sendMessage(msg: String) {
        hideKeyboard()
        if (msg.trim().isEmpty()) return
        if (benderObj.question == Bender.Question.IDLE && freshIdle){
            freshIdle = false
            // Убираем фразу "Отлично - ты справился"
            textTxt.text = Bender.Question.IDLE.question
            return
        }
        val (phrase, color) = benderObj.listenAnswer(msg.trim())
        messageEt.setText("")
        val (r, g, b) = color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)
        textTxt.text = phrase
    }

}
