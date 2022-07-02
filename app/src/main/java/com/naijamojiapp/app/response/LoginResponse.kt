package com.naijamojiapp.app.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class LoginResponse : Serializable {
    var status: Status? = null

    override fun toString(): String {
        return "ClassPojo [status = $status]"
    }

    inner class Status : Serializable{
        var result: Result? = null

        var message: String? = null

        var success: String? = null

        override fun toString(): String {
            return "ClassPojo [result = $result, message = $message, success = $success]"
        }
    }


    inner class Result : Serializable{
        var id: String? = null

        var social_id: String? = null

        var email: String? = null

        var first_name: String? = null

        var last_name: String? = null

        var gender: String? = null

        var skin_tone: String? = null

        var birth_date  : String? = null

        var login_type: String? = null

        var token: String? = null

        override fun toString(): String {
            return "Result(id=$id, social_id=$social_id, email=$email, first_name=$first_name, last_name=$last_name, gender=$gender, skin_tone=$skin_tone, birth_date=$birth_date, login_type=$login_type, token=$token)"
        }


    }
}
