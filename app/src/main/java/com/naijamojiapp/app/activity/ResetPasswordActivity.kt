package com.naijamojiapp.app.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.naijamojiapp.R
import com.naijamojiapp.app.customview.CustomDialog
import com.naijamojiapp.app.customview.CustomProgressDialog
import com.naijamojiapp.app.response.CommonResponse
import com.naijamojiapp.app.utils.CheckConnection
import com.naijamojiapp.app.utils.Constants
import com.naijamojiapp.app.utils.Preferences
import com.naijamojiapp.app.utils.Utility
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity(), View.OnClickListener {
    var rlBack : RelativeLayout? = null
    var edtNewPassword : EditText? = null
    var edtConfirmPassword : EditText? = null
    var edtEmail : EditText? = null
    var tvResetPassword : TextView? = null
    var strEmail : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utility.instance!!.setTopBar(this,window)
        setContentView(R.layout.activity_reset_password)
        if (intent != null) {
            if (intent.extras != null) {
               // strEmail = intent.extras!!.getString("email")!!
            }
        }
        init()
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }
    private fun handleIntent(intent: Intent) {
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data
        if (Intent.ACTION_VIEW == appLinkAction) {
            appLinkData?.lastPathSegment?.also { recipeId ->
                Uri.parse("content://coderscotch.com/naija/")
                    .buildUpon()
                    .appendPath(recipeId)
                    .build().also { appData ->
                        Log.e("Print appData","===>"+appData);
                        //resetPassword(appData)
                    }
            }
        }
    }

    private fun resetPassword(appData: Uri?) {
        intent = Intent(this, ResetPasswordActivity::class.java)
        startActivity(intent)
    }
    private fun init() {
        rlBack = findViewById(R.id.rl_back)
        rlBack!!.setOnClickListener(this)
        edtNewPassword = findViewById(R.id.edtNewPassword)
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword)
        edtEmail = findViewById(R.id.et_email)
        tvResetPassword = findViewById(R.id.tv_reset_password)
        tvResetPassword!!.setOnClickListener(this)
        //edtEmail!!.setText(strEmail)
        edtEmail!!.isEnabled = false
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.rl_back -> {
                finish()
            }
            R.id.tv_reset_password -> {
                if (CheckConnection.getInstance(this).isConnectingToInternet) {
                    if (isValid()) {
                        callResetPasswordWS()
                    }
                } else {
                    CustomDialog.instance!!.showalert(this@ResetPasswordActivity, resources.getString(R.string.check_internet_connection))
                }
            }
        }
    }
    private fun isValid(): Boolean {
        if (TextUtils.isEmpty(edtEmail!!.getText().toString())) {
            CustomDialog.instance!!.showalert(this@ResetPasswordActivity, resources.getString(R.string.please_enter_email))
            return false
        } else {
            edtEmail!!.setError(null)
        }

        if (!Constants.INSTANCE.isValidEmail(edtEmail!!.getText().toString())) {
            CustomDialog.instance!!.showalert(this@ResetPasswordActivity, resources.getString(R.string.please_enter_valid_email))
            return false
        } else {
            edtEmail!!.setError(null)
        }

        if (TextUtils.isEmpty(edtNewPassword!!.text.toString())) {
            CustomDialog.instance!!.showalert(this@ResetPasswordActivity, resources.getString(R.string.please_enter_newPass))
            return false
        } else {
            edtNewPassword!!.error = null
        }

        if (edtNewPassword!!.text.toString().length < 8) {
            CustomDialog.instance!!.showalert(this@ResetPasswordActivity, resources.getString(R.string.please_enter_valid_newpass))
            return false
        } else {
            edtNewPassword!!.error = null
        }

        if (!Utility.instance!!.isValidPassword(edtNewPassword!!.text.toString())) {
            CustomDialog.instance!!.showalert(this@ResetPasswordActivity, resources.getString(R.string.please_enter_valid_newpass))
            return false
        }

        if (TextUtils.isEmpty(edtConfirmPassword!!.text.toString())) {
            CustomDialog.instance!!.showalert(this@ResetPasswordActivity, resources.getString(R.string.please_enter_confirmPass))
            return false
        } else {
            edtConfirmPassword!!.error = null
        }

        if (!edtNewPassword!!.text.toString().equals(edtConfirmPassword!!.text.toString())) {
            CustomDialog.instance!!.showalert(this@ResetPasswordActivity, resources.getString(R.string.please_enter_valid_newandConfirm))
            return false
        }
        return true
    }
    private fun callResetPasswordWS() {
        val progressDialog = CustomProgressDialog(this@ResetPasswordActivity, R.style.progress_dialog_text_style)
        progressDialog.show()

        AndroidNetworking.post(Constants.INSTANCE.URLLOCAL + Constants.INSTANCE.strWS_change_password)
            .addBodyParameter(Constants.INSTANCE.str_email, edtEmail!!.text.toString())
            .addBodyParameter(Constants.INSTANCE.str_new_pass, edtConfirmPassword!!.text.toString())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsObject(CommonResponse::class.java, object :
                ParsedRequestListener<CommonResponse> {
                override fun onResponse(response: CommonResponse?) {
                    Log.i("response", response.toString())
                    if (progressDialog.isShowing)
                        progressDialog.dismiss()
                    var mSuccess = false
                    if (response != null)
                        if (response.status!!.success == (Constants.INSTANCE.one)) {
                            mSuccess = true
                        }
                    if (mSuccess) {
                        CustomDialog.instance!!.showalert(this@ResetPasswordActivity, response!!.status!!.message!!)
                        goToLoginScreen()
                    } else {
                        try {
                            CustomDialog.instance!!.showalert(this@ResetPasswordActivity, response!!.status!!.message!!)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            CustomDialog.instance!!.showalert(this@ResetPasswordActivity, response!!.status!!.message!!)
                        }
                    }
                }
                override fun onError(anError: ANError) {
                    Log.i("error", anError.toString())
                    CustomDialog.instance!!.showalert(this@ResetPasswordActivity, resources.getString(R.string.old_pass_notvalid))
                    if (progressDialog != null && progressDialog.isShowing)
                        progressDialog.dismiss()

                }
            })

    }
    private fun goToLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        Preferences.INSTANCE!!.ClearPrefsValue()
        startActivity(intent)
    }
}