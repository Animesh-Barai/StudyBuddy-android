package com.edricchan.studybuddy.interfaces.chat.icon

import com.edricchan.studybuddy.interfaces.HasId
import com.edricchan.studybuddy.interfaces.HasTimestampMetadata
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

/**
 * Data class used to represent a chat icon.
 * @property assets The list of icon assets (See [ChatIconAsset] for more info)
 * @property id The chat icon's document ID
 * @property name The name of the chat icon (if the chat icon is a default)
 * @property description A description of the chat icon
 * @property subjectType The type of subject that the chat icon represents
 * @property author The author (Can be a string or a [com.google.firebase.firestore.DocumentReference])
 * @property authorInfo The author's information/bio, if any
 * @property userSubmitted Whether the chat icon was submitted by a user
 */
data class ChatIcon(
    @DocumentId override var id: String = "",
    val assets: List<ChatIconAsset>? = null,
    val name: String? = null,
    val description: String? = null,
    @ChatIconSubjectTypeAnnotation val subjectType: String? = ChatIconSubjectType.OTHER,
    val author: Any? = null,
    var authorInfo: String? = null,
    val userSubmitted: Boolean? = true,
    override val createdAt: Timestamp? = null,
    override val lastModified: Timestamp? = null
) : HasId, HasTimestampMetadata