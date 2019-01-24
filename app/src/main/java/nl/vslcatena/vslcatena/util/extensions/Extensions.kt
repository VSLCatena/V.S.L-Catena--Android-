package nl.vslcatena.vslcatena.util.extensions

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

//If this gets to big, split in multiple files.

fun Fragment.applyArguments(applyFunction: (bundle: Bundle) -> Unit) {
    if (arguments == null)
        arguments = Bundle()
    applyFunction(arguments!!)
}


//Updates the values in a BaseModel subclass.
// Removed Because it can't be observed
//fun <T: BaseModel> BaseModel.updateDataFromFirebase(){
//    FirebaseDatabase.getInstance().getReference("${this.javaClass.getAnnotation(DataReference::class.java)!!.listReference}/$id")
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onCancelled(p0: DatabaseError) {
//
//                }
//
//                override fun onDataChange(p0: DataSnapshot) {
//                    val newData = Deserializer.deserializeObject(p0, this.javaClass)
//                    this.javaClass.declaredFields.forEach {field ->
//                        field.isAccessible = true
//                        field.set(this@updateDataFromFirebase, field.get(newData))
//                    }
//                }
//            })
//}


fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: Observer<T>) {
    observe(owner, object : Observer<T> {
        override fun onChanged(data: T) {
            observer.onChanged(data)
            removeObserver(this)
        }
    })
}

fun ImageView.setImageFromFirebaseStorage(path: String) {
    Glide.with(context)
        .load(FirebaseStorage.getInstance().getReference(path))
        .into(this)
}

suspend fun <T> Task<T>.await(): T? {
    return suspendCoroutine { continuation ->
        addOnCompleteListener { task ->
            continuation.resume(task.result)
        }
    }
}