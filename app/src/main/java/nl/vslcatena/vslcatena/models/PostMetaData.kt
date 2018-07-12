package nl.vslcatena.vslcatena.models

import nl.vslcatena.vslcatena.firebase.BaseModel

/**
 * Meta-data that a post should contain:
 * @param postedTime The time the post is made.
 * @param postedBy The id of the user that made the post.
 * @param title The title of the post.
 * @param content The content of the post.
 * @param tags The tags that were given to the post.
 */
data class PostMetaData(
        override val id: String,
        val postedTime: Long,
        val title: String,
        val postedBy: String,
        val content: String,
        val tags: List<String>
): BaseModel {

    constructor(): this(
        "",
        -1,
        "",
        "",
        "",
        ArrayList()
    )

}