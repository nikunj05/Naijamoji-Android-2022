package com.naijamojiapp.app.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SocialLoginSignUpResponse : Serializable {
    var status: Status? = null

    override fun toString(): String {
        return "ClassPojo [status = $status]"
    }

    inner class Status {
        var result: Result? = null

        var success: String? = null

        var message: String? = null

        override fun toString(): String {
            return "ClassPojo [result = $result, success = $success, message = $message]"
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
