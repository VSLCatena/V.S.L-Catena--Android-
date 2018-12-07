package nl.vslcatena.vslcatena.abstraction.firebase

/**
 * Created by Thomas van den Bulk on 14-5-2018.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class DataReference(val listReference: String = "", val singleReference: String = "")