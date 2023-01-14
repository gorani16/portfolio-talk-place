package com.gorani.talkplace.board

data class Board(
    val title: String = "",
    val content: String = "",
    val uid: String = "",
    val time: String = "",
    val thumbnailImageUrl: String? = ""
)
