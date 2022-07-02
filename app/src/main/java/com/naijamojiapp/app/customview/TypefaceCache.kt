package com.naijamojiapp.app.customview

import android.content.res.AssetManager
import android.graphics.Typeface

import java.util.Hashtable

object TypefaceCache {

    private val FONT_LIGHT = "fonts/NunitoSansLight.ttf"
    private val FONT_REGULAR = "fonts/QuicksandRegular.ttf"
    private val FONT_MEDIUM = "fonts/QuicksandMedium.ttf"
    private val FONT_BOLD = "fonts/NunitoSansBold.ttf"
    private val FONT_COND = "fonts/NunitoSansItalic.ttf"

    private val CACHE = Hashtable<String, Typeface>()

    operator fun get(manager: AssetManager, typefaceCode: Int): Typeface? {
        synchronized(CACHE) {
            val typefaceName = getTypefaceName(typefaceCode)
            if (!CACHE.containsKey(typefaceName)) {
                val t = Typeface.createFromAsset(manager, typefaceName)
                CACHE.put(typefaceName, t)
            }
            return CACHE[typefaceName]
        }
    }

    fun getTypefaceName(typefaceCode: Int): String {
        var typefaceTemp = ""
        when (typefaceCode) {
            0 -> typefaceTemp = FONT_LIGHT
            1 -> typefaceTemp = FONT_REGULAR
            2 -> typefaceTemp = FONT_MEDIUM
            3 -> typefaceTemp = FONT_BOLD
            4 -> typefaceTemp = FONT_COND
            else -> typefaceTemp = FONT_REGULAR
        }
        return typefaceTemp
    }
}
