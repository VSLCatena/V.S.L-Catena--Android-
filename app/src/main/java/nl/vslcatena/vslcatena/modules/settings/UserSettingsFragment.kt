package nl.vslcatena.vslcatena.modules.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import nl.vslcatena.vslcatena.R

/**
 * Fragment where the user can adjust setting relating to his account:
 *  - Change his profile picture
 *  - Manage subscriptions?
 *
 */
class UserSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun preference
}
