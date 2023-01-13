package com.gorani.talkplace.board

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gorani.talkplace.databinding.ItemBoardListBinding

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

    class BoardListViewHolder(private val binding: ItemBoardListBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(board: Board) {
            binding.tvBoardTitle.text = board.title
            binding.tvBoardContent.text = board.content
            binding.tvBoardTime.text = board.time
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