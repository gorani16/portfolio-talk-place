package com.gorani.talkplace.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.gorani.talkplace.R
import com.gorani.talkplace.board.BoardWriteActivity
import com.gorani.talkplace.databinding.FragmentBoardBinding

class BoardFragment: Fragment() {

    private lateinit var binding: FragmentBoardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnWrite.setOnClickListener {
            Toast.makeText(requireContext(), "게시글 작성 버튼", Toast.LENGTH_LONG).show()
            val intent = Intent(context, BoardWriteActivity::class.java)
            startActivity(intent)
        }

    }

}