package nl.vslcatena.vslcatena.abstraction.fragment

import com.google.firebase.auth.FirebaseAuth
import kotlin.reflect.KClass

/**
 * Used for fragments that need authentication to view. Beheviour is set up in the BaseFragment
 * //todo add option for roles
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class NeedsAuthentication()



