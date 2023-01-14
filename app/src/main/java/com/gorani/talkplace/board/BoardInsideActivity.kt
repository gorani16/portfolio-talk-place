package com.gorani.talkplace.board

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

    private lateinit var key: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)

        binding.boardSettingButton.setOnClickListener {
            showDialog()
        }

        key = intent.getStringExtra("boardKey") as String
        getBoardData(key)
        getImageData(key)

        binding.ivBackButton.setOnClickListener {
            finish()
        }

    }

    private fun showDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정 / 삭제")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.btn_modify)?.setOnClickListener {
            Toast.makeText(this, "수정모드 활성화.", Toast.LENGTH_LONG).show()

        }

        alertDialog.findViewById<Button>(R.id.btn_delete)?.setOnClickListener {

            FBRef.boardRef.child(key).removeValue()
            Toast.makeText(this, "삭제됐습니다.", Toast.LENGTH_LONG).show()
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

                try {
                    val boardData = dataSnapshot.getValue(Board::class.java)
                    boardData?.let {
                        binding.tvBoardTitle.text = boardData.title
                        binding.tvBoardContent.text = boardData.content
                        binding.tvWrittenTime.text = boardData.time
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "게시글 삭제 완료")
                }


            }

            override fun onCancelled(dataBaserror: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", dataBaserror.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }

}