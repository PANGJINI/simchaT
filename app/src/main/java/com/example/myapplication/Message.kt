package com.example.myapplication

import org.w3c.dom.Comment

class Message(
    var message: String?,
    var sendId: String?) {
    constructor():this("","")
}

//data class ChatModel(
//    var comments: HashMap<String, Comment> = HashMap(),
//    var sendId: String?) {
//    class Comment(val uid:String? = null, val message: String? = null, val time: String? = null)
//}