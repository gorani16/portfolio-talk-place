package com.gorani.talkplace.board

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.gorani.talkplace.R
import com.gorani.talkplace.databinding.ActivityBoardWriteBinding
import com.gorani.talkplace.utils.FBAuth
import com.gorani.talkplace.utils.FBRef

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
            val title = binding.etWriteTitle.text.toString()
            val content = binding.etWriteContent.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()

            FBRef.boardRef
                .push()
                .setValue(Board(title, content, uid, time))

            Toast.makeText(this, "게시되었습니다.", Toast.LENGTH_LONG).show()
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
    }
}