package com.naijamojiapp.app.customview

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.naijamojiapp.R

class CustomDialog {

    companion object {
        var instance: CustomDialog? = null
            private set
        init {
            instance = CustomDialog()
        }
    }

    fun showalert(context: Context, message: String) {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.custom_toast, null)
        val text = layout.findViewById<TextView>(R.id.tvmessage)
        text.setText(message)
        val toast = Toast(context)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }


/*    fun LogoutDialog(context: Context, dissmissListener: DismissListenerWithStatus?): Dialog {

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.logout_dialog)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        val tvno = dialog.findViewById(R.id.dialog_no)
        val tvyes = dialog.findViewById(R.id.dialog_yes)

        tvno.setOnClickListener(View.OnClickListener {
            if (dissmissListener != null) {
                dialog.dismiss()
                dissmissListener.onDismissed("0")
            }
        })
        tvyes.setOnClickListener(View.OnClickListener {
            if (dissmissListener != null) {
                dialog.dismiss()
                dissmissListener.onDismissed("1")
            }
        })

        dialog.show()
        return dialog
    }

    fun BlockDialog(context: Context, dissmissListener: DismissListenerWithStatus?): Dialog {

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.delete_dialog)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        val tvno = dialog.findViewById(R.id.block_dialog_no)
        val tvyes = dialog.findViewById(R.id.block_dialog_yes)

        tvno.setOnClickListener(View.OnClickListener {
            if (dissmissListener != null) {
                dialog.dismiss()
                dissmissListener.onDismissed("0")
            }
        })
        tvyes.setOnClickListener(View.OnClickListener {
            if (dissmissListener != null) {
                dialog.dismiss()
                dissmissListener.onDismissed("1")
            }
        })
        dialog.show()
        return dialog
    }

    interface DismissListenerWithStatus {
        fun onDismissed(message: String)
    }*/

}
