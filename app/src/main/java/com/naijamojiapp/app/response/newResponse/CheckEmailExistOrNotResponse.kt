package com.naijamojiapp.app.response.newResponse

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class CheckEmailExistOrNotResponse : Serializable {
    var status: Status? = null

    override fun toString(): String {
        return "ClassPojo [status = $status]"
    }

    inner class Status : Serializable{

        var message: String? = null

        var success: String? = null

        var is_email_exist: String? = null

        override fun toString(): String {
            return "Status(message=$message, success=$success, is_email_exist=$is_email_exist)"
        }

    }

}
