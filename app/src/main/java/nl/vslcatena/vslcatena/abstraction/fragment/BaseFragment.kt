package nl.vslcatena.vslcatena.abstraction.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import nl.vslcatena.vslcatena.R

abstract class BaseFragment : Fragment() {

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View? {
//        return TextView(activity).apply {
//            setText(R.string.hello_blank_fragment)
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val needsAuth = this::class.java.getAnnotation(NeedsAuthentication::class.java) != null

        if(needsAuth) {
            Log.d("Auth", "This fragment (${this::class.java}) needs auth")
            if (FirebaseAuth.getInstance().currentUser == null) {
                Log.d("Auth", "Not authenticated")
                //Todo redirect to Login screen (Use dependency injection?)
            }
        }
        else
            Log.d("Auth", "This fragment (${this::class.java}) doesn't need auth")

    }


}
