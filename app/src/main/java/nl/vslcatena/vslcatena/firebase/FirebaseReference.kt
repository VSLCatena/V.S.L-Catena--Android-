package nl.vslcatena.vslcatena.firebase

/**
 * Created by Thomas van den Bulk on 14-5-2018.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class FirebaseReference(val listReference: String = "", val singleReference: String = "")