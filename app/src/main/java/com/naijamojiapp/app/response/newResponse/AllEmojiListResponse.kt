package com.naijamojiapp.app.response.newResponse

import java.io.Serializable
import java.util.*

class AllEmojiListResponse : Serializable {
    var status: Status? = null

    override fun toString(): String {
        return "ClassPojo [status = $status]"
    }

    inner class Status : Serializable {
        var emojis: Emojis? = null
        var success: String? = null
        var message: String? = null
        var timestamp: String? = null

        override fun toString(): String {
            return "ClassPojo [emojis = $emojis, success = $success, message = $message, timestamp = $timestamp]"
        }
    }

    inner class Emojis : Serializable {
        var deleted: Array<String>? = null
        var insert: ArrayList<Insert>? = null

        override fun toString(): String {
            return "ClassPojo [deleted = $deleted, insert = $insert]"
        }
    }

    inner class Insert : Serializable {
        var is_gender_available: String? = null
        var image: String? = null
        var img_f_light: String? = null
        var img_f_dark: String? = null
        var created_at: String? = null
        var title: String? = null
        var text_limit: String? = null
        var is_studiomode: String? = null
        var img_m_dark: String? = null
        var category_id: String? = null
        var updated_at: String? = null
        var img_m_light: String? = null
        var id: String? = null
        var position: String? = null
        var img_m_medium: String? = null
        var is_publish: String? = null
        var img_f_medium: String? = null

        override fun toString(): String {
            return "ClassPojo [is_gender_available = $is_gender_available, image = $image, img_f_light = $img_f_light, img_f_dark = $img_f_dark, created_at = $created_at, title = $title, text_limit = $text_limit, is_studiomode = $is_studiomode, img_m_dark = $img_m_dark, category_id = $category_id, updated_at = $updated_at, img_m_light = $img_m_light, id = $id, position = $position, img_m_medium = $img_m_medium, is_publish = $is_publish, img_f_medium = $img_f_medium]"
        }
    }
}