package com.naijamojiapp.app.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class StickerTagListResponse : Serializable {
    var status: Status? = null

    override fun toString(): String {
        return "ClassPojo [status = $status]"
    }

    inner class Status : Serializable {
        var result: ArrayList<Result>? = null

        var success: String? = null

        var message: String? = null

        override fun toString(): String {
            return "ClassPojo [result = $result, success = $success, message = $message]"
        }
    }

    inner class Result : Serializable{
        var id: String? = null

        var title: String? = null

        override fun toString(): String {
            return "ClassPojo [id = $id, title = $title]"
        }
    }
}
