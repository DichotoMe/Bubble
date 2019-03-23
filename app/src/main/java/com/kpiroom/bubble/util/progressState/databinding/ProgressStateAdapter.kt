package com.kpiroom.bubble.util.progressState.databinding

import androidx.databinding.BindingAdapter
import com.kpiroom.bubble.util.progressState.ProgressState
import com.kpiroom.bubble.util.view.ProgressLayout

@BindingAdapter("app:progressState")
fun setProgressState(progressLayout: ProgressLayout, stateContainer: ProgressState?) {

    fun onLoading(message: String?) {
        message?.let {
            progressLayout.progress(it)
        } ?: run {
            progressLayout.progress()
        }
    }

    fun onAlert(
        message: String,
        callback: ((Boolean) -> Unit)?,
        firstOption: String? = null,
        secondOption: String? = null
    ) {
        callback?.let {
            progressLayout.alert(message, callback, firstOption, secondOption)
        } ?: run {
            progressLayout.alert(message, firstOption = firstOption)
        }
    }

    fun onDismissed() {
        progressLayout.dismiss()
    }

    stateContainer?.let {
        val state = it.state
        val loadingMessage = it.message
        val alertMessage = it.message ?: ""
        val callback = it.alertCallback

        val firstOption = it.firstOption
        val secondOption = it.secondOption

        when (state) {
            ProgressState.LOADING -> onLoading(loadingMessage)
            ProgressState.ALERT -> onAlert(alertMessage, callback, firstOption, secondOption)
            ProgressState.FINISHED -> onDismissed()
        }
    }
}
