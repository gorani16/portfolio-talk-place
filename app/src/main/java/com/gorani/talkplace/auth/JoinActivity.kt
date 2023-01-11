package com.gorani.talkplace.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gorani.talkplace.MainActivity
import com.gorani.talkplace.R
import com.gorani.talkplace.databinding.ActivityJoinBinding

class JoinActivity: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)

        binding.btnJoin.setOnClickListener {

            var isGoToJoin = true

            val emailAddress = binding.etEmailAddress.text.toString()
            val password = binding.etPassword.text.toString()
            val passwordCheck = binding.etPasswordCheck.text.toString()

            if (emailAddress.isEmpty()) {
                Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false

            } else if (password.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false

            } else if (passwordCheck.isEmpty()) {
                Toast.makeText(this, "비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false

            } else if (password != passwordCheck) {
                Toast.makeText(this, "비밀번호를 똑같이 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false

            } else if (password.length < 6) {
                Toast.makeText(this, "비밀번호를 6자리 이상 입력해주세요..", Toast.LENGTH_LONG).show()
                isGoToJoin = false

            }

            if (isGoToJoin) {
                auth.createUserWithEmailAndPassword(emailAddress, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "회원가입 성공", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)

                        } else {
                            Toast.makeText(this, "회원가입 실패", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
}