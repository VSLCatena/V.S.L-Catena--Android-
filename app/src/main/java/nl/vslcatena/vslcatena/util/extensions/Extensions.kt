package nl.vslcatena.vslcatena.util.extensions

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.ImageView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import nl.vslcatena.vslcatena.abstraction.firebase.BaseModel
import nl.vslcatena.vslcatena.abstraction.firebase.Deserializer
import nl.vslcatena.vslcatena.abstraction.firebase.FirebaseReference
import nl.vslcatena.vslcatena.util.GlideApp

//If this gets to big, split in multiple files.

fun Fragment.applyArguments(applyFunction: (bundle: Bundle) -> Unit){
    if(arguments == null)
        arguments = Bundle()
    applyFunction(arguments!!)
}


//Updates the values in a BaseModel subclass.
fun <T: BaseModel> BaseModel.updateDataFromFirebase(clazz: Class<T>){
    FirebaseDatabase.getInstance().getReference("${clazz.getAnnotation(FirebaseReference::class.java).listReference}/$id")
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


fun <T> LiveData<T>.observeOnce(observer: Observer<T>){

    val containerObserver = object: Observer<T> {
        override fun onChanged(t: T?) {
            if(t== null)
                return
            observer.onChanged(t)
            removeObserver(this)
        }
    }
    observeForever(containerObserver)
}

fun ImageView.setImageFromFirebaseStorage(context: Context, path: String) {
    GlideApp.with(context)
            .load(FirebaseStorage.getInstance().getReference(path))
            .into(this)
}