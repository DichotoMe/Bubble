package com.kpiroom.bubble.util.view

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.kpiroom.bubble.os.BubbleApp
import com.kpiroom.bubble.util.constants.dpToPx

fun Context.getStatusBarHeight(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else {
        Math.ceil((25 * resources.displayMetrics.density).toDouble()).toInt()
    }
}

fun Context.getToolbarHeight(): Int {
    val tv = TypedValue()
    return if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
        TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
    } else {
        dpToPx(56)
    }
}

fun Context.getNavBarHeight(): Int {
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else 0
}


fun isCameraAvailable(): Boolean {
    val app = BubbleApp.app
    return app.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
}

fun Activity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Activity.showKeyboard(view: View? = currentFocus) {
    view?.let {
        it.requestFocus()
        it.performClick()
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)

}

fun View.showKeyboard() {
    requestFocus()
    performClick()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)

}

fun View.setOnBackButtonClicked(condition: (() -> Boolean)? = null, onClicked: () -> Unit) {
    isFocusableInTouchMode = true
    requestFocus()
    setOnKeyListener { _, keyCode, event ->
        if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK && condition?.invoke() ?: true) {
            onClicked()
            true
        } else false
    }
}