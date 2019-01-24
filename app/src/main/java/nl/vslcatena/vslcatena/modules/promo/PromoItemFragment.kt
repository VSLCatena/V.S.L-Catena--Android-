package nl.vslcatena.vslcatena.modules.promo


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import nl.vslcatena.vslcatena.BaseFragment
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.util.login.NeedsAuthentication

/**
 * Fragment for showing a single promo item.
 */
@NeedsAuthentication
class PromoItemFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_promo_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }
}


