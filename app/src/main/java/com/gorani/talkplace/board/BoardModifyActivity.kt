package com.gorani.talkplace.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.gorani.talkplace.GlideApp
import com.gorani.talkplace.R
import com.gorani.talkplace.databinding.ActivityBoardModifyBinding
import com.gorani.talkplace.utils.FBAuth
import com.gorani.talkplace.utils.FBRef
import java.io.ByteArrayOutputStream

class BoardModifyActivity : AppCompatActivity() {

    private val TAG = BoardModifyActivity::class.java.simpleName

    private lateinit var binding: ActivityBoardModifyBinding

    private lateinit var key: String

    private lateinit var boardWriterUid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_modify)

        key = intent.getStringExtra("key") as String
        getBoardData(key)
        getImageData(key)

        binding.btnModify.setOnClickListener {
            editBoardData(key)
        }

        binding.ivBackButton.setOnClickListener {
            Toast.makeText(this, "수정 모드를 종료합니다.", Toast.LENGTH_LONG).show()
            finish()
        }

    }

    private fun editBoardData(key: String) {

        FBRef.boardRef
            .child(key)
            .setValue(
                Board(
                    binding.etModifyTitle.text.toString(),
                    binding.etModifyContent.text.toString(),
                    boardWriterUid,
                    FBAuth.getTime()
                )
            )
        Toast.makeText(this, "수정이 완료됐습니다.", Toast.LENGTH_LONG).show()
        finish()

    }

    private fun getImageData(key: String) {

        val pathString = "$key.png"
        val storageReference = Firebase.storage.reference.child(pathString)
        val imageView = binding.ivAddImageArea

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
                    binding.etModifyTitle.setText(boardData.title)
                    binding.etModifyContent.setText(boardData.content)
                    boardWriterUid = boardData.uid

                }
            }

            override fun onCancelled(dataBaserror: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", dataBaserror.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }

    private fun imageUpload(key: String) {

        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("$key.png")

        val imageView = binding.ivAddImageArea
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            binding.ivAddImageArea.setImageURI(data?.data)
        }
    }
}