package ru.skillbranch.devintensive.ui.profile

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_profile.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.ui.custom.DefaultAvatar
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel

class ProfileActivity : AppCompatActivity() {

    companion object {
        const val IS_EDIT_MODE = "IS_EDIT_MODE"
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var viewFields: Map<String, TextView>
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("M_ProfileActivity", "onCreate()")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViews(savedInstanceState)
        initViewModel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("M_ProfileActivity", "onSaveInstanceState()")
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_EDIT_MODE, isEditMode)
    }

    private fun initViewModel() {
        Log.d("M_ProfileActivity", "initViewModel()")
        viewModel = ViewModelProviders.of(this)
            .get(ProfileViewModel::class.java)
        viewModel.getProfileData().observe(this, Observer {
            Log.d("M_ProfileActivity", "Profile observer reacts")
            updateUI(it)
        })
        viewModel.getTheme().observe(this, Observer {
            Log.d("M_ProfileActivity", "Theme observer reacts")
            updateTheme(it)
        })
    }

    private fun updateTheme(mode: Int) {
        Log.d("M_ProfileActivity", "updateTheme()")
        delegate.localNightMode = mode
    }

    private fun updateUI(profile: Profile) {
        Log.d("M_ProfileActivity", "updateUI()")
        profile.toMap().also {
            for ((k, v) in viewFields) v.text = it[k].toString()
        }
        // Цепляем дефолтную картинку для данного профиля
        val drawable = DefaultAvatar(this).apply {
            assignInitials(
                Utils.toInitials(profile.firstName, profile.lastName) ?: "??"
            )
        }
        iv_avatar.setImageDrawable(drawable)
    }

    private fun initViews(savedInstanceState: Bundle?) {
        Log.d("M_ProfileActivity", "initViews()")
        viewFields = mapOf(
            "nickName" to tv_nick_name,
            "rank" to tv_rank,
            "firstName" to et_first_name,
            "lastName" to et_last_name,
            "about" to et_about,
            "repository" to et_repository,
            "rating" to tv_rating,
            "respect" to tv_respect
        )

        isEditMode = savedInstanceState?.getBoolean(
            IS_EDIT_MODE, false
        ) ?: false
        showCurrentMode(isEditMode)

        btn_edit.setOnClickListener {
            Log.d("M_ProfileActivity", "click btn_edit")
            if (isEditMode) saveProfileInfo()
            isEditMode = !isEditMode
            showCurrentMode(isEditMode)
        }

        btn_switch_theme.setOnClickListener {
            Log.d("M_ProfileActivity", "click btn_switch_theme")
            viewModel.switchTheme()
        }
    }

    private fun showCurrentMode(isEdit: Boolean) {
        Log.d("M_ProfileActivity", "showCurrentMode()")
        val info = viewFields.filter {
            setOf("firstName", "lastName", "about", "repository").contains(it.key)
        }
        for ((_, v) in info) {
            v as EditText
            v.isFocusable = isEdit
            v.isFocusableInTouchMode = isEdit
            v.isEnabled = isEdit
            v.background.alpha = if (isEdit) 255 else 0
        }

        ic_eye.visibility = if (isEdit) View.GONE else View.VISIBLE
        wr_about.isCounterEnabled = isEdit

        with(btn_edit) {
            val filter: ColorFilter? = if (isEdit) {
                PorterDuffColorFilter(
                    resources.getColor(R.color.color_accent, theme),
                    PorterDuff.Mode.SRC_IN
                )
            } else null

            val icon = if (isEdit) {
                resources.getDrawable(R.drawable.ic_save_black_24dp, theme)
            } else resources.getDrawable(R.drawable.ic_edit_black_24dp, theme)

            background.colorFilter = filter
            setImageDrawable(icon)
        }
    }

    private fun saveProfileInfo() {
        Log.d("M_ProfileActivity", "saveProfileInfo()")
        Profile(
            firstName = et_first_name.text.toString(),
            lastName = et_last_name.text.toString(),
            about = et_about.text.toString(),
            repository = et_repository.text.toString()
        ).apply {
            viewModel.saveProfileData(this)
        }
    }


}
