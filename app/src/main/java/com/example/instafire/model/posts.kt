package com.example.instafire.model

import com.google.firebase.firestore.PropertyName

data class posts (
    var description: String = "",
    @get:PropertyName("image_url") @set:PropertyName("image_url")var image_url: String = "",
    @get:PropertyName("creation_time") @set:PropertyName("creation_time") var creation_time: Long =0,
    var user: User? = null
)