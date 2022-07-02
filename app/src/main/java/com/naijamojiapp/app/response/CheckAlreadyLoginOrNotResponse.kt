package com.naijamojiapp.app.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class CheckAlreadyLoginOrNotResponse : Serializable {
    var status: Status? = null

    override fun toString(): String {
        return "ClassPojo [status = $status]"
    }

    inner class Status : Serializable{
        var result: Result? = null

        var message: String? = null

        var success: String? = null

        var is_login: String? = null

        override fun toString(): String {
            return "ClassPojo [message = $message, success = $success, is_login = $is_login]"
        }
    }


    inner class Result {
        var id: String? = null

        var social_id: String? = null

        var email: String? = null

        var first_name: String? = null

        var last_name: String? = null

        var gender: String? = null

        var skin_tone: String? = null

        var login_type: String? = null

        var token: String? = null

        var birth_date: String? = null

        override fun toString(): String {
            return "Result(id=$id, social_id=$social_id, email=$email, first_name=$first_name, last_name=$last_name, gender=$gender, skin_tone=$skin_tone, login_type=$login_type, token=$token, birth_date=$birth_date)"
        }
    }
}
