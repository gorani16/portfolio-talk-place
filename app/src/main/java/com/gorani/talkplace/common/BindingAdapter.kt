package com.gorani.talkplace.common

import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import com.gorani.talkplace.GlideApp
import com.gorani.talkplace.board.BoardListAdapter


@androidx.databinding.BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String) {

    val storage: FirebaseStorage =
        FirebaseStorage.getInstance("gs://talkplace-4f99f.appspot.com")
    val storageReference = storage.reference
    val pathReference = storageReference.child("$imageUrl.png")

    pathReference.downloadUrl.addOnCompleteListener { uri ->
        if (uri.isSuccessful) {
            Log.d("loadImage", imageUrl)
            GlideApp.with(view.context)
                .load(uri.result)
                .into(view)
        } else {
            Log.d("loadImage", imageUrl)
        }

    }

}
