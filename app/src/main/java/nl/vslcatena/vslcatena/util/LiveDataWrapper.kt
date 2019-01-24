package nl.vslcatena.vslcatena.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * A liveData wrapper that can be used so you can initialize the liveData before linking it to a
 * source. An example is for when you want to hook observers in the constructor, but have to wait
 * for parameters from an outside source (viewModels have this problem). Just create this object,
 * and when the source is created/know, call #wrap and give it the liveData object.
 */
class LiveDataWrapper<T>(
    private var wrapped: LiveData<T>? = null
) : MutableLiveData<T>(), Observer<T> {

    /**
     * When we wrap a liveData object, we first remove ourselves from our previous source, if any
     * then we check if we have active observers and if so, hook ourselves up.
     */
    fun wrap(liveData: LiveData<T>?) {
        // If we had a liveData wrapped before, we remove ourselves from that
        wrapped?.removeObserver(this)

        wrapped = liveData
        // If we have active observers, we start observing the wrapped object
        if (hasActiveObservers()) {
            liveData?.observeForever(this)
        }
    }

    /** When our wrapped object's value changed, we want to proxy it to our own object */
    override fun onChanged(t: T) {
        value = t
    }

    /** If we become active we start observing our wrapped object */
    override fun onActive() {
        wrapped?.observeForever(this)
    }

    /** When we become inactive we want to stop observing our wrapped object (for GC reasons) */
    override fun onInactive() {
        wrapped?.removeObserver(this)
    }
}
