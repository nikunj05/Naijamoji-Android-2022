package com.naijamojiapp.app.response.newResponse

import java.io.Serializable
import java.util.*

class CatListTabResponse : Serializable {
    var status: Status? = null

    override fun toString(): String {
        return "ClassPojo [status = $status]"
    }

    inner class Status : Serializable {
        var success: String? = null
        var categories: Categories? = null
        var message: String? = null
        var timestamp: String? = null

        override fun toString(): String {
            return "ClassPojo [success = $success, categories = $categories, message = $message, timestamp = $timestamp]"
        }
    }

    inner class Categories : Serializable {

        val deleted: Array<String> ?= null
        var insert: ArrayList<Insert>? = null

        override fun toString(): String {
            return "ClassPojo [deleted = $deleted, insert = $insert]"
        }
    }

    inner class Insert : Serializable {
        var image: String? = null
        var updated_at: String? = null
        var name: String? = null
        var created_at: String? = null
        var id: String? = null
        var position: String? = null

        override fun toString(): String {
            return "ClassPojo [image = $image, updated_at = $updated_at, name = $name, created_at = $created_at, id = $id, position = $position]"
        }
    }
}