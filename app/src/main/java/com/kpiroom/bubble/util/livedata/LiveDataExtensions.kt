package com.kpiroom.bubble.util.livedata

import androidx.annotation.NonNull
import androidx.lifecycle.*


fun <T : Any?> MutableLiveData<T>.setDefault(default: T?) = apply { value = default }
fun <T : Any?> swapValues(liveData1: MutableLiveData<T>, liveData2: MutableLiveData<T>) {
    val temp: T? = liveData1.value
    liveData1.value = liveData2.value
    liveData2.value = temp
}
fun LiveData<Boolean>.observeTrue(owner: LifecycleOwner, observer: Observer<Boolean>) {
    observe(owner, Observer {
        if (it == true)
            observer.onChanged(it)
    })
}

fun <T> LiveData<T>.observeNotNull(owner: LifecycleOwner, observer: Observer<T>) {
    observe(owner, Observer { data ->
        data?.let {
            observer.onChanged(it)
        }
    })
}

fun <T> MediatorLiveData<T>.addSource(source: LiveData<T>) {
    addSource(source) {
        value = it
    }
}

fun <T, S> MediatorLiveData<T>.addSourceNotNull(source: LiveData<S>, observer: (S) -> Unit) {
    addSource(source) { data ->
        data?.let {
            observer(it)
        }
    }
}