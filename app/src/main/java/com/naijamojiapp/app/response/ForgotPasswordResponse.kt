package com.naijamojiapp.app.response

import java.io.Serializable

class ForgotPasswordResponse : Serializable {
    var status: Status? = null

    override fun toString(): String {
        return "ClassPojo [status = $status]"
    }

    inner class Status : Serializable{
        var message: String? = null

        var success: String? = null


        override fun toString(): String {
            return "Status(message=$message, success=$success)"
        }


    }
}
