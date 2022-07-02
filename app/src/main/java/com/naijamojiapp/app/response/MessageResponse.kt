package com.naijamojiapp.app.response

import com.google.gson.annotations.Expose
import java.io.Serializable
import com.google.gson.annotations.SerializedName



class MessageResponse : Serializable, LoginResponse() {

    private var social_id: Array<String>? = null

    private var last_name: Array<String>? = null

    private var type: Array<String>? = null

    private var first_name: Array<String>? = null

    fun getSocial_id(): Array<String>? {
        return social_id
    }

    fun setSocial_id(social_id: Array<String>) {
        this.social_id = social_id
    }

    fun getLast_name(): Array<String>? {
        return last_name
    }

    fun setLast_name(last_name: Array<String>) {
        this.last_name = last_name
    }

    fun getType(): Array<String>? {
        return type
    }

    fun setType(type: Array<String>) {
        this.type = type
    }

    fun getFirst_name(): Array<String>? {
        return first_name
    }

    fun setFirst_name(first_name: Array<String>) {
        this.first_name = first_name
    }

    override fun toString(): String {
        return "ClassPojo [social_id = $social_id, last_name = $last_name, type = $type, first_name = $first_name]"
    }
}
