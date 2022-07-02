package com.naijamojiapp.app.response

import java.io.Serializable

class CommonResponseWithObject : Serializable {
    var status: Status? = null

    override fun toString(): String {
        return "ClassPojo [status = $status]"
    }

    inner class Status : Serializable {
        var success: String? = null

        var message: String? = null

        override fun toString(): String {
            return "Status(success=$success, message=$message)"
        }
    }



}
