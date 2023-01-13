package com.gorani.talkplace.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.gorani.talkplace.R
import com.gorani.talkplace.databinding.ActivityBoardWriteBinding
import com.gorani.talkplace.utils.FBAuth
import com.gorani.talkplace.utils.FBRef
import java.io.ByteArrayOutputStream

class BoardWriteActivity: AppCompatActivity() {

    private lateinit var binding: ActivityBoardWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)

        /** board 데이터베이스 구조
         * board
         *  -key -> push() : 랜덤한 값이 생성된다.
         *      -boardModel( title, content, uid, time )
         */
        binding.btnInput.setOnClickListener {

            var isWritten = true
            val title = binding.etWriteTitle.text.toString()
            val content = binding.etWriteContent.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()

            if (title.isEmpty()) {
                Toast.makeText(this, "제목을 입력하세요.", Toast.LENGTH_LONG).show()
                isWritten = false

            } else if (content.isEmpty()) {
                Toast.makeText(this, "내용을 입력하세요.", Toast.LENGTH_LONG).show()
                isWritten = false

            }

            val key = FBRef.boardRef.push().key as String

            if (isWritten) {
                FBRef.boardRef
                    .child(key)
                    .setValue(Board(title, content, uid, time))

                Toast.makeText(this, "게시글이 작성됐습니다.", Toast.LENGTH_LONG).show()

                imageUpload(key)

                finish()
            }

        }

        binding.ivBackButton.setOnClickListener {
            finish()
        }

        /**
         * 삽질 기록
         FBRef.boardRef
        .child(FBAuth.getUid())
        .setValue(Board(title, content, uid, time))
         => board
            -현재 사용자의 uid
                -boardModel
         특이점 : 저장 및 최신화가 딱 한 번만 이루어짐. 저 쿼리로 계속해서 작성을 해도 기존의 내용만 바뀔뿐이며 단 하나만 저장됨.
         */

        binding.ivAddImageArea.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
        }
    }

    private fun imageUpload(key: String) {

        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("$key + png")

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

    // TODO 권한 요청 기능 추가하기.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            binding.ivAddImageArea.setImageURI(data?.data)
        }
    }

}