package nl.vslcatena.vslcatena.util.login

import nl.vslcatena.vslcatena.models.Role

/**
 * Used for fragments that need authentication to view. Beheviour is set up in the BaseFragment
 * //todo add option for roles
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class NeedsAuthentication(val role: Role = Role.USER)



