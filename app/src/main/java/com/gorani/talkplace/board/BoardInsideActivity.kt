package com.gorani.talkplace.board

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.gorani.talkplace.GlideApp
import com.gorani.talkplace.R
import com.gorani.talkplace.databinding.ActivityBoardInsideBinding
import com.gorani.talkplace.utils.FBRef

class BoardInsideActivity : AppCompatActivity() {

    private val TAG = this::class.java.simpleName

    private lateinit var binding: ActivityBoardInsideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)


        // 이 key 로 해당 게시글, 해당 이미지를 가지고옴
        val key = intent.getStringExtra("boardKey") as String
        getBoardData(key)
        getImageData(key)

        binding.ivBackButton.setOnClickListener {
            finish()
        }

    }

    private fun getImageData(key: String) {

        val pathString = "$key.png"
        val storageReference = Firebase.storage.reference.child(pathString)
        val imageView = binding.ivBoardImageArea

        storageReference.downloadUrl.addOnCompleteListener { uri ->
            if (uri.isSuccessful) {

                GlideApp.with(this)
                    .load(uri.result)
                    .into(imageView)
            }
        }

    }

    private fun getBoardData(key: String) {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val boardData = dataSnapshot.getValue(Board::class.java)
                boardData?.let {
                    binding.tvBoardTitle.text = boardData.title
                    binding.tvBoardContent.text = boardData.content
                    binding.tvWrittenTime.text = boardData.time
                }

            }

            override fun onCancelled(dataBaserror: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", dataBaserror.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }

}