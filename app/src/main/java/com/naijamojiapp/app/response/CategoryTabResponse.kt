package com.naijamojiapp.app.response

import java.io.Serializable
import java.util.ArrayList

class CategoryTabResponse : Serializable {

    var status: Status? = null

    override fun toString(): String {
        return "ClassPojo [status = $status]"
    }

    inner class Status : Serializable {
        var success: String? = null

        val categories: ArrayList<Categories>? = null

        var message: String? = null

        override fun toString(): String {
            return "ClassPojo [success = $success, categories = $categories, message = $message]"
        }
    }

    inner class Categories : Serializable {
        var category_image: String? = null

        var category_name: String? = null

        var category_id: String? = null

        override fun toString(): String {
            return "ClassPojo [category_image = $category_image, category_name = $category_name, category_id = $category_id]"
        }
    }

}
