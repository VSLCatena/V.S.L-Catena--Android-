package nl.vslcatena.vslcatena.util.abstractions

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import nl.vslcatena.vslcatena.BaseCoroutineFragment
import nl.vslcatena.vslcatena.models.Identifier
import nl.vslcatena.vslcatena.modules.news.NewsEditItemFragmentArgs
import nl.vslcatena.vslcatena.util.Result
import nl.vslcatena.vslcatena.util.data.BaseModel
import nl.vslcatena.vslcatena.util.data.DataCreator
import nl.vslcatena.vslcatena.util.extensions.observeOnce

abstract class EditItemFragment<T : BaseModel> : BaseCoroutineFragment(), Observer<T> {

    protected var editId: Identifier? = null
    protected var editItem: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            editId = NewsEditItemFragmentArgs.fromBundle(arguments).editId?.let {
                Identifier(it)
            }
        }

        editId?.let { editId ->
            showProgress(true)

            val observer = Observer<T> {
                onChanged(it)
                showProgress(false)
            }

            DataCreator.getSingleReference(getItemClass(), editId)
                .observeOnce(this, observer)
        }
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getSubmitButton().setOnClickListener { submit() }
    }


    fun submit() {
        getSubmitButton().let { button ->
            button.isClickable = false
            button.isEnabled = false
        }

        launch {
            val result = doSubmit()

            if (result.isSuccesful()) {
                // If we were successful we show a toast and we go back
                Toast.makeText(context, "Succesvol geplaatst!", Toast.LENGTH_LONG)
                    .show()
                findNavController().popBackStack()
            } else {
                // If we failed we show a toast telling what went wrong, and then re-enable the button
                Toast.makeText(
                    context,
                    "Oops! Er ging iets fout: " + result.getExceptionOrNull()?.message,
                    Toast.LENGTH_LONG
                ).show()

                getSubmitButton().let {
                    it.isEnabled = true
                    it.isClickable = true
                }
            }
        }
    }

    abstract fun getItemClass(): Class<T>

    abstract fun getSubmitButton(): MaterialButton

    abstract suspend fun doSubmit(): Result<out Any?>
}