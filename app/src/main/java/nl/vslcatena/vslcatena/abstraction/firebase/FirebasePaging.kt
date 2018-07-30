package nl.vslcatena.vslcatena.abstraction.firebase

import android.arch.lifecycle.*
import android.arch.paging.*
import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.util.DiffUtil
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.*

abstract class FirebasePagingAdapter<T: BaseModel, VH: RecyclerView.ViewHolder>(private val context: Context, private val columnCount: Int = 1) : PagedListAdapter<T, VH>(object: DiffUtil.ItemCallback<T>(){
    // This is just used for checking if two items are the same
    override fun areItemsTheSame(oldItem: T, newItem: T) =
            oldItem.id == newItem.id

    // And this is to check if the content is still the same
    override fun areContentsTheSame(oldItem: T, newItem: T) =
        oldItem.toString() == newItem.toString()
}) {
    //inner class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v)

    // To make everything as easy as possible, I have chosen to create the viewholder automatically
    // and let you override getView and onBindViewHolder yourself
    @LayoutRes
    abstract fun getView(): Int
    abstract fun createViewHolder(view: View): VH

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            createViewHolder(LayoutInflater.from(context).inflate(getView(), parent, false))


    /* Recyclerview binders */
    // These are some easy to use functions to bind the adapter to your recyclerview
    fun bindTo(activity: FragmentActivity, recyclerView: RecyclerView, klass: Class<T>, pageSize: Int = 20){
        bindTo(activity, ViewModelProviders.of(activity).get(FirebaseViewModel::class.java) as FirebaseViewModel<T>, recyclerView, klass, pageSize)
    }

    fun bindTo(fragment: Fragment, recyclerView: RecyclerView, klass: Class<T>, pageSize: Int = 20){
        bindTo(fragment, ViewModelProviders.of(fragment).get(FirebaseViewModel::class.java) as FirebaseViewModel<T>, recyclerView, klass, pageSize)
    }

    fun bindTo(lifecycleOwner: LifecycleOwner, firebaseViewModel: FirebaseViewModel<T>, recyclerView: RecyclerView, klass: Class<T>, pageSize: Int = 20){
        recyclerView.adapter = this
        //todo make option for grid
        if (columnCount > 1){
            recyclerView.layoutManager = GridLayoutManager(context, columnCount)
        } else {
            recyclerView.layoutManager = LinearLayoutManager(context)
        }


        // Here we grab the FirebaseReference that we also use for the liveviewmodel
        // This way we have a single workflow for both of them
        val reference = FirebaseDatabase.getInstance().getReference(klass.getAnnotation(FirebaseReference::class.java).listReference)

        // Here we build our model list and when we observe a change in the list we submit it
        // to the PagedListAdapter
        firebaseViewModel.getModelList(reference, klass, pageSize)
                .observe(lifecycleOwner, Observer { pagedList ->
                    submitList(pagedList)
                })
    }
}

// This class holds our references to the paged lists.
// These are bound to the context so they clean up after themselves.
class FirebaseViewModel<T: BaseModel>: ViewModel() {
    private var modelList: HashMap<String, LiveData<PagedList<T>>> = HashMap()

    fun getModelList(reference: DatabaseReference, klass: Class<T>, pageSize: Int): LiveData<PagedList<T>> {
        if(modelList[reference.toString()] == null){
            modelList[reference.toString()] = LivePagedListBuilder(
                    FirebaseDataSourceFactory(reference, klass),
                    pageSize
            ).build()
        }

        return modelList[reference.toString()]!!
    }
}

// The DataSourceFactory creates the DataSource. Don't really know why but we'll just deal with it.
class FirebaseDataSourceFactory<T: BaseModel>(val reference: DatabaseReference, val klass: Class<T>) :
        DataSource.Factory<String, T>() {
    val sourceLiveData = MutableLiveData<FirebaseDataSource<T>>()
    override fun create(): DataSource<String, T> {
        val source = FirebaseDataSource(reference, klass)
        sourceLiveData.postValue(source)
        return source
    }
}

// This is the class that actualy retrieves the data from firebase.
// There are 3 functions (loadInitial, loadBefore and loadAfter) that loads our data.
// We just let them all call the same function because there isn't really a difference between them
class FirebaseDataSource<T: BaseModel>(val reference: DatabaseReference, val klass: Class<T>) : ItemKeyedDataSource<String, T>(){

    override fun getKey(item: T) = item.id

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<T>) {
        fetch(params.requestedLoadSize, params.requestedInitialKey, callback)
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<T>) {
        fetch(params, callback)
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<T>) {
        fetch(params, callback)
    }

    private fun fetch(params: LoadParams<String>, callback: LoadCallback<T>){
        fetch(params.requestedLoadSize, params.key, callback)
    }

    private fun fetch(loadSize: Int, startAt: String?, callback: LoadCallback<T>){
        var _reference: Query = reference
        if(startAt != null)
            _reference = _reference.startAt(startAt)
        _reference.limitToFirst(loadSize)
        _reference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                callback.onResult(Deserializer.deserializeList(p0, klass))
            }
            override fun onCancelled(p0: DatabaseError) {}
        })
    }
}