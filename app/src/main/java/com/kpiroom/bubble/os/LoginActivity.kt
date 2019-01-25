package com.kpiroom.bubble.os

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kpiroom.bubble.R
import kotlinx.android.synthetic.main.activity_login_screen.*
import kotlinx.android.synthetic.main.activity_login_screen.view.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        emailEditText.setOnTouchListener { _, _ ->
            scrollTo(scroll)
            false
        }

        passwordEditText.setOnTouchListener { _, _ ->
            scrollTo(scroll)
            false
        }

        getStartedButton.setOnClickListener {
            root.progress("Message")
        }
    }

    private fun scrollTo(view: View) {
        val rect = Rect()
        view.getGlobalVisibleRect(rect)

        ValueAnimator.ofInt(scroll.scrollY, rect.bottom + scroll.scrollY)
            .apply {
                addUpdateListener {
                    scroll.scrollTo(0, it.animatedValue as Int)
                }

                interpolator = AccelerateDecelerateInterpolator()
                start()
            }
    }
}