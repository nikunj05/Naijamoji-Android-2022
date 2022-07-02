package com.naijamojiapp.app.utils

import android.content.Context
import android.content.SharedPreferences
import com.naijamojiapp.app.Naijamoji


 class Preferences {
    val PREF_USER_ID = "pref_user_id"
    val PREF_FIREST_NAME = "pref_user_first_name"
    val PREF_LAST_NAME = "pref_user_last_name"
    val PREF_USER_EMAIL = "pref_user_email"
    val PREF_USER_GENDER = "pref_user_gender"
    val PREF_USER_SKINTONE = "pref_user_skintone"
    val PREF_USER_PROFILE_PIC = "pref_user_profile_pic"
    val PREF_ISLOGIN = "pref_user_islogin"
    val PREF_mCharacterLimit = "pref_Characterlimit"
    val PREF_SIGNIN_OBJ = "pref_user_signin"
    val PREF_DIVICEID = "device_id"//respose
    val PREF_DEVICE_TOKEN = "pref_device_token"//respose
    val LOGIN_TYPE = "pref_login_type"//respose
    val TOKEN = "pref_token"//respose
    val notificationNo = "notificationNo"
    //timestamp
     val PREF_CAT_TIMESTAMP = "pref_cat_timestamp"
     val PREF_AllSTICKERS_TIMESTAMP = "pref_allstickers_timestamp"
     val PREF_TAG_TIMESTAMP = "pref_tag_timestamp"



    private val PREFERENCE_FILE = "naijamoji_preferences"
    private val mPrefs: SharedPreferences
    private val mEdit: SharedPreferences.Editor

    init {
        val app = Naijamoji().getInstance()
        mPrefs = app!!.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE)
        mEdit = mPrefs.edit()
    }

    val userId: String
        get() = INSTANCE?.mPrefs!!.getString(PREF_USER_ID, "")!!

    val preToken: String
        get() = INSTANCE?.mPrefs!!.getString(PREF_DEVICE_TOKEN, "")!!

    val deviceID: String
        get() = INSTANCE?.mPrefs!!.getString(PREF_DIVICEID, "")!!

     val token: String
        get() = INSTANCE?.mPrefs!!.getString(TOKEN, "")!!

    val loginType: String
        get() = INSTANCE?.mPrefs!!.getString(LOGIN_TYPE, "")!!

     val characterLimit: String
         get() = INSTANCE?.mPrefs!!.getString(PREF_mCharacterLimit, "")!!

     val getGender: String
         get() = INSTANCE?.mPrefs!!.getString(PREF_USER_GENDER, "")!!

     val getSkinTone: String
         get() = INSTANCE?.mPrefs!!.getString(PREF_USER_SKINTONE, "")!!

     //Timestamp Local
     val getCatTimeStamp: String
         get() = INSTANCE?.mPrefs!!.getString(PREF_CAT_TIMESTAMP, "0")!!

     val getAllStickersTimeStamp: String
         get() = INSTANCE?.mPrefs!!.getString(PREF_AllSTICKERS_TIMESTAMP, "0")!!

     val getTagTimeStamp: String
         get() = INSTANCE?.mPrefs!!.getString(PREF_TAG_TIMESTAMP, "0")!!


    var loginStatus: Boolean
        get() = mPrefs.getBoolean(PREF_ISLOGIN, false)
        set(isLogin) {
            mEdit.putBoolean(PREF_ISLOGIN, isLogin)
            save()
        }

    val getNotificationNo: Int?
        get() = INSTANCE?.mPrefs?.getInt(notificationNo, 0)


    fun SavePrefValueInt(Key: String, value: Int) {
        mEdit.putInt(Key, value)
        save()
    }

    fun contains(key: String): Boolean {
        return mPrefs.contains(key)
    }


    fun SavePrefValue(Key: String?, value: String?) {
        mEdit.putString(Key, value)
        save()
    }

    fun getPrefBoolean(key: String): Boolean {
        return mPrefs.getBoolean(key, false)
    }

    fun SavePrefBoolean(Key: String, value: Boolean) {
        mEdit.putBoolean(Key, value)
        save()
    }

    // Save content
    private fun save() {
        mEdit.apply()
    }

    fun ClearPrefsValue() {
        val sp = Preferences.INSTANCE?.mPrefs
        val editor = sp?.edit()
        editor?.clear()
        editor?.commit()
    }


    //Get Login Object
    /*public LoginResponse getLoginObject() {
        String mReturnObj = mPrefs.getString(PREF_SIGNIN_OBJ, "");
        if (!mReturnObj.equalsIgnoreCase("")) {
            Gson ObjGson = new Gson();
            return ObjGson.fromJson(mReturnObj, LoginResponse.class);
        } else {
            return null;
        }
    }*/

    //Set Login Object
    fun setLoginObject(ObjSocialLogin: String) {
        mEdit.putString(PREF_SIGNIN_OBJ, ObjSocialLogin)
        save()
    }

    companion object {
        var INSTANCE: Preferences? = Preferences()
    }


}
