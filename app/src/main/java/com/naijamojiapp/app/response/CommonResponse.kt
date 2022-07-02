package com.naijamojiapp.app.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class CommonResponse : Serializable {
    var status: Status? = null

    override fun toString(): String {
        return "ClassPojo [status = $status]"
    }

    inner class Status : Serializable{
        var message: String? = null

        var success: String? = null

        override fun toString(): String {
            return "ClassPojo [message = $message, success = $success]"
        }
    }

}
