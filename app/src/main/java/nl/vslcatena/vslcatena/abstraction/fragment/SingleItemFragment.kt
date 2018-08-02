package nl.vslcatena.vslcatena.abstraction.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import nl.vslcatena.vslcatena.abstraction.firebase.BaseModel
import nl.vslcatena.vslcatena.abstraction.firebase.LiveViewModel
import nl.vslcatena.vslcatena.util.extensions.applyArguments

private const val ARG_ITEM_ID = "itemId"

/**
 * Abstract fragment for showing a single object from a list in firebase using it's id.
 */
abstract class SingleItemFragment<T:BaseModel>(private val clazz: Class<T>) : Fragment() {
    private lateinit var itemId: String
    protected var item: T? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            itemId = it.getString(ARG_ITEM_ID)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        LiveViewModel.of(this).observeSingle(clazz, itemId){
            onItemRetrieved(it!!)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    abstract fun onItemRetrieved(item: T)


    companion object {

        fun <T: BaseModel> applyItem(fragment: SingleItemFragment<T>, itemId: String) =
                fragment.apply {
                    applyArguments {
                        it.putString(ARG_ITEM_ID, itemId)
                    }
                }
    }
}
