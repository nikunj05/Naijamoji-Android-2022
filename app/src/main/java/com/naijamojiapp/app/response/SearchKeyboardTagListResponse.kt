package com.naijamojiapp.app.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class SearchKeyboardTagListResponse : Serializable {
    var status: Status? = null

    override fun toString(): String {
        return "ClassPojo [status = $status]"
    }


    inner class Status {
        var result: ArrayList<Result>? = null

        var success: String? = null

        var message: String? = null

        override fun toString(): String {
            return "ClassPojo [result = $result, success = $success, message = $message]"
        }
    }

    inner class Result : Serializable {
        var tag: String? = null


        override fun toString(): String {
            return "Result(tag=$tag)"
        }

    }
}
