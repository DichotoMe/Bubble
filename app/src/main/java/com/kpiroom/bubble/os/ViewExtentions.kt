package com.kpiroom.bubble.os

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Context.displayDialog(
    titleRes: Int = 0, messageRes: Int = 0,
    negativeButtonRes: Int = 0,
    negativeButtonCallback: ((DialogInterface, Int) -> Unit)? = null,
    positiveButtonRes: Int = 0,
    positiveButtonCallback: ((DialogInterface, Int) -> Unit)? = null
) {
    val builder = AlertDialog.Builder(this)
    if (titleRes != 0) builder.setTitle(titleRes)
    if (messageRes != 0) builder.setMessage(messageRes)
    if (negativeButtonRes != 0) builder.setNegativeButton(negativeButtonRes, negativeButtonCallback)
    if (positiveButtonRes != 0) builder.setPositiveButton(positiveButtonRes, positiveButtonCallback)
    builder.show()
}

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