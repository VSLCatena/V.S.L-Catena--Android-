package nl.vslcatena.vslcatena.util.abstractions

import android.view.View
import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView


abstract class LifecycleAwareViewHolder(view: View) : RecyclerView.ViewHolder(view),
    LifecycleOwner {

    override fun getLifecycle() = lifecycleRegistry
    private val lifecycleRegistry = LifecycleRegistry(this)

    init {
        lifecycleRegistry.markState(Lifecycle.State.INITIALIZED)
    }

    @CallSuper
    fun markAttach() {
        lifecycleRegistry.markState(Lifecycle.State.STARTED)
    }

    @CallSuper
    fun markDetach() {
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED)
    }
}