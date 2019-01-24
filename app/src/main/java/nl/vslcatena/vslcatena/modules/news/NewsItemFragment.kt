package nl.vslcatena.vslcatena.modules.news


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.news_item.*
import kotlinx.android.synthetic.main.post_header.*
import nl.vslcatena.vslcatena.BaseFragment
import nl.vslcatena.vslcatena.R
import nl.vslcatena.vslcatena.models.Identifier
import nl.vslcatena.vslcatena.models.User
import nl.vslcatena.vslcatena.util.LiveDataWrapper
import nl.vslcatena.vslcatena.util.data.DataCreator
import nl.vslcatena.vslcatena.util.data.observeSingle
import nl.vslcatena.vslcatena.util.extensions.observeOnce
import nl.vslcatena.vslcatena.util.extensions.setImageFromFirebaseStorage
import nl.vslcatena.vslcatena.util.login.NeedsAuthentication
import java.text.SimpleDateFormat

/**
 * Fragment for showing a single newsItem.
 *
 */
@NeedsAuthentication
class NewsItemFragment : BaseFragment() {

    private val user = LiveDataWrapper<User>()
    private lateinit var news: LiveData<News>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.news_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        news = DataCreator.getSingleReference(
            News::class.java,
            Identifier(NewsItemFragmentArgs.fromBundle(arguments).itemId)
        )
        news.observeOnce(this, Observer {
            user.wrap(
                DataCreator.getSingleReference(
                    User::class.java,
                    it.user
                )
            )
        })

        val sdf = SimpleDateFormat.getDateTimeInstance()

        news.observe(this, Observer {
            item_title.text = it.title
            item_content.text = it.content
            postHeaderPostDate.text = sdf.format(it.date)
        })

        user.observe(this, Observer {
            postHeaderUserName.text = it.name
            postHeaderUserImage.setImageFromFirebaseStorage(it.getThumbnailRef())
        })
    }
}
