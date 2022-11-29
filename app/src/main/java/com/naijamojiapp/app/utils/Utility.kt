package com.naijamojiapp.app.utils

import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.InputFilter
import android.text.Spanned
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import com.facebook.messenger.MessengerUtils
import com.naijamojiapp.R
import com.naijamojiapp.ime.latin.RichInputConnection
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class Utility {
    var address = ""

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    fun simpleDateConvertDob(mDate: String): String {
        val mInputDate = SimpleDateFormat("dd/MM/yyyy")
        return try {
            val date = mInputDate.parse(mDate)
            val mConvertedDate = SimpleDateFormat("dd-MMM-yyyy")
            mConvertedDate.format(date)
        } catch (e: Exception) {
            mDate
        }
    }

    fun mDatePickerDateformatChange(mDate: String): String {
        val mInputDate = SimpleDateFormat("dd/MM/yyyy")
        return try {
            val date = mInputDate.parse(mDate)
            val mConvertedDate = SimpleDateFormat("dd/MM/yyyy")
            mConvertedDate.format(date)
        } catch (e: Exception) {
            mDate
        }
    }


    fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            val runningProcesses = am.runningAppProcesses
            for (processInfo in runningProcesses) {
                if (processInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        if (activeProcess == context.packageName) {
                            isInBackground = false
                        }
                    }
                }
            }
        } else {
            val taskInfo = am.getRunningTasks(1)
            val componentInfo = taskInfo[0].topActivity
            if (componentInfo!!.packageName == context.packageName) {
                isInBackground = false
            }
        }
        return isInBackground
    }

    val date: String
        get() {
            val c = Calendar.getInstance()
            val df = SimpleDateFormat("yyyy-MM-dd")
            return df.format(c.time)
        }

    fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }


    enum class TimeDifference(val timeDifference: Int) {
        SECOND(0), MINUTE(1), HOUR(2), DAY(3), MONTH(4), YEAR(5);

    }

    companion object {
        private val AUTHORITY = "com.naijamojiapp.inputcontent"
        var INSTANCE: Utility? = null
        const val TIME_SERVER = "time-a.nist.gov"
        val instance: Utility?
            get() {
                if (INSTANCE == null) INSTANCE = Utility()
                return INSTANCE
            }

        fun current(dateInParse: String?): String {
            val sdf = SimpleDateFormat("yyy-MM-dd HH:mm")
            var date: Date? = null
            try {
                date = sdf.parse(dateInParse)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            val formatter = SimpleDateFormat("EEEE HH:mm")
            return formatter.format(date)
        }

        @JvmStatic
        fun decodeFile(filePath: String?): Bitmap { // Decode image size
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            BitmapFactory.decodeFile(filePath, o)
            // The new size we want to scale to
            val REQUIRED_SIZE = 1024
            // Find the correct scale value. It should be the power of 2.
            var width_tmp = o.outWidth
            var height_tmp = o.outHeight
            var scale = 1
            while (true) {
                if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) break
                width_tmp /= 2
                height_tmp /= 2
                scale *= 2
            }
            // Decode with inSampleSize
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            val b1 = BitmapFactory.decodeFile(filePath, o2)
            return ExifUtils.rotateBitmap(filePath, b1)
            // image.setImageBitmap(bitmap);
        }

        @JvmStatic
        fun getPath(activity: Activity, uri: Uri?): String? {
            val projection = arrayOf(MediaStore.Video.Media.DATA)
            val cursor = activity.managedQuery(uri, projection, null, null, null)
            return if (cursor != null) {
                val column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                cursor.moveToFirst()
                cursor.getString(column_index)
            } else null
        }

        fun bitmapToFile(bitmapp: Bitmap, context: Context): File {
            var filepath = File(context.filesDir, "images_send")
            if (!filepath.exists()) {
                filepath.mkdirs()
            }
            var file = File(filepath.path, "images_send.webp")

            var bitmap: Bitmap? = null
            bitmap = bitmapp
            var quality = 100
            var stream: FileOutputStream? = null
            //bitmap = makeTransparent(bitmapp)
            if (RichInputConnection.getPackageName() != null && RichInputConnection.getPackageName().equals("com.hike.chat.stickers")) {
                try {
                    stream = FileOutputStream(file)

                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                val bitmapnew = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
                val canvas = Canvas(bitmapnew)
                canvas.drawColor(Color.WHITE)
                canvas.drawBitmap(bitmap, 0f, 0f, null)
                var byteArrayLength = 100000
                var bos: ByteArrayOutputStream? = null
                while (byteArrayLength / 1000 >= 100) {
                    bos = ByteArrayOutputStream()
                    bitmapnew.compress(Bitmap.CompressFormat.WEBP,
                            quality,
                            bos)
                    byteArrayLength = bos.toByteArray().size
                }
                try {
                    stream!!.write(bos!!.toByteArray())
                    stream.flush()
                    stream.close()
                    return file
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                try {
                    stream = FileOutputStream(file)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                bitmap = Bitmap.createScaledBitmap(bitmap, 512, 512, true)
                var byteArrayLength = 100000
                var bos: ByteArrayOutputStream? = null
                while (byteArrayLength / 1000 >= 100) {
                    bos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.WEBP,
                            quality,
                            bos)
                    byteArrayLength = bos.toByteArray().size
                }
                try {
                    stream!!.write(bos!!.toByteArray())
                    stream.flush()
                    stream.close()
                    return file
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }


            return file
        }

        fun makeTransparent(bit: Bitmap): Bitmap {
            val width = bit.width
            val height = bit.height
            val myBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val allpixels = IntArray(myBitmap.height * myBitmap.width)
            bit.getPixels(allpixels, 0, myBitmap.width, 0, 0, myBitmap.width, myBitmap.height)
            myBitmap.setPixels(allpixels, 0, width, 0, 0, width, height)
            for (i in 0 until myBitmap.height * myBitmap.width) {
                if (allpixels[i] == Color.WHITE)
                    allpixels[i] = Color.alpha(Color.TRANSPARENT)
            }
            myBitmap.setPixels(allpixels, 0, myBitmap.width, 0, 0, myBitmap.width, myBitmap.height)
            return myBitmap
        }

        //Share normal image
        fun share(mParent: Context, packageName: String, sendImage: Bitmap) {
            val file = File(mParent.externalCacheDir, "share.png")
            val fOut = FileOutputStream(file)

            val newBitmap = Bitmap.createBitmap(sendImage.width, sendImage.height, sendImage.config)
            val canvas = Canvas(newBitmap)
            canvas.drawColor(Color.WHITE)
            canvas.drawBitmap(sendImage, 0f, 0f, null)
//            newBitmap = Bitmap.createScaledBitmap(newBitmap, 512, 512, true)
            newBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
            fOut.flush()
            fOut.close()
            file.setReadable(true, false)
            val bitmapPath: String = MediaStore.Images.Media.insertImage(mParent.contentResolver, newBitmap, "title", null)
            val sendImageUrl = Uri.parse(bitmapPath)
            if (packageName.equals("com.whatsapp")) {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_STREAM, sendImageUrl)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.setClassName("com.whatsapp", "com.whatsapp.ContactPicker")
                intent.setPackage("com.whatsapp")
                mParent.startActivity(intent)
            } else if (packageName.equals(MessengerUtils.PACKAGE_NAME)) {
                val intent2 = Intent()
                intent2.action = Intent.ACTION_SEND
                try {
                    intent2.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent2.type = "image/png"
                    intent2.setPackage(MessengerUtils.PACKAGE_NAME)
                    intent2.putExtra(Intent.EXTRA_STREAM, sendImageUrl)
                    mParent.startActivity(intent2)
                } catch (e: Exception) {
                }
            } else if (packageName.equals("com.facebook.katana")) {
                val intent3 = Intent()
                intent3.action = Intent.ACTION_SEND
                try {
                    intent3.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent3.type = "image/png"
                    intent3.setPackage("com.facebook.katana")
                    intent3.putExtra(Intent.EXTRA_STREAM, sendImageUrl)
                    mParent.startActivity(intent3)
                } catch (e2: Exception) {
                    e2.printStackTrace()
                }
            } else if (packageName.equals("com.google.android.talk")) {
                val intent4 = Intent(Intent.ACTION_SEND)
                intent4.type = "image/png"
                intent4.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent4.setPackage("com.google.android.talk")
                intent4.putExtra(Intent.EXTRA_STREAM, sendImageUrl)
                mParent.startActivity(intent4)
            } else if (packageName.equals("com.tencent.mm")) {
                val intent5 = Intent()
                intent5.action = Intent.ACTION_SEND
                intent5.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent5.type = "image/png"
                intent5.setPackage("com.tencent.mm")
                intent5.putExtra(Intent.EXTRA_STREAM, sendImageUrl)
                mParent.startActivity(intent5)
            } else if (packageName.equals("com.viber.voip")) {
                val intent6 = Intent()
                intent6.action = Intent.ACTION_SEND
                intent6.type = "image/png"
                intent6.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent6.setPackage("com.viber.voip")
                intent6.putExtra(Intent.EXTRA_STREAM, sendImageUrl)
                mParent.startActivity(intent6)
            } else if (packageName.equals("jp.naver.line.android")) {
                val intent7 = Intent()
                intent7.action = Intent.ACTION_SEND
                intent7.type = "image/png"
                intent7.setPackage("jp.naver.line.android")
                intent7.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent7.putExtra(Intent.EXTRA_STREAM, sendImageUrl)
                mParent.startActivity(intent7)
            } else if (packageName.equals("com.bsb.hike")) {
                val intent8 = Intent()
                intent8.action = Intent.ACTION_SEND
                intent8.type = "image/png"
                intent8.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent8.setPackage("com.bsb.hike")
                intent8.putExtra(Intent.EXTRA_STREAM, sendImageUrl)
                mParent.startActivity(intent8)
            } else if (packageName.equals("com.bbm")) {
                val intent9 = Intent()
                intent9.action = Intent.ACTION_SEND
                intent9.type = "image/png"
                intent9.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent9.setPackage("com.bbm")
                intent9.putExtra(Intent.EXTRA_STREAM, sendImageUrl)
                mParent.startActivity(intent9)
            } else {
                val intent10 = Intent(Intent.ACTION_SEND)
                val packageManager = mParent.packageManager
                intent10.type = "image/*"
                val queryIntentActivities = packageManager.queryIntentActivities(intent10, 0)
                for (i in 0 until queryIntentActivities.size) {
                    val activityInfo = (queryIntentActivities.get(i) as ResolveInfo).activityInfo
                    if (ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name).packageName.matches(packageName.toRegex())) {
                        val intent11 = Intent()
                        intent11.action = Intent.ACTION_SEND
                        intent11.type = "image/png"
                        intent11.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        intent11.setPackage(packageName)
                        intent11.putExtra(Intent.EXTRA_STREAM, sendImageUrl)
                        mParent.startActivity(intent11)
                    }
                }
            }
        }



        fun bitmapToFileNormalImage(bitmapp: Bitmap, context: Context): File {
            var filepath = File(context.filesDir, "images_send")
            if (!filepath.exists()) {
                filepath.mkdirs()
            }
            var file = File(filepath.path, "images_send.png")

            var bitmap: Bitmap? = null
            if (RichInputConnection.getPackageName() != null && RichInputConnection.getPackageName().equals("com.hike.chat.stickers")) {
                bitmap = bitmapp
            } else {
                bitmap = makeTransparent(bitmapp)
            }
            var quality = 100
            var stream: FileOutputStream? = null
            try {
                stream = FileOutputStream(file)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            var byteArrayLength = 100000
            var bos: ByteArrayOutputStream? = null
            while (byteArrayLength / 1000 >= 100) {
                bos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG,
                        quality,
                        bos)
                byteArrayLength = bos.toByteArray().size
                quality -= 10
            }
            try {
                stream!!.write(bos!!.toByteArray())
                stream.flush()
                stream.close()
                return file
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return file
        }
    }

    fun isValidPassword(password: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#\$%^&*+=?-])(?=\\S+$).{4,}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }

    fun isImmersiveAvailable(): Boolean {
        if(Build.VERSION.SDK_INT >= 19){
            return true
        }
        return false
    }

fun setTopBar(context: Context,window : Window)  {
    window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    window.statusBarColor = ContextCompat.getColor(context, R.color.status_bar_color)
    window.navigationBarColor = ContextCompat.getColor(context, R.color.status_bar_color)
}

}