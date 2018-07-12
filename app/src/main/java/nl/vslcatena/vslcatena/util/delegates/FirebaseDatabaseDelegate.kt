package nl.vslcatena.vslcatena.util.delegates

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import nl.vslcatena.vslcatena.firebase.Deserializer
import nl.vslcatena.vslcatena.firebase.FirebaseReference
import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


//TODO Make this working or remove it
class FirebaseDatabaseDelegate<in R, T>(val id: String, val clazz: Class<T>): ReadOnlyProperty<R, FirebaseDatabaseDelegated<T>> {
    private val delegated = FirebaseDatabaseDelegated<T>()

    init {
        with(FirebaseDatabase.getInstance().getReference(clazz.getAnnotation(FirebaseReference::class.java).listReference).child(id)){
            addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    //Maybe some code here to tell the deligated that is isnt going to receive a value.
                }
                override fun onDataChange(p0: DataSnapshot) {
                    delegated.item = Deserializer.deserializeObject(p0, clazz)
                }
            })
        }
    }

    override fun getValue(thisRef: R, property: KProperty<*>): FirebaseDatabaseDelegated<T> = delegated
}

class FirebaseDatabaseDelegated<T>{
    val pendingActions = ArrayList<OnValueGiven<T>>()

    var item: T? = null

//            by Delegates.observable(){
//        property, oldValue, newValue ->
//
//        if (newValue != null)
//            pendingActions.forEach { it.onValueGiven(newValue) }
//
//    }


    fun getValue(onReceived: OnValueGiven<T>){
        if (item != null) {
            onReceived.onValueGiven(item!!)
        } else {
            pendingActions.add(onReceived)
        }
    }

    interface OnValueGiven<T>{
        fun onValueGiven(item:T)
    }
}