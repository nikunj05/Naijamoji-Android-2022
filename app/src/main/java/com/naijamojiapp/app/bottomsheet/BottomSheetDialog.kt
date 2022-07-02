package com.naijamojiapp.app.bottomsheet

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.naijamojiapp.R
import com.naijamojiapp.app.interfaces.ResultCallBack
import com.naijamojiapp.app.utils.Constants


class BottomSheetDialog : BottomSheetDialogFragment(), View.OnClickListener {
    private var tvCamera: TextView? = null
    private var tvGallery: TextView? = null
    private var tvCancel: TextView? = null
    companion object {
        private val ARGS_LAYOUT_FILE_URL = BottomSheetDialog::class.java.name + ".layoutFileName"
        internal lateinit var resultCallBack: ResultCallBack
        fun newInstance(resultCallB: ResultCallBack): BottomSheetDialog {
            val fragment = BottomSheetDialog()
            resultCallBack = resultCallB
            return fragment
        }
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView = View.inflate(context, R.layout.bottom_signup_dialog, null)
        tvCamera = contentView.findViewById(R.id.tv_camera)
        tvGallery = contentView.findViewById(R.id.tv_library)
        tvCancel = contentView.findViewById(R.id.tv_cancle)
        tvCamera!!.setOnClickListener(this)
        tvGallery!!.setOnClickListener(this)
        tvCancel!!.setOnClickListener(this)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(resources.getColor(android.R.color.transparent))
    }

    override fun onClick(view: View) {
        when (view.id) {

            R.id.tv_library -> {
                resultCallBack.onActivityResultMine(Constants.RECENT_CONTENT_REPORT_IMAGE, Activity.RESULT_OK, null)
                dismiss()
            }

            R.id.tv_camera -> {
                resultCallBack.onActivityResultMine(Constants.RECENT_CONTENT_REPORT_VIDEO, Activity.RESULT_OK, null)
                dismiss()
            }

            R.id.tv_cancle -> {
                resultCallBack.onActivityResultMine(Constants.RECENT_CONTENT_REPORT_CANCEL, Activity.RESULT_OK, null)
                dismiss()
            }
        }
    }


}
