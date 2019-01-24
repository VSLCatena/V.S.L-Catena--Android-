package nl.vslcatena.vslcatena.util.abstractions

import androidx.recyclerview.widget.RecyclerView

abstract class LifecycleAwareAdapter<T : RecyclerView.ViewHolder> : RecyclerView.Adapter<T>() {

    override fun onViewAttachedToWindow(holder: T) {
        if (holder is LifecycleAwareViewHolder)
            holder.markAttach()
    }

    override fun onViewDetachedFromWindow(holder: T) {
        if (holder is LifecycleAwareViewHolder)
            holder.markDetach()
    }
}