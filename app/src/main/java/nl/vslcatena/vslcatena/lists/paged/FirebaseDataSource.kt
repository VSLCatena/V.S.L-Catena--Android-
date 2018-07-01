package nl.vslcatena.vslcatena.lists.paged

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import android.arch.paging.ItemKeyedDataSource
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import nl.vslcatena.vslcatena.firebase.Deserializer
import nl.vslcatena.vslcatena.firebase.FirebaseModel
import nl.vslcatena.vslcatena.firebase.FirebaseReference
import nl.vslcatena.vslcatena.firebase.LiveViewModel

class FirebaseDataSource<T: FirebaseModel>(private val clazz: Class<T>, private val provider: LiveViewModel.Companion.Provider): ItemKeyedDataSource<String, T>(){
    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<T>) {
        provider.observeList(clazz, true){
            Log.d("InitialListSize", it.size.toString())
            //callback.onResult(it)
        }

        FirebaseDatabase.getInstance().getReference(clazz.getAnnotation(FirebaseReference::class.java).listReference).limitToFirst(params.requestedLoadSize).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                val list = if(p0 == null) ArrayList() else Deserializer.deserializeList(p0, clazz)
                Log.d("InitialListSize", list.size.toString())

                callback.onResult(list)

            }

        })
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<T>) {
        Log.d("PagingtestA", params.key)
        Log.d("PagingtestA", params.requestedLoadSize.toString())
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<T>) {
        Log.d("PagingtestB", params.key)
        Log.d("PagingtestB", params.requestedLoadSize.toString())
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getKey(item: T) = item.id1

}

class FirebaseDataSourceFactory<T:FirebaseModel>(private val clazz: Class<T>, private val provider: LiveViewModel.Companion.Provider) :
        DataSource.Factory<String, T>() {
    val sourceLiveData = MutableLiveData<FirebaseDataSource<T>>()
    override fun create(): DataSource<String, T> {
        val source = FirebaseDataSource(clazz, provider)
        sourceLiveData.postValue(source)
        return source
    }
}



