package com.gorani.talkplace.board

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.RequestManager
import com.gorani.talkplace.databinding.ItemBoardListBinding
import com.gorani.talkplace.utils.FBAuth

class BoardListAdapter(private val items: MutableList<Board>): ListAdapter<Board, BoardListAdapter.BoardListViewHolder>(BoardListDiffCallback())  {

    private lateinit var binding: ItemBoardListBinding

    interface ItemClick {
        fun onClick(view: View, position: Int )
    }

    var itemClick: ItemClick? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardListViewHolder {

        binding = ItemBoardListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BoardListViewHolder(binding)

    }

    override fun onBindViewHolder(holder: BoardListViewHolder, position: Int) {

        if (itemClick != null) {
            holder.itemView.setOnClickListener { view ->
                itemClick?.onClick(view, position)
            }
        }
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class BoardListViewHolder(private val binding: ItemBoardListBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(board: Board) {

            binding.board = board
            if (board.uid == FBAuth.getUid()) {
                binding.itemView.setBackgroundColor(Color.parseColor("#F1DCFF"))
            }
            binding.executePendingBindings()
        }

    }
}

class BoardListDiffCallback : DiffUtil.ItemCallback<Board>() {

    override fun areItemsTheSame(oldItem: Board, newItem: Board): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: Board, newItem: Board): Boolean {
        return oldItem == newItem
    }

}