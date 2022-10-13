package com.naijamojiapp.app.utils

import android.os.Build
import android.os.Bundle

import java.util.Arrays

class Constants {

    companion object {
        val INSTANCE: Constants = Constants()
        const val RECENT_CONTENT_REPORT_IMAGE = 15
        const val RECENT_CONTENT_REPORT_VIDEO = 16
        const val RECENT_CONTENT_REPORT_CANCEL = 22
        const val RC_STORAGE_PERM = 101
    }

    // faceboook
    fun getPermissions(): List<String> {
        return Arrays.asList("email", "public_profile", "user_friends")
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return if (target == null) {
            false
        } else {
            android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    val RC_LOCATION_PERM = 102

    val message = "message"
    val one = "true"
    val zero = "false"
    val minusone = "-1"
    val title = "title"

    //login
    var strWS_email = "email"
    var strWS_password = "password"
    //Signup
    var str_first_name = "first_name"
    var str_f_name = "first_name"
    var str_last_name = "last_name"
    var str_email = "email"
    var str_password = "password"
    var str_gender = "gender"
    var str_category = "category"
    /*SocialLogin/SignUp*/
    var str_social_id = "social_id"
    var str_type = "type"
    // Edit and get Profile
    var str_skin_tone= "skin_tone"
    var str_sticker_url= "sticker_url"
    var str_emoji_id= "emoji_id"
    var str_register_via= "register_via"
    var str_dob = "birth_date"
    var str_login_type = "login_type"
    //RemoveYourStickers
    var str_id = "id"
    //ChangePassword
    var str_old_pass = "old_pass"
    var str_new_pass = "new_pass"
    //for search in keyboard
    var str_word = "word"
    var str_tag = "tag"
    var str_skintone = "skin_tone"
    var str_timestamp = "timestamp"



    var URLLOCAL = "https://coderscotch.com/naija/public/api/"
    //var GETLOCATIONURL = "https://maps.googleapis.com/maps/api/geocode/json"

    //Web services
    var strWS_login = "login"
    var strWS_signup = "register"
    var strWS_forgot_pass = "forgotpw"
    var strWS_social_insert = "social-insert"
    var strWS_get_profile = "get-profile"
    var strWS_edit_profile = "edit-profile"
    var strWS_emoji_listing = "emoji-listing"
    var strWS_tag_list = "cat-list"
    var strWS_emojis_search = "emojis-by-category"
    var strWS_emoji_list_category = "emoji-list-category"
    var strWS_studio_mode = "studio_mode"
    var strWS_delete_user = "delete-user"
    var strWS_upload_image = "upload-image"
    var strWS_check_social_account = "check-social-account"
    var strWS_emoji_usage = "emoji-usage"
    var strWS_custom_user_emoji= "custom-user-emoji"
    var strWS_delete_emoji= "delete-emoji"
    var strWS_change_password= "change-password"
    var strWS_search_tag= "search-tags"
    var strWS_emojis_by_tag= "emojis-by-tag"
    var strWS_update_emoji = "update-emoji"
    var strWS_tag_list_new = "tag-list"
    var strWS_emoji_list_tag = "emoji-list-tag"
    var strWS_delete_custom_user_emoji = "delete-custom-user-emoji"
    var strWS_check_email_exist = "check-email-exist"
    var strWS_random_emoji = "random-emoji"

}
