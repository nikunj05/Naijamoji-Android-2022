package com.naijamojiapp.app.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class EmojiListResponse : Serializable {
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
        var id: String? = null

        var image: String? = null

        var gender: String? = null

        var skin_tone: String? = null

        var image_withtext: String? = null

        var title: String? = null

        var category: String? = null

        var character_limit: String? = null

        var is_studiomode: String? = null

        override fun toString(): String {
            return "Result(image=$image, gender=$gender, skin_tone=$skin_tone, image_withtext=$image_withtext, id=$id, title=$title, category=$category, character_limit=$character_limit, is_studiomode=$is_studiomode)"
        }


    }
}
