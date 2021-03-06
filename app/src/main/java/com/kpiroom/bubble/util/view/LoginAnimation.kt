package com.kpiroom.bubble.util.view

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.graphics.Rect
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.google.android.material.animation.ArgbEvaluatorCompat
import com.kpiroom.bubble.R
import com.kpiroom.bubble.util.constants.col

class LoginAnimation(private val storage: MutableCollection<ValueAnimator>) {

    companion object {
        const val DURATION = 300L
    }

    private val animUtils = AnimationUtils
    fun transformTextColor(
        view: TextView,
        duration: Long = DURATION,
        interpolator: TimeInterpolator = AccelerateInterpolator()
    ) =
        animUtils.transformTextColor(
            view,
            ArgbEvaluatorCompat(),
            col(R.color.colorPrimary),
            col(R.color.colorTextRed),
            duration,
            interpolator
        ).addTo(storage)

    fun scaleXY(view: View) =
        animUtils.scaleXY(
            view,
            scaleEnd = 1.14f
        ).addTo(storage)

    fun transformAlpha(view: View, isIncreasing: Boolean = true, duration: Long = DURATION) =
        if (isIncreasing) {
            animUtils.transformAlpha(
                view,
                0.0f,
                1.0f,
                duration
            ).addTo(storage)

        } else {
            animUtils.transformAlpha(
                view,
                1.0f,
                0.0f,
                duration
            ).addTo(storage)
        }

    fun transformEditTextAlpha(view: View, isIncreasing: Boolean, duration: Long = DURATION): ValueAnimator {
        if (isIncreasing) {
            val animator = animUtils.transformAlpha(
                view,
                0.0f,
                1.0f,
                duration
            ).addTo(storage)
            animator.doOnStart {
                val focusable = view.isFocusableInTouchMode
                if (!focusable) {
                    view.isFocusableInTouchMode = true
                } else if (focusable) {
                    view.isFocusableInTouchMode = false
                    view.clearFocus()
                }
            }

            return animator

        } else {
            val animator = animUtils.transformAlpha(
                view,
                1.0f,
                0.0f,
                duration
            ).addTo(storage)
            animator.doOnEnd {
                val visibility = view.visibility
                if (visibility == View.VISIBLE) {
                    view.visibility = View.INVISIBLE
                } else if (visibility == View.INVISIBLE) {
                    view.visibility = View.VISIBLE
                }
            }

            return animator
        }
    }

    fun transformHeight(view: TextView, heightBegin: Int, heightEnd: Int, duration: Long = 200L) =
        animUtils.transformHeight(
            view,
            heightBegin,
            heightEnd,
            duration
        ).addTo(storage)

    fun translateY(view: TextView, positionBegin: Float, positionEnd: Float) =
        animUtils.translateY(
            view,
            positionBegin,
            positionEnd
        ).addTo(storage)

    fun smoothScrollToBottom(view: View, scrollView: ScrollView): ValueAnimator {
        val rect = Rect()
        view.getGlobalVisibleRect(rect)

        return ValueAnimator.ofInt(scrollView.scrollY, scrollView.scrollY + rect.bottom)
            .apply {
                interpolator = AccelerateDecelerateInterpolator()
                addUpdateListener {
                    scrollView.smoothScrollTo(0, it.animatedValue as Int)
                }
            }
    }
}