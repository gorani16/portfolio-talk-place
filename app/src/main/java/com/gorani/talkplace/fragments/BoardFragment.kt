package com.gorani.talkplace.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.gorani.talkplace.R
import com.gorani.talkplace.board.Board
import com.gorani.talkplace.board.BoardInsideActivity
import com.gorani.talkplace.board.BoardListAdapter
import com.gorani.talkplace.board.BoardWriteActivity
import com.gorani.talkplace.databinding.FragmentBoardBinding
import com.gorani.talkplace.utils.FBRef

class BoardFragment: Fragment() {

    private val TAG = BoardFragment::class.java.simpleName

    private lateinit var binding: FragmentBoardBinding

    private val boardDataList = mutableListOf<Board>()
    private val boardKeyList = mutableListOf<String>()

    private lateinit var boardListAdapter: BoardListAdapter

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

        boardListAdapter = BoardListAdapter(boardDataList)

        getBoardData()

        // 서버에서 받아온 데이터를 리사이클러 뷰에 할당
        binding.rvBoardList.adapter = boardListAdapter.apply {
            submitList(boardDataList)
        }

        boardListAdapter.itemClick = object : BoardListAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {

                val intent = Intent(requireContext(), BoardInsideActivity::class.java)
                intent.putExtra("boardKey", boardKeyList[position])
                startActivity(intent)

            }

        }

        binding.btnWrite.setOnClickListener {
            val intent = Intent(context, BoardWriteActivity::class.java)
            startActivity(intent)
        }

    }

    private fun getBoardData() {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                boardDataList.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(Board::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())

                    boardKeyList.reverse()
                    boardDataList.reverse()
                    boardListAdapter.notifyDataSetChanged()

                }
            }

            override fun onCancelled(dataBaserror: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", dataBaserror.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)
    }

}