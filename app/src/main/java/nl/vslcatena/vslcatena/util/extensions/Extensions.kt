package nl.vslcatena.vslcatena.util.extensions

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import nl.vslcatena.vslcatena.abstraction.firebase.BaseModel
import nl.vslcatena.vslcatena.abstraction.firebase.DataReference
import nl.vslcatena.vslcatena.abstraction.firebase.Deserializer

//If this gets to big, split in multiple files.

fun Fragment.applyArguments(applyFunction: (bundle: Bundle) -> Unit){
    if(arguments == null)
        arguments = Bundle()
    applyFunction(arguments!!)
}


//Updates the values in a BaseModel subclass.
fun <T: BaseModel> BaseModel.updateDataFromFirebase(clazz: Class<T>){
    FirebaseDatabase.getInstance().getReference("${clazz.getAnnotation(DataReference::class.java).listReference}/$id")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val newData = Deserializer.deserializeObject(p0, clazz)
                    clazz.declaredFields.forEach {field ->
                        field.isAccessible = true
                        field.set(this@updateDataFromFirebase, field.get(newData))
                    }
                }
            })
}


fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: Observer<T>) {
    observe(owner, object: Observer<T> {
        override fun onChanged(data: T) {
            observer.onChanged(data)
            removeObserver(this)
        }
    })
}

fun ImageView.setImageFromFirebaseStorage(context: Context, path: String) {
    Glide.with(context)
            .load(FirebaseStorage.getInstance().getReference(path))
            .into(this)
}