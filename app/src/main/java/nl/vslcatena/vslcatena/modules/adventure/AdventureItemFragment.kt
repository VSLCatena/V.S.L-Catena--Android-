package nl.vslcatena.vslcatena.modules.adventure


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import nl.vslcatena.vslcatena.BaseFragment
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.util.data.observeSingle

/**
 * Fragment for showing a single adventure item.
 */
class AdventureItemFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_adventure_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val arguments = arguments ?: return
        observeSingle<Adventure>(
            AdventureItemFragmentArgs.fromBundle(arguments).itemId,
            Observer {
                //
            }
        )
    }

}
