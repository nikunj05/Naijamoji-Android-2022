package com.naijamojiapp.app.response.newResponse

import java.io.Serializable
import java.util.*

class RendomImageResponse : Serializable {
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

    inner class Result {
        var image: String? = null
        var id: String? = null
        override fun toString(): String {
            return "ClassPojo [image = $image, id = $id]"
        }
    }
}