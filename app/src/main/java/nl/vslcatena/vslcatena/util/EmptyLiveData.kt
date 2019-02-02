package nl.vslcatena.vslcatena.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


class EmptyLiveData<T> : LiveData<T>() {
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {}
    override fun observeForever(observer: Observer<in T>) {}
    override fun removeObserver(observer: Observer<in T>) {}
    override fun removeObservers(owner: LifecycleOwner) {}
}