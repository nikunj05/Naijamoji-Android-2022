/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.naijamojiapp.ime.keyboard

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.IBinder
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.naijamojiapp.R
import com.naijamojiapp.app.activity.HomeActivity
import com.naijamojiapp.app.activity.LoginActivity
import com.naijamojiapp.app.interfaces.CatListTabClicked
import com.naijamojiapp.app.response.CategoryTabResponse
import com.naijamojiapp.app.response.SearchKeyboardTagListResponse
import com.naijamojiapp.app.response.newResponse.StickerListByTagOrCategory
import com.naijamojiapp.app.roomDB.AppDatabase
import com.naijamojiapp.app.roomDB.entity.AllStickerList
import com.naijamojiapp.app.roomDB.entity.CatListTab
import com.naijamojiapp.app.roomDB.entity.TagList
import com.naijamojiapp.app.utils.Constants
import com.naijamojiapp.app.utils.Preferences
import com.naijamojiapp.app.utils.Utility
import com.naijamojiapp.ime.compat.InputMethodServiceCompatUtils
import com.naijamojiapp.ime.event.Event
import com.naijamojiapp.ime.keyboard.KeyboardLayoutSet.KeyboardLayoutSetException
import com.naijamojiapp.ime.keyboard.KeyboardSearchView.ExtensionManager
import com.naijamojiapp.ime.keyboard.KeyboardSearchView.SearchView
import com.naijamojiapp.ime.keyboard.KeyboardSearchView.SearchView.TouchActionListener
import com.naijamojiapp.ime.keyboard.KeyboardStickerView.*
import com.naijamojiapp.ime.keyboard.KeyboardStickerView.adapter.CatTabKeyboardAdapter
import com.naijamojiapp.ime.keyboard.emoji.EmojiPalettesView
import com.naijamojiapp.ime.keyboard.internal.KeyboardState
import com.naijamojiapp.ime.keyboard.internal.KeyboardState.SwitchActions
import com.naijamojiapp.ime.keyboard.internal.KeyboardTextsSet
import com.naijamojiapp.ime.latin.*
import com.naijamojiapp.ime.latin.define.ProductionFlags
import com.naijamojiapp.ime.latin.settings.Settings
import com.naijamojiapp.ime.latin.settings.SettingsValues
import com.naijamojiapp.ime.latin.utils.DialogUtils
import com.naijamojiapp.ime.latin.utils.LanguageOnSpacebarUtils
import com.naijamojiapp.ime.latin.utils.ResourceUtils
import com.naijamojiapp.ime.latin.utils.ScriptUtils
import java.util.*
import javax.annotation.Nonnull

class KeyboardSwitcher : SwitchActions, SendStickersInterface, ClickonTagInterface, CatListTabClicked, SendDaynamicStickersInterface {
    private var rvStickerGrid: RecyclerView? = null
    private var cardlistAdapter: KbStickersAdapter? = null
    private var mLinearLayoutManager: GridLayoutManager? = null
    private var mEmojiList: ArrayList<AllStickerList>? = null

    var simpleSeekBar: SeekBar? = null
    var rlSeekbar: RelativeLayout? = null
    var ivPlus: ImageView? = null
    var ivMinus: ImageView? = null
    var mContext: Context? = null
    var rlOne: RelativeLayout? = null
    var rlTwo: RelativeLayout? = null
    var progressChangedValue = 0
    var allTabsParantView: View? = null

    //StickerTag
    var rvStickerTag: RecyclerView? = null

    private var mSearchTagList: ArrayList<SearchKeyboardTagListResponse.Result>? = null
    private var mStickersTagAdapter: StickerTagKeyboardAdapter? = null
    private var mRelativelayoutForTagView: RelativelayoutForTagView? = null

    //NoInternetConnection
    var rlLogin: RelativeLayout? = null
    var linearSignin: LinearLayout? = null
    var blockerCloseButton: ImageView? = null
    var btnSignin: Button? = null

    private var mCurrentInputView: InputView? = null
    private var mMainKeyboardFrame: View? = null
    var mainKeyboardView: MainKeyboardView? = null

    //        private set
    private var mEmojiPalettesView: EmojiPalettesView? = null
    private var mLatinIME: LatinIME? = null
    private var mRichImm: RichInputMethodManager? = null
    private var mIsHardwareAcceleratedDrawingEnabled = false
    var mSickersView: RelativeLayout? = null
    var searchBar: SearchView? = null
    private var llMain: LinearLayout? = null
    private var mState: KeyboardState? = null
    private var mKeyboardLayoutSet: KeyboardLayoutSet? = null
    private val mKeyboardTextsSet = KeyboardTextsSet()
    private var mKeyboardTheme: KeyboardTheme? = null
    private var mThemeContext: Context? = null
    private var mExtensionManager: ExtensionManager? = null
    private var mDaynamicEmojilist: ArrayList<StickerListByTagOrCategory.Result>? = null

    //Added new
    private var slideUpAnimation: Animation? = null
    private var slideDownAnimation: Animation? = null
    private var mTabSearch: ImageView? = null

    /*  private var mTab0: ImageView? = null
      private var mTab1: ImageView? = null
      private var mTab2: ImageView? = null
      private var mTab3: ImageView? = null
      private var mTab4: ImageView? = null
      private var mTab5: ImageView? = null
      private var mTab6: ImageView? = null*/
    private var mTabDisplayKbView: ImageView? = null
    private val mEdtSearch: EditText? = null

    //TabEmojiDaynamic
    private var rvCatListTab: RecyclerView? = null
    private var mEmojiTabKeyboardAdapter: CatTabKeyboardAdapter? = null
    private var mCategoryTagList: ArrayList<CategoryTabResponse.Categories>? = null

    var isDaynamic: Boolean = false
    var mDatabase: AppDatabase? = null
    private fun initInternal(latinIme: LatinIME) {
        mLatinIME = latinIme
        mRichImm = RichInputMethodManager.getInstance()
        mState = KeyboardState(this)
        mIsHardwareAcceleratedDrawingEnabled = InputMethodServiceCompatUtils.enableHardwareAcceleration(mLatinIME)
    }

    fun updateKeyboardTheme() {
        val themeUpdated = updateKeyboardThemeAndContextThemeWrapper(mLatinIME, KeyboardTheme.getKeyboardTheme(mLatinIME))
        if (themeUpdated && mainKeyboardView != null) {
            mLatinIME!!.setInputView(onCreateInputView(mIsHardwareAcceleratedDrawingEnabled))
        }
    }

    private fun updateKeyboardThemeAndContextThemeWrapper(context: Context?,
                                                          keyboardTheme: KeyboardTheme): Boolean {
        if (mThemeContext == null || keyboardTheme != mKeyboardTheme) {
            mKeyboardTheme = keyboardTheme
            mThemeContext = ContextThemeWrapper(context, keyboardTheme.mStyleId)
            KeyboardLayoutSet.onKeyboardThemeChanged()
            return true
        }
        return false
    }

    fun loadKeyboard(editorInfo: EditorInfo?, settingsValues: SettingsValues,
                     currentAutoCapsState: Int, currentRecapitalizeState: Int) {
        val builder = KeyboardLayoutSet.Builder(mThemeContext, editorInfo)
        val res = mThemeContext!!.resources
        val keyboardWidth = ResourceUtils.getDefaultKeyboardWidth(res)
        val keyboardHeight = ResourceUtils.getKeyboardHeight(res, settingsValues)
        builder.setKeyboardGeometry(keyboardWidth, keyboardHeight)
        builder.setSubtype(mRichImm!!.currentSubtype)
        builder.setVoiceInputKeyEnabled(settingsValues.mShowsVoiceInputKey)
        builder.setLanguageSwitchKeyEnabled(mLatinIME!!.shouldShowLanguageSwitchKey())
        builder.setSplitLayoutEnabledByUser(ProductionFlags.IS_SPLIT_KEYBOARD_SUPPORTED && settingsValues.mIsSplitKeyboardEnabled)
        mKeyboardLayoutSet = builder.build()
        try {
            mState!!.onLoadKeyboard(currentAutoCapsState, currentRecapitalizeState)
            mKeyboardTextsSet.setLocale(mRichImm!!.currentSubtypeLocale, mThemeContext)
        } catch (e: KeyboardLayoutSetException) {
            Log.w(TAG, "loading keyboard failed: " + e.mKeyboardId, e.cause)
        }
    }

    fun saveKeyboardState() {
        if (keyboard != null || isShowingEmojiPalettes) {
            mState!!.onSaveKeyboardState()
        }
    }

    fun onHideWindow() {
        if (mainKeyboardView != null) {
            mainKeyboardView!!.onHideWindow()
        }
    }

    private fun setKeyboard(@Nonnull keyboardId: Int, @Nonnull toggleState: KeyboardSwitchState) { // Make {@link MainKeyboardView} visible and hide {@link EmojiPalettesView}.
        val currentSettingsValues = Settings.getInstance().current
        setMainKeyboardFrame(currentSettingsValues, toggleState)
        val keyboardView = mainKeyboardView
        val oldKeyboard = keyboardView!!.keyboard
        val newKeyboard = mKeyboardLayoutSet!!.getKeyboard(keyboardId)
        keyboardView.setKeyboard(newKeyboard)
        mCurrentInputView!!.setKeyboardTopPadding(newKeyboard.mTopPadding)
        keyboardView.setKeyPreviewPopupEnabled(currentSettingsValues.mKeyPreviewPopupOn, currentSettingsValues.mKeyPreviewPopupDismissDelay)
        keyboardView.setKeyPreviewAnimationParams(
                currentSettingsValues.mHasCustomKeyPreviewAnimationParams,
                currentSettingsValues.mKeyPreviewShowUpStartXScale,
                currentSettingsValues.mKeyPreviewShowUpStartYScale,
                currentSettingsValues.mKeyPreviewShowUpDuration,
                currentSettingsValues.mKeyPreviewDismissEndXScale,
                currentSettingsValues.mKeyPreviewDismissEndYScale,
                currentSettingsValues.mKeyPreviewDismissDuration)
        keyboardView.updateShortcutKey(mRichImm!!.isShortcutImeReady)
        val subtypeChanged = oldKeyboard == null || newKeyboard.mId.mSubtype != oldKeyboard.mId.mSubtype
        val languageOnSpacebarFormatType = LanguageOnSpacebarUtils.getLanguageOnSpacebarFormatType(newKeyboard.mId.mSubtype)
        val hasMultipleEnabledIMEsOrSubtypes = mRichImm!!.hasMultipleEnabledIMEsOrSubtypes(true /* shouldIncludeAuxiliarySubtypes */)
        keyboardView.startDisplayLanguageOnSpacebar(subtypeChanged, languageOnSpacebarFormatType, hasMultipleEnabledIMEsOrSubtypes)
    }

    val keyboard: Keyboard?
        get() = if (mainKeyboardView != null) {
            mainKeyboardView!!.keyboard
        } else null

    // TODO: Remove this method. Come up with a more comprehensive way to reset the keyboard layout
// when a keyboard layout set doesn't get reloaded in LatinIME.onStartInputViewInternal().
    fun resetKeyboardStateToAlphabet(currentAutoCapsState: Int,
                                     currentRecapitalizeState: Int) {
        mState!!.onResetKeyboardStateToAlphabet(currentAutoCapsState, currentRecapitalizeState)
    }

    fun onPressKey(code: Int, isSinglePointer: Boolean,
                   currentAutoCapsState: Int, currentRecapitalizeState: Int) {
        mState!!.onPressKey(code, isSinglePointer, currentAutoCapsState, currentRecapitalizeState)
    }

    fun onReleaseKey(code: Int, withSliding: Boolean,
                     currentAutoCapsState: Int, currentRecapitalizeState: Int) {
        mState!!.onReleaseKey(code, withSliding, currentAutoCapsState, currentRecapitalizeState)
    }

    fun onFinishSlidingInput(currentAutoCapsState: Int,
                             currentRecapitalizeState: Int) {
        mState!!.onFinishSlidingInput(currentAutoCapsState, currentRecapitalizeState)
    }

    // Implements {@link KeyboardState.SwitchActions}.
    override fun setAlphabetKeyboard() {
        if (SwitchActions.DEBUG_ACTION) {
            Log.d(TAG, "setAlphabetKeyboard")
        }
        setKeyboard(KeyboardId.ELEMENT_ALPHABET, KeyboardSwitchState.OTHER)
    }

    // Implements {@link KeyboardState.SwitchActions}.
    override fun setAlphabetManualShiftedKeyboard() {
        setKeyboard(KeyboardId.ELEMENT_ALPHABET_MANUAL_SHIFTED, KeyboardSwitchState.OTHER)
    }

    // Implements {@link KeyboardState.SwitchActions}.
    override fun setAlphabetAutomaticShiftedKeyboard() {
        setKeyboard(KeyboardId.ELEMENT_ALPHABET_AUTOMATIC_SHIFTED, KeyboardSwitchState.OTHER)
    }

    // Implements {@link KeyboardState.SwitchActions}.
    override fun setAlphabetShiftLockedKeyboard() {
        setKeyboard(KeyboardId.ELEMENT_ALPHABET_SHIFT_LOCKED, KeyboardSwitchState.OTHER)
    }

    // Implements {@link KeyboardState.SwitchActions}.
    override fun setAlphabetShiftLockShiftedKeyboard() {
        setKeyboard(KeyboardId.ELEMENT_ALPHABET_SHIFT_LOCK_SHIFTED, KeyboardSwitchState.OTHER)
    }

    // Implements {@link KeyboardState.SwitchActions}.
    override fun setSymbolsKeyboard() {
        if (SwitchActions.DEBUG_ACTION) {
            Log.d(TAG, "setSymbolsKeyboard")
        }
        setKeyboard(KeyboardId.ELEMENT_SYMBOLS, KeyboardSwitchState.OTHER)
    }

    // Implements {@link KeyboardState.SwitchActions}.
    override fun setSymbolsShiftedKeyboard() {
        if (SwitchActions.DEBUG_ACTION) {
            Log.d(TAG, "setSymbolsShiftedKeyboard")
        }
        setKeyboard(KeyboardId.ELEMENT_SYMBOLS_SHIFTED, KeyboardSwitchState.SYMBOLS_SHIFTED)
    }

    fun isImeSuppressedByHardwareKeyboard(
            @Nonnull settingsValues: SettingsValues,
            @Nonnull toggleState: KeyboardSwitchState): Boolean {
        return settingsValues.mHasHardwareKeyboard && toggleState == KeyboardSwitchState.HIDDEN
    }

    private fun setMainKeyboardFrame(@Nonnull settingsValues: SettingsValues,
                                     @Nonnull toggleState: KeyboardSwitchState) {
        val visibility = if (isImeSuppressedByHardwareKeyboard(settingsValues, toggleState)) View.GONE else View.VISIBLE
        mainKeyboardView!!.visibility = visibility
        mMainKeyboardFrame!!.visibility = visibility
        mEmojiPalettesView!!.visibility = View.GONE
        mEmojiPalettesView!!.stopEmojiPalettes()
    }

    // Implements {@link KeyboardState.SwitchActions}.
    override fun setEmojiKeyboard() {
        val keyboard = mKeyboardLayoutSet!!.getKeyboard(KeyboardId.ELEMENT_ALPHABET)
        mMainKeyboardFrame!!.visibility = View.GONE
        mainKeyboardView!!.visibility = View.GONE
        mEmojiPalettesView!!.startEmojiPalettes(mKeyboardTextsSet.getText(KeyboardTextsSet.SWITCH_TO_ALPHA_KEY_LABEL), mainKeyboardView!!.keyVisualAttribute, keyboard.mIconsSet)
        mEmojiPalettesView!!.visibility = View.VISIBLE
        searchBar!!.visibility = View.GONE
    }

    enum class KeyboardSwitchState(val mKeyboardId: Int) {
        HIDDEN(-1), SYMBOLS_SHIFTED(KeyboardId.ELEMENT_SYMBOLS_SHIFTED), EMOJI(KeyboardId.ELEMENT_EMOJI_RECENTS), OTHER(-1);
    }

    val keyboardSwitchState: KeyboardSwitchState
        get() {
            val hidden = (!isShowingEmojiPalettes && (mKeyboardLayoutSet == null || mainKeyboardView == null || !mainKeyboardView!!.isShown))
            var state: KeyboardSwitchState
            if (hidden) {
                return KeyboardSwitchState.HIDDEN
            } else if (isShowingEmojiPalettes) {
                return KeyboardSwitchState.EMOJI
            } else if (isShowingKeyboardId(KeyboardId.ELEMENT_SYMBOLS_SHIFTED)) {
                return KeyboardSwitchState.SYMBOLS_SHIFTED
            }
            return KeyboardSwitchState.OTHER
        }

    fun onToggleKeyboard(@Nonnull toggleState: KeyboardSwitchState) {
        val currentState = keyboardSwitchState
        if (currentState == toggleState) {
            mLatinIME!!.stopShowingInputView()
            mLatinIME!!.hideWindow()
            setAlphabetKeyboard()
        } else {
            mLatinIME!!.startShowingInputView(true)
            if (toggleState == KeyboardSwitchState.EMOJI) {
                setEmojiKeyboard()
            } else {
                mEmojiPalettesView!!.stopEmojiPalettes()
                mEmojiPalettesView!!.visibility = View.GONE
                mMainKeyboardFrame!!.visibility = View.VISIBLE
                mainKeyboardView!!.visibility = View.VISIBLE
                setKeyboard(toggleState.mKeyboardId, toggleState)
            }
        }
    }

    // Future method for requesting an updating to the shift state.
    override fun requestUpdatingShiftState(autoCapsFlags: Int, recapitalizeMode: Int) {
        mState!!.onUpdateShiftState(autoCapsFlags, recapitalizeMode)
    }

    // Implements {@link KeyboardState.SwitchActions}.
    override fun startDoubleTapShiftKeyTimer() {
        val keyboardView = mainKeyboardView
        keyboardView?.startDoubleTapShiftKeyTimer()
    }

    // Implements {@link KeyboardState.SwitchActions}.
    override fun cancelDoubleTapShiftKeyTimer() {
        val keyboardView = mainKeyboardView
        keyboardView?.cancelDoubleTapShiftKeyTimer()
    }

    // Implements {@link KeyboardState.SwitchActions}.
    override fun isInDoubleTapShiftKeyTimeout(): Boolean {
        val keyboardView = mainKeyboardView
        return keyboardView != null && keyboardView.isInDoubleTapShiftKeyTimeout
    }

    /**
     * Updates state machine to figure out when to automatically switch back to the previous mode.
     */
    fun onEvent(event: Event?, currentAutoCapsState: Int,
                currentRecapitalizeState: Int) {
        mState!!.onEvent(event, currentAutoCapsState, currentRecapitalizeState)
    }

    fun isShowingKeyboardId(@Nonnull vararg keyboardIds: Int): Boolean {
        if (mainKeyboardView == null || !mainKeyboardView!!.isShown) {
            return false
        }
        val activeKeyboardId = mainKeyboardView!!.keyboard!!.mId.mElementId
        for (keyboardId in keyboardIds) {
            if (activeKeyboardId == keyboardId) {
                return true
            }
        }
        return false
    }

    val isShowingEmojiPalettes: Boolean
        get() = mEmojiPalettesView != null && mEmojiPalettesView!!.isShown

    val isShowingMoreKeysPanel: Boolean
        get() = if (isShowingEmojiPalettes) {
            false
        } else mainKeyboardView!!.isShowingMoreKeysPanel

    val visibleKeyboardView: View?
        get() = if (isShowingEmojiPalettes) {
            mEmojiPalettesView
        } else mainKeyboardView

    fun deallocateMemory() {
        if (mainKeyboardView != null) {
            mainKeyboardView!!.cancelAllOngoingEvents()
            mainKeyboardView!!.deallocateMemory()
        }
        if (mEmojiPalettesView != null) {
            mEmojiPalettesView!!.stopEmojiPalettes()
        }
    }

    fun onCreateInputView(isHardwareAcceleratedDrawingEnabled: Boolean): View? {
        if (mainKeyboardView != null) {
            mainKeyboardView!!.closing()
        }

        updateKeyboardThemeAndContextThemeWrapper(mLatinIME, KeyboardTheme.getKeyboardTheme(mLatinIME))
        mCurrentInputView = LayoutInflater.from(mThemeContext).inflate(R.layout.input_view, null) as InputView
        mMainKeyboardFrame = mCurrentInputView!!.findViewById(R.id.main_keyboard_frame)
        mEmojiPalettesView = mCurrentInputView!!.findViewById<View>(R.id.emoji_palettes_view) as EmojiPalettesView
        mainKeyboardView = mCurrentInputView!!.findViewById<View>(R.id.keyboard_view) as MainKeyboardView
        mainKeyboardView!!.setHardwareAcceleratedDrawingEnabled(isHardwareAcceleratedDrawingEnabled)
        mainKeyboardView!!.setKeyboardActionListener(mLatinIME)
        mEmojiPalettesView!!.setHardwareAcceleratedDrawingEnabled(isHardwareAcceleratedDrawingEnabled)
        mEmojiPalettesView!!.setKeyboardActionListener(mLatinIME)

        //CatTabList
        searchBar = mCurrentInputView!!.findViewById<View>(R.id.view_search_view) as SearchView
        searchBar!!.setTouchListener(touchActionListener)
        mSickersView = mCurrentInputView!!.findViewById<View>(R.id.stickers_view) as StickersView
        mTabDisplayKbView = mCurrentInputView!!.findViewById(R.id.tab_display_kbview)
        mTabDisplayKbView!!.visibility = View.GONE
        mTabSearch = mCurrentInputView!!.findViewById(R.id.tab_switch_kb)
        llMain = mCurrentInputView!!.findViewById(R.id.ll_main)
        allTabsParantView = mCurrentInputView!!.findViewById<View>(R.id.naijamoji_tabs)


        if (mThemeContext != null) {
            mDatabase = AppDatabase.getDatabaseInstance(mThemeContext)
            rvCatListTab = mCurrentInputView!!.findViewById(R.id.rvCatListTab)
            rvCatListTab!!.layoutManager = LinearLayoutManager(mThemeContext, LinearLayoutManager.HORIZONTAL, false)
            mEmojiTabKeyboardAdapter = CatTabKeyboardAdapter(mThemeContext!!, mDatabase!!.userDao().allCatList as java.util.ArrayList<CatListTab>, this@KeyboardSwitcher)
            rvCatListTab!!.adapter = mEmojiTabKeyboardAdapter
        }

        slideUpAnimation = AnimationUtils.loadAnimation(mThemeContext, R.anim.slide_up_animation)
        slideDownAnimation = AnimationUtils.loadAnimation(mThemeContext, R.anim.slide_down_animation)
        mTabSearch!!.visibility = View.GONE

        //StickerView
        rvStickerGrid = mCurrentInputView!!.findViewById(R.id.sticker_grid)
        mLinearLayoutManager = GridLayoutManager(mThemeContext, 4)
        simpleSeekBar = mCurrentInputView!!.findViewById(R.id.simpleSeekBar) as SeekBar
        rlSeekbar = mCurrentInputView!!.findViewById(R.id.rlSeekbar) as RelativeLayout
        rlOne = mCurrentInputView!!.findViewById(R.id.rlOne) as RelativeLayout
        rlTwo = mCurrentInputView!!.findViewById(R.id.rlTwo) as RelativeLayout
        ivPlus = mCurrentInputView!!.findViewById(R.id.ivPlus) as ImageView
        ivMinus = mCurrentInputView!!.findViewById(R.id.ivMinus) as ImageView
        simpleSeekBar!!.isEnabled = false

        //StickerTag
        rvStickerTag = mCurrentInputView!!.findViewById(R.id.rvStickerTag) as RecyclerView
        rvStickerTag!!.layoutManager = LinearLayoutManager(mThemeContext, LinearLayoutManager.HORIZONTAL, false)
        mStickersTagAdapter = StickerTagKeyboardAdapter(mThemeContext!!, this@KeyboardSwitcher, mDatabase!!.userDao().allTagList as ArrayList<TagList>)
        rvStickerTag!!.adapter = mStickersTagAdapter

        //   searchViewSetVisiblity(false)
        mTabSearch!!.setOnClickListener {
            if (searchBar!!.visibility == View.VISIBLE) {
                searchViewSetVisiblity(false)
            } else {
                searchViewSetVisiblity(true)
            }
        }

        ivPlus!!.setOnClickListener {
            if (progressChangedValue == 0) {
                simpleSeekBar!!.progress = 50
                rvStickerGrid!!.layoutManager = mLinearLayoutManager
                setAllStickerAdapter(100)
            } else if (progressChangedValue == 50) {
                simpleSeekBar!!.progress = 100
                rvStickerGrid!!.layoutManager = mLinearLayoutManager
                setAllStickerAdapter(140)
            }
        }


        ivMinus!!.setOnClickListener {
            if (progressChangedValue == 100) {
                simpleSeekBar!!.progress = 50
                rvStickerGrid!!.layoutManager = mLinearLayoutManager
                setAllStickerAdapter(100)
            } else if (progressChangedValue == 50) {
                simpleSeekBar!!.progress = 0
                rvStickerGrid!!.layoutManager = mLinearLayoutManager
                setAllStickerAdapter(70)
            }
        }



        simpleSeekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                progressChangedValue = progress
                if (progressChangedValue <= 49) {
                    mLinearLayoutManager!!.spanCount = 4
                    mLinearLayoutManager!!.requestLayout()
                } else if (progressChangedValue >= 50 && progressChangedValue <= 99) {
                    mLinearLayoutManager!!.spanCount = 3
                    mLinearLayoutManager!!.requestLayout()
                } else if (progressChangedValue == 100) {
                    mLinearLayoutManager!!.spanCount = 2
                    mLinearLayoutManager!!.requestLayout()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })


        //LoginDialog=========================================================
        rlLogin = mCurrentInputView!!.findViewById(R.id.rlLogin) as RelativeLayout
        linearSignin = mCurrentInputView!!.findViewById(R.id.linear_signin) as LinearLayout
        blockerCloseButton = mCurrentInputView!!.findViewById(R.id.blocker_close_button) as ImageView
        btnSignin = mCurrentInputView!!.findViewById(R.id.btn_signin) as Button

        linearSignin!!.setOnClickListener {
            val intent = Intent(mThemeContext, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            mThemeContext!!.startActivity(intent)
            rlLogin!!.visibility = View.GONE
        }
        btnSignin!!.setOnClickListener {
            val intent = Intent(mThemeContext, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            mThemeContext!!.startActivity(intent)
            rlLogin!!.visibility = View.GONE
        }
        blockerCloseButton!!.setOnClickListener {
            if (rlLogin != null && rlLogin!!.visibility == View.VISIBLE) {
                rlLogin!!.visibility = View.GONE
            }
        }
        return mCurrentInputView
    }

    val keyboardShiftMode: Int
        get() {
            val keyboard = keyboard ?: return WordComposer.CAPS_MODE_OFF
            return when (keyboard.mId.mElementId) {
                KeyboardId.ELEMENT_ALPHABET_SHIFT_LOCKED, KeyboardId.ELEMENT_ALPHABET_SHIFT_LOCK_SHIFTED -> WordComposer.CAPS_MODE_MANUAL_SHIFT_LOCKED
                KeyboardId.ELEMENT_ALPHABET_MANUAL_SHIFTED -> WordComposer.CAPS_MODE_MANUAL_SHIFTED
                KeyboardId.ELEMENT_ALPHABET_AUTOMATIC_SHIFTED -> WordComposer.CAPS_MODE_AUTO_SHIFTED
                else -> WordComposer.CAPS_MODE_OFF
            }
        }

    val currentKeyboardScriptId: Int
        get() = if (null == mKeyboardLayoutSet) {
            ScriptUtils.SCRIPT_UNKNOWN
        } else mKeyboardLayoutSet!!.scriptId

    private fun searchViewSetVisiblity(isShow: Boolean) {
        if (isShow) {
            //DoVisible SearchView
            searchBar!!.visibility = View.VISIBLE
            SearchView.showCursor()
            mSickersView!!.visibility = View.GONE
            mSickersView!!.startAnimation(slideDownAnimation)
            rvStickerTag!!.visibility = View.VISIBLE
            mCurrentInputView!!.setBackgroundColor(mCurrentInputView!!.resources.getColor(R.color.dim_overlay_on_keyboard))

        } else {
            mState!!.enableAlphabetMode()
            searchBar!!.clearFocus()
            searchBar!!.setCursorVisible(false)
            searchBar!!.visibility = View.GONE
            mSickersView!!.visibility = View.GONE
            SearchView.mSearchView.setText("")
            allTabsParantView!!.visibility = View.VISIBLE
            rvStickerTag!!.visibility = View.INVISIBLE
            mCurrentInputView!!.setBackgroundColor(mCurrentInputView!!.resources.getColor(R.color.dim_overlay_on_keyboard))
        }
    }

    private val touchActionListener: TouchActionListener = object : TouchActionListener {
        override fun onSearchBarTouched() {}
        override fun onBackArrowClicked() {
            SearchClose()
        }
    }

    fun SearchClose() {
        //==========================================
        //Refresh CatList for uncheck item
        mEmojiTabKeyboardAdapter = CatTabKeyboardAdapter(mThemeContext!!, mDatabase!!.userDao().allCatList as ArrayList<CatListTab>, this@KeyboardSwitcher)
        rvCatListTab!!.adapter = mEmojiTabKeyboardAdapter
        mTabSearch!!.visibility = View.GONE
        //======================================

        mState!!.enableAlphabetMode()
        searchBar!!.clearFocus()
        searchBar!!.setCursorVisible(false)
        searchBar!!.visibility = View.GONE
        mSickersView!!.visibility = View.GONE
        SearchView.mSearchView.setText("")
        allTabsParantView!!.visibility = View.VISIBLE
        rvStickerTag!!.visibility = View.INVISIBLE
        //   setSelect("")
        mCurrentInputView!!.setBackgroundColor(mCurrentInputView!!.resources.getColor(android.R.color.transparent))
    }

    val isShowingGenericSearchBar: Boolean
        get() = if (searchBar != null) {
            searchBar!!.isShown
        } else false

    val isShowingKeyboardView: Boolean
        get() = if (mainKeyboardView != null) {
            mainKeyboardView!!.isShown
        } else false

    companion object {
        private val TAG = KeyboardSwitcher::class.java.simpleName
        val instance = KeyboardSwitcher()

        @JvmStatic
        fun init(latinIme: LatinIME) {
            instance.initInternal(latinIme)
        }
    }


    override fun tagClick(mTagId: String) {
        mState!!.enableAlphabetMode()
        searchBar!!.clearFocus()
        searchBar!!.setCursorVisible(false)
        searchBar!!.visibility = View.GONE
        mSickersView!!.visibility = View.VISIBLE
        mSickersView!!.startAnimation(slideUpAnimation)
        SearchView.mSearchView.setText("")
        allTabsParantView!!.visibility = View.VISIBLE
        rvStickerTag!!.visibility = View.INVISIBLE
        mCurrentInputView!!.setBackgroundColor(mCurrentInputView!!.resources.getColor(android.R.color.transparent))
        callSearchStickerWs(mTagId)
    }

    //getWordWhenUserTypeInEditText
    fun getWords(word: Editable) {
        if (!TextUtils.isEmpty(word.toString())) {
            rvStickerTag!!.layoutManager = LinearLayoutManager(mThemeContext, LinearLayoutManager.HORIZONTAL, false)
            mStickersTagAdapter = StickerTagKeyboardAdapter(mThemeContext!!, this@KeyboardSwitcher, mDatabase!!.userDao().getTagBySearch("%" + word.toString() + "%") as ArrayList<TagList>)
            rvStickerTag!!.adapter = mStickersTagAdapter
        } else {
            rvStickerTag!!.layoutManager = LinearLayoutManager(mThemeContext, LinearLayoutManager.HORIZONTAL, false)
            mStickersTagAdapter = StickerTagKeyboardAdapter(mThemeContext!!, this@KeyboardSwitcher, mDatabase!!.userDao().allTagList as ArrayList<TagList>)
            rvStickerTag!!.adapter = mStickersTagAdapter
        }
    }

    fun isLogin(): Boolean {
        return Preferences.INSTANCE!!.loginStatus
    }

    //CatListTabClicked=========================================================================
    override fun mTabClick(resultm: CatListTab?, position: Int, isClose: Boolean, isLogin: String, isPostionZero: Boolean) {
        isDaynamic = false
        if (isLogin.equals("1")) {
            if (isPostionZero) {
                rlLogin!!.visibility = View.GONE
                if (resultm!!.select) {
                    if (mSickersView!!.visibility == View.VISIBLE) {
                        callPopulerStickerWs(resultm.id)
                        rlSeekbar!!.visibility = View.VISIBLE
                    } else {
                        mSickersView!!.startAnimation(slideUpAnimation)
                        mSickersView!!.visibility = View.VISIBLE
                        mTabSearch!!.visibility = View.VISIBLE
                        callPopulerStickerWs(resultm.id)
                        rlSeekbar!!.visibility = View.VISIBLE
                    }
                } else {
                    mSickersView!!.startAnimation(slideDownAnimation)
                    mSickersView!!.visibility = View.GONE
                    mTabSearch!!.visibility = View.GONE
                }
            } else {
                rlLogin!!.visibility = View.GONE
                if (resultm!!.select) {
                    if (mSickersView!!.visibility == View.VISIBLE) {
                        mEmojiList = mDatabase!!.userDao().getStickersByID(resultm.id) as ArrayList<AllStickerList>?
                        Log.i("sizeStickers", mEmojiList!!.size.toString())
                        if (mEmojiList != null && mEmojiList!!.size > 0) {
                            rvStickerGrid!!.layoutManager = mLinearLayoutManager
                            if (progressChangedValue == 0) {
                                setAllStickerAdapter(70)
                            } else if (progressChangedValue == 50) {
                                setAllStickerAdapter(100)
                            } else if (progressChangedValue == 100) {
                                setAllStickerAdapter(140)
                            }
                            rvStickerGrid!!.adapter = cardlistAdapter
                            rlSeekbar!!.visibility = View.VISIBLE
                            rvStickerGrid!!.visibility = View.VISIBLE
                        } else {
                            rlSeekbar!!.visibility = View.GONE
                            rvStickerGrid!!.visibility = View.GONE
                        }
                    } else {
                        mSickersView!!.startAnimation(slideUpAnimation)
                        mSickersView!!.visibility = View.VISIBLE
                        mTabSearch!!.visibility = View.VISIBLE
                        mEmojiList = mDatabase!!.userDao().getStickersByID(resultm.id) as ArrayList<AllStickerList>?
                        Log.i("sizeStickers", mEmojiList!!.size.toString())
                        if (mEmojiList != null && mEmojiList!!.size > 0) {
                            rvStickerGrid!!.layoutManager = mLinearLayoutManager
                            if (progressChangedValue == 0) {
                                setAllStickerAdapter(70)
                            } else if (progressChangedValue == 50) {
                                setAllStickerAdapter(100)
                            } else if (progressChangedValue == 100) {
                                setAllStickerAdapter(140)
                            }

                            rlSeekbar!!.visibility = View.VISIBLE
                            rvStickerGrid!!.visibility = View.VISIBLE
                        } else {
                            rlSeekbar!!.visibility = View.GONE
                            rvStickerGrid!!.visibility = View.GONE
                        }
                    }
                } else {
                    mSickersView!!.startAnimation(slideDownAnimation)
                    mSickersView!!.visibility = View.GONE
                    mTabSearch!!.visibility = View.GONE
                }
            }

        } else {
            rlLogin!!.visibility = View.VISIBLE
        }
    }

    private fun setAllStickerAdapter(size: Int) {
        if (isDaynamic) {
            var mEmojiList: ArrayList<AllStickerList>? = null
            cardlistAdapter = KbStickersAdapter(mThemeContext!!, mEmojiList, mDaynamicEmojilist, this@KeyboardSwitcher, this@KeyboardSwitcher, size)
            rvStickerGrid!!.adapter = cardlistAdapter
        } else {
            var mDaynamicEmojilist: ArrayList<StickerListByTagOrCategory.Result>? = null
            cardlistAdapter = KbStickersAdapter(mThemeContext!!, mEmojiList, mDaynamicEmojilist, this@KeyboardSwitcher, this@KeyboardSwitcher, size)
            rvStickerGrid!!.adapter = cardlistAdapter
        }
    }


    //local Stickers click=========================================
    override fun sendSticekrs(context: Context, position: Int, data: AllStickerList, isSendOrNot: String, mBitpam: Bitmap, mImageView: ImageView) {
        if (isSendOrNot.equals("0")) {
            Glide.with(context)
                    .asBitmap().load(mBitpam)
                    .listener(object : RequestListener<Bitmap> {
                        override fun onResourceReady(bitmap: Bitmap, o: Any, target: com.bumptech.glide.request.target.Target<Bitmap>?, dataSource: DataSource, b: Boolean): Boolean {
                            if (Build.VERSION.SDK_INT >= 25) {
                                RichInputConnection.doCommitContent("Recovery animation", Utility.bitmapToFile(bitmap, context), bitmap)
                            } else {
                                if (RichInputConnection.getPackageName() != null && RichInputConnection.getPackageName().equals("com.hike.chat.stickers")
                                        || RichInputConnection.getPackageName().equals("com.whatsapp")
                                        || RichInputConnection.getPackageName().equals("com.facebook.orca")
                                        || RichInputConnection.getPackageName().equals("com.snapchat.android")) {
                                    RichInputConnection.doCommitContent("Recovery animation", Utility.bitmapToFile(bitmap, context), bitmap)
                                } else {
                                    Utility.share(context, RichInputConnection.getPackageName(), bitmap)
                                }
                            }
                            return false
                        }

                        override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                            return false
                        }
                    }).submit()
        } else {
            if (data.is_gender_available.equals("0") && data.is_studiomode.equals("0")) {

            } else {
                val builder = AlertDialog.Builder(
                        DialogUtils.getPlatformDialogThemeContext(mThemeContext!!))
                val create = builder.create()
                var inflate: View? = null
                if (data.is_gender_available.equals("0") && data.is_studiomode.equals("1")) {
                    inflate = create.layoutInflater.inflate(R.layout.dialog_keyboard_second, null)
                } else if (data.is_gender_available.equals("1") && data.is_studiomode.equals("0")) {
                    inflate = create.layoutInflater.inflate(R.layout.dialog_keyboardremove_studiomode, null)
                } else {
                    inflate = create.layoutInflater.inflate(R.layout.dialog_keyboard, null)
                }
                val rlLight = inflate!!.findViewById<View>(R.id.rlLight) as LinearLayout
                val rlMedium = inflate.findViewById<View>(R.id.rlMedium) as LinearLayout
                val rlDark = inflate.findViewById<View>(R.id.rlDark) as LinearLayout
                val rlGender = inflate.findViewById<View>(R.id.rlGender) as LinearLayout
                val rlStudioMode = inflate.findViewById<View>(R.id.rlStudioMode) as LinearLayout
                create.window!!.setGravity(Gravity.BOTTOM)
                if (create.window != null) {
                    if (Build.VERSION.SDK_INT >= 26) {
//                        create.window!!.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY - 1)
                        create.window!!.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
                    } else {
                        create.window!!.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
                    }
                }
                create.window!!.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
                val windowToken: IBinder = mainKeyboardView!!.getWindowToken()
                        ?: return
                val window: Window = create.window!!
                val lp = window.attributes
                lp.token = windowToken
                lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG
                window.attributes = lp
                window.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
                //   mOptionsDialog = dialog
                create.setView(inflate)
                if (data.is_gender_available.equals("0")) {
                    rlLight.visibility = View.GONE
                    rlMedium.visibility = View.GONE
                    rlDark.visibility = View.GONE
                    rlGender.visibility = View.GONE
                } else {
                    rlLight.visibility = View.VISIBLE
                    rlMedium.visibility = View.VISIBLE
                    rlDark.visibility = View.VISIBLE
                    rlGender.visibility = View.VISIBLE
                }
                rlLight.setOnClickListener {
                    if (data.genderSticker.equals("ML")) {
                        Glide.with(mThemeContext!!).load(data.img_m_light).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "ML"
                        Log.i("gender", data.genderSticker)
                    }
                    if (data.genderSticker.equals("MM")) {
                        Glide.with(mThemeContext!!).load(data.img_m_light).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "ML"
                        Log.i("gender", data.genderSticker)
                    }
                    if (data.genderSticker.equals("MD")) {
                        Glide.with(mThemeContext!!).load(data.img_m_light).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "ML"
                        Log.i("gender", data.genderSticker)
                    }
                    if (data.genderSticker.equals("FL")) {
                        Glide.with(mThemeContext!!).load(data.img_f_light).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "FL"
                        Log.i("gender", data.genderSticker)
                    }
                    if (data.genderSticker.equals("FM")) {
                        Glide.with(mThemeContext!!).load(data.img_f_light).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "FL"
                        Log.i("gender", data.genderSticker)
                    }
                    if (data.genderSticker.equals("FD")) {
                        Glide.with(mThemeContext!!).load(data.img_f_light).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "FL"
                        Log.i("gender", data.genderSticker)
                    }
                    create.cancel()
                }
                rlMedium.setOnClickListener {
                    if (data.genderSticker.equals("ML")) {
                        Glide.with(mThemeContext!!).load(data.img_m_medium).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "MM"
                        Log.i("gender", data.genderSticker)
                    }
                    if (data.genderSticker.equals("MM")) {
                        Glide.with(mThemeContext!!).load(data.img_m_medium).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "MM"
                        Log.i("gender", data.genderSticker)
                    }
                    if (data.genderSticker.equals("MD")) {
                        Glide.with(mThemeContext!!).load(data.img_m_medium).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "MM"
                        Log.i("gender", data.genderSticker)

                    }
                    if (data.genderSticker.equals("FL")) {
                        Glide.with(mThemeContext!!).load(data.img_f_medium).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "FM"
                        Log.i("gender", data.genderSticker)
                    }
                    if (data.genderSticker.equals("FM")) {
                        Glide.with(mThemeContext!!).load(data.img_f_medium).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "FM"
                        Log.i("gender", data.genderSticker)
                    }
                    if (data.genderSticker.equals("FD")) {
                        Glide.with(mThemeContext!!).load(data.img_f_medium).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "FM"
                        Log.i("gender", data.genderSticker)
                    }
                    create.cancel()
                }
                rlDark.setOnClickListener {
                    if (data.genderSticker.equals("ML")) {
                        Glide.with(mThemeContext!!).load(data.img_m_dark).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "MD"
                        Log.i("gender", data.genderSticker)
                    }
                    if (data.genderSticker.equals("MM")) {
                        Glide.with(mThemeContext!!).load(data.img_m_dark).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "MD"
                        Log.i("gender", data.genderSticker)
                    }
                    if (data.genderSticker.equals("MD")) {
                        Glide.with(mThemeContext!!).load(data.img_m_dark).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "MD"
                        Log.i("gender", data.genderSticker)
                    }
                    if (data.genderSticker.equals("FL")) {
                        Glide.with(mThemeContext!!).load(data.img_f_dark).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "FD"
                        Log.i("gender", data.genderSticker)
                    }
                    if (data.genderSticker.equals("FM")) {
                        Glide.with(mThemeContext!!).load(data.img_f_dark).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "FD"
                        Log.i("gender", data.genderSticker)
                    }
                    if (data.genderSticker.equals("FD")) {
                        Glide.with(mThemeContext!!).load(data.img_f_dark).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "FD"
                        Log.i("gender", data.genderSticker)
                    }
                    create.cancel()
                }
                rlGender.setOnClickListener {
                    if (data.genderSticker.equals("ML")) {
                        Glide.with(mThemeContext!!).load(data.img_f_light).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "FL"
                        Log.i("gender", data.genderSticker)
                    } else if (data.genderSticker.equals("MM")) {
                        Glide.with(mThemeContext!!).load(data.img_f_medium).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "FM"
                        Log.i("gender", data.genderSticker)
                    } else if (data.genderSticker.equals("MD")) {
                        Glide.with(mThemeContext!!).load(data.img_f_dark).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "FD"
                        Log.i("gender", data.genderSticker)
                    } else if (data.genderSticker.equals("FL")) {
                        Glide.with(mThemeContext!!).load(data.img_m_light).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "ML"
                        Log.i("gender", data.genderSticker)
                    } else if (data.genderSticker.equals("FM")) {
                        Glide.with(mThemeContext!!).load(data.img_m_medium).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "MM"
                        Log.i("gender", data.genderSticker)
                    } else if (data.genderSticker.equals("FD")) {
                        Glide.with(mThemeContext!!).load(data.img_m_dark).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "MD"
                        Log.i("gender", data.genderSticker)
                    }
                    create.cancel()
                }
                rlStudioMode.setOnClickListener {
                    setViewVisiblity()
                    if (data.is_gender_available.equals("0")) {
                        goToApp("4", data.image, data.id!!, data.text_limit!!)
                    } else {
                        if (data.genderSticker.equals("ML")) {
                            goToApp("4", data.img_m_light, data.id!!, data.text_limit!!)
                        }
                        if (data.genderSticker.equals("MM")) {
                            goToApp("4", data.img_m_medium, data.id!!, data.text_limit!!)
                        }
                        if (data.genderSticker.equals("MD")) {
                            goToApp("4", data.img_m_dark, data.id!!, data.text_limit!!)
                        }
                        if (data.genderSticker.equals("FL")) {
                            goToApp("4", data.img_f_light, data.id!!, data.text_limit!!)
                        }
                        if (data.genderSticker.equals("FM")) {
                            goToApp("4", data.img_f_medium, data.id!!, data.text_limit!!)
                        }
                        if (data.genderSticker.equals("FD")) {
                            goToApp("4", data.img_f_dark, data.id!!, data.text_limit!!)
                        }
                    }
                    create.cancel()
                }

                create.setOnCancelListener {
                    rlOne!!.visibility = View.GONE
                    rlTwo!!.visibility = View.GONE
                    create.cancel()
                }
                create.setOnDismissListener {
                    rlOne!!.visibility = View.GONE
                    rlTwo!!.visibility = View.GONE
                    create.dismiss()
                }

                rlOne!!.visibility = View.VISIBLE
                rlTwo!!.visibility = View.VISIBLE

                if (data.is_gender_available.equals("0") && data.is_studiomode.equals("0")) {

                } else {
                    create.show()
                }

                if (data.is_gender_available.equals("0")) {
                    create.window?.setLayout(dpToPx(100), ViewGroup.LayoutParams.WRAP_CONTENT)
                } else if (data.is_studiomode.equals("0")) {
                    create.window?.setLayout(dpToPx(250), ViewGroup.LayoutParams.WRAP_CONTENT)
                } else {
                    create.window?.setLayout(dpToPx(300), ViewGroup.LayoutParams.WRAP_CONTENT)
                }
            }
        }
    }

    //Daynamic sticker click
    override fun sendDaynamicSticekrs(context: Context, position: Int, data: StickerListByTagOrCategory.Result, isSendOrNot: String, mBitpam: Bitmap, mImageView: ImageView) {
        if (isSendOrNot.equals("0")) {
            Glide.with(context)
                    .asBitmap().load(mBitpam)
                    .listener(object : RequestListener<Bitmap> {
                        override fun onResourceReady(bitmap: Bitmap, o: Any, target: com.bumptech.glide.request.target.Target<Bitmap>?, dataSource: DataSource, b: Boolean): Boolean {
                            if (Build.VERSION.SDK_INT >= 25) {
                                RichInputConnection.doCommitContent("Recovery animation", Utility.bitmapToFile(bitmap, context), bitmap)
                            } else {
                                if (RichInputConnection.getPackageName() != null && RichInputConnection.getPackageName().equals("com.hike.chat.stickers")
                                        || RichInputConnection.getPackageName().equals("com.whatsapp")
                                        || RichInputConnection.getPackageName().equals("com.facebook.orca")
                                        || RichInputConnection.getPackageName().equals("com.snapchat.android")) {
                                    RichInputConnection.doCommitContent("Recovery animation", Utility.bitmapToFile(bitmap, context), bitmap)
                                } else {
                                    Utility.share(context, RichInputConnection.getPackageName(), bitmap)
                                }
                            }
                            return false
                        }

                        override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                            return false
                        }
                    }).submit()
        } else {
            //  AutoStartHelper.instance.getAutoStartPermission(mThemeContext!!);

            if (data.is_gender_available.equals("0") && data.is_studiomode.equals("0")) {
            } else {
                val builder = AlertDialog.Builder(
                        DialogUtils.getPlatformDialogThemeContext(mThemeContext!!))
                val create = builder.create()
                var inflate: View? = null
                if (data.is_gender_available.equals("0") && data.is_studiomode.equals("1")) {
                    inflate = create.layoutInflater.inflate(R.layout.dialog_keyboard_second, null)
                } else if (data.is_gender_available.equals("1") && data.is_studiomode.equals("0")) {
                    inflate = create.layoutInflater.inflate(R.layout.dialog_keyboardremove_studiomode, null)
                } else {
                    inflate = create.layoutInflater.inflate(R.layout.dialog_keyboard, null)
                }
                val rlLight = inflate!!.findViewById<View>(R.id.rlLight) as LinearLayout
                val rlMedium = inflate.findViewById<View>(R.id.rlMedium) as LinearLayout
                val rlDark = inflate.findViewById<View>(R.id.rlDark) as LinearLayout
                val rlGender = inflate.findViewById<View>(R.id.rlGender) as LinearLayout
                val rlStudioMode = inflate.findViewById<View>(R.id.rlStudioMode) as LinearLayout
                create.window!!.setGravity(Gravity.BOTTOM)
//                create.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
//                create.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                if (create.window != null) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        create.window!!.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
                    } else {
                        create.window!!.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
                    }
                }
                create.window!!.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)

                val windowToken: IBinder = mainKeyboardView!!.getWindowToken()
                        ?: return
                val window: Window = create.window!!
                val lp = window.attributes
                lp.token = windowToken
                lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG
                window.attributes = lp
                window.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
                create.setView(inflate)
                if (data.is_gender_available.equals("0")) {
                    rlLight.visibility = View.GONE
                    rlMedium.visibility = View.GONE
                    rlDark.visibility = View.GONE
                    rlGender.visibility = View.GONE
                } else {
                    rlLight.visibility = View.VISIBLE
                    rlMedium.visibility = View.VISIBLE
                    rlDark.visibility = View.VISIBLE
                    rlGender.visibility = View.VISIBLE
                }
                rlLight.setOnClickListener {

                    if (data.genderSticker.equals("ML")) {
                        Glide.with(mThemeContext!!).load(data.img_m_light).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "ML"
                        Log.i("gender","===>"+ data.genderSticker)
                    } else if (data.genderSticker.equals("MM")) {
                        Glide.with(mThemeContext!!).load(data.img_m_light).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "ML"
                        Log.i("gender", "===>"+data.genderSticker)
                    } else if (data.genderSticker.equals("MD")) {
                        Glide.with(mThemeContext!!).load(data.img_m_light).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "ML"
                        Log.i("gender", "===>"+data.genderSticker)
                    } else if (data.genderSticker.equals("FL")) {
                        Glide.with(mThemeContext!!).load(data.img_f_light).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "FL"
                        Log.i("gender", "===>"+data.genderSticker)
                    } else if (data.genderSticker.equals("FM")) {
                        Glide.with(mThemeContext!!).load(data.img_f_light).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "FL"
                        Log.i("gender", "===>"+data.genderSticker)
                    } else if (data.genderSticker.equals("FD")) {
                        Glide.with(mThemeContext!!).load(data.img_f_light).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "FL"
                        Log.i("gender", "===>"+data.genderSticker)
                    }
                    create.cancel()
                }
                rlMedium.setOnClickListener {

                    if (data.genderSticker.equals("ML")) {
                        Glide.with(mThemeContext!!).load(data.img_m_medium).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "MM"
                        Log.i("gender", "===>"+data.genderSticker)
                    } else if (data.genderSticker.equals("MM")) {
                        Glide.with(mThemeContext!!).load(data.img_m_medium).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "MM"
                        Log.i("gender", "===>"+data.genderSticker)
                    } else if (data.genderSticker.equals("MD")) {
                        Glide.with(mThemeContext!!).load(data.img_m_medium).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "MM"
                        Log.i("gender", "===>"+data.genderSticker)

                    } else if (data.genderSticker.equals("FL")) {
                        Glide.with(mThemeContext!!).load(data.img_f_medium).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "FM"
                        Log.i("gender", "===>"+data.genderSticker)
                    } else if (data.genderSticker.equals("FM")) {
                        Glide.with(mThemeContext!!).load(data.img_f_medium).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "FM"
                        Log.i("gender", "===>"+data.genderSticker)
                    } else if (data.genderSticker.equals("FD")) {
                        Glide.with(mThemeContext!!).load(data.img_f_medium).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "FM"
                        Log.i("gender", "===>"+data.genderSticker)
                    }
                    create.cancel()
                }
                rlDark.setOnClickListener {
                    if (data.genderSticker.equals("ML")) {
                        Glide.with(mThemeContext!!).load(data.img_m_dark).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "MD"
                        Log.i("gender", "===>"+data.genderSticker)
                    } else if (data.genderSticker.equals("MM")) {
                        Glide.with(mThemeContext!!).load(data.img_m_dark).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "MD"
                        Log.i("gender", "===>"+data.genderSticker)
                    } else if (data.genderSticker.equals("MD")) {
                        Glide.with(mThemeContext!!).load(data.img_m_dark).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "MD"
                        Log.i("gender", "===>"+data.genderSticker)
                    } else if (data.genderSticker.equals("FL")) {
                        Glide.with(mThemeContext!!).load(data.img_f_dark).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "FD"
                        Log.i("gender", "===>"+data.genderSticker)
                    } else if (data.genderSticker.equals("FM")) {
                        Glide.with(mThemeContext!!).load(data.img_f_dark).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "FD"
                        Log.i("gender", "===>"+data.genderSticker)
                    } else if (data.genderSticker.equals("FD")) {
                        Glide.with(mThemeContext!!).load(data.img_f_dark).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "FD"
                        Log.i("gender", "===>"+data.genderSticker)
                    }
                    create.cancel()
                }
                rlGender.setOnClickListener {
                    if (data.genderSticker.equals("ML")) {
                        Glide.with(mThemeContext!!).load(data.img_f_light).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "FL"
                        Log.i("gender", "===>"+data.genderSticker)
                    } else if (data.genderSticker.equals("MM")) {
                        Glide.with(mThemeContext!!).load(data.img_f_medium).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "FM"
                        Log.i("gender", "===>"+data.genderSticker)
                    } else if (data.genderSticker.equals("MD")) {
                        Glide.with(mThemeContext!!).load(data.img_f_dark).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "FD"
                        Log.i("gender", "===>"+data.genderSticker)
                    } else if (data.genderSticker.equals("FL")) {
                        Glide.with(mThemeContext!!).load(data.img_m_light).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "ML"
                        Log.i("gender", "===>"+data.genderSticker)
                    } else if (data.genderSticker.equals("FM")) {
                        Glide.with(mThemeContext!!).load(data.img_m_medium).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "MM"
                        Log.i("gender", "===>"+data.genderSticker)
                    } else if (data.genderSticker.equals("FD")) {
                        Glide.with(mThemeContext!!).load(data.img_m_dark).error(R.drawable.photos).into(mImageView)
                        data.genderSticker = "MD"
                        Log.i("gender", "===>"+data.genderSticker)
                    }
                    create.cancel()
                }

                rlStudioMode.setOnClickListener {
                    setViewVisiblity()
                    if (data.is_gender_available.equals("0")) {
                        goToApp("4", data.image!!, data.id!!, data.text_limit!!)
                    } else {
                        if (data.genderSticker.equals("ML")) {
                            goToApp("4", data.img_m_light!!, data.id!!, data.text_limit!!)
                        } else if (data.genderSticker.equals("MM")) {
                            goToApp("4", data.img_m_medium!!, data.id!!, data.text_limit!!)
                        } else if (data.genderSticker.equals("MD")) {
                            goToApp("4", data.img_m_dark!!, data.id!!, data.text_limit!!)
                        } else if (data.genderSticker.equals("FL")) {
                            goToApp("4", data.img_f_light!!, data.id!!, data.text_limit!!)
                        } else if (data.genderSticker.equals("FM")) {
                            goToApp("4", data.img_f_medium!!, data.id!!, data.text_limit!!)
                        } else if (data.genderSticker.equals("FD")) {
                            goToApp("4", data.img_f_dark!!, data.id!!, data.text_limit!!)
                        }
                    }
                    create.cancel()
                }

                create.setOnCancelListener {
                    rlOne!!.visibility = View.GONE
                    rlTwo!!.visibility = View.GONE
                    create.cancel()
                }
                create.setOnDismissListener {
                    rlOne!!.visibility = View.GONE
                    rlTwo!!.visibility = View.GONE
                    create.dismiss()
                }
                rlOne!!.visibility = View.VISIBLE
                rlTwo!!.visibility = View.VISIBLE

                if (data.is_gender_available.equals("0") && data.is_studiomode.equals("0")) {
                } else {
                    create.show()
                }

                if (data.is_gender_available.equals("0")) {
                    create.window?.setLayout(dpToPx(100), ViewGroup.LayoutParams.WRAP_CONTENT)
                } else if (data.is_studiomode.equals("0")) {
                    create.window?.setLayout(dpToPx(250), ViewGroup.LayoutParams.WRAP_CONTENT)
                } else {
                    create.window?.setLayout(dpToPx(300), ViewGroup.LayoutParams.WRAP_CONTENT)
                }
            }
        }

    }


    fun dpToPx(dp: Int): Int {
        val density = mThemeContext!!.resources
                .displayMetrics
                .density
        return Math.round(dp.toFloat() * density)
    }


    private fun goToApp(mNavigationCount: String, mStickerUrl: String, mStickerId: String, mTextLimit: String) {
        if (Preferences.INSTANCE!!.loginStatus) {
            val intent = Intent(mThemeContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("formKeyboard", mNavigationCount)
            if (mNavigationCount.equals("4")) {
                intent.putExtra("selected_sticker", mStickerUrl)
                intent.putExtra("selected_id", mStickerId)
                intent.putExtra("character_limit", mTextLimit)
            }
            mThemeContext!!.startActivity(intent)
        } else {
            val intent = Intent(mThemeContext, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("formKeyboard", mNavigationCount)
            mThemeContext!!.startActivity(intent)
        }
    }

    private fun setViewVisiblity() {
        mEmojiTabKeyboardAdapter = CatTabKeyboardAdapter(mThemeContext!!, mDatabase!!.userDao().allCatList as ArrayList<CatListTab>, this@KeyboardSwitcher)
        rvCatListTab!!.adapter = mEmojiTabKeyboardAdapter
        mTabSearch!!.visibility = View.GONE

        rlOne!!.visibility = View.GONE
        rlTwo!!.visibility = View.GONE
        mSickersView!!.visibility = View.GONE
        mainKeyboardView!!.visibility = View.GONE
        //    setSelect("")
    }

    // Get sticker from tag
    private fun callSearchStickerWs(mTagId: String) {
        AndroidNetworking.post(Constants.INSTANCE.URLLOCAL + Constants.INSTANCE.strWS_emoji_list_tag)
                .addBodyParameter(Constants.INSTANCE.str_tag, mTagId)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(StickerListByTagOrCategory::class.java, object : ParsedRequestListener<StickerListByTagOrCategory> {
                    override fun onResponse(response: StickerListByTagOrCategory?) {
                        Log.i("response", "PLPLPLPL" + response.toString())
                        var mSuccess = false
                        if (response != null)
                            if (response.status!!.success == (Constants.INSTANCE.one)) {
                                mSuccess = true
                            }
                        if (mSuccess) {
                            var mEmojiList: ArrayList<AllStickerList>? = null
                            mDaynamicEmojilist = response!!.status!!.result
                            if (mDaynamicEmojilist != null && mDaynamicEmojilist!!.size > 0) {
                                rvStickerGrid!!.layoutManager = mLinearLayoutManager
                                isDaynamic = true
                                if (progressChangedValue == 0) {
                                    cardlistAdapter = KbStickersAdapter(mThemeContext!!, mEmojiList, mDaynamicEmojilist, this@KeyboardSwitcher, this@KeyboardSwitcher, 70)
                                } else if (progressChangedValue == 50) {
                                    cardlistAdapter = KbStickersAdapter(mThemeContext!!, mEmojiList, mDaynamicEmojilist, this@KeyboardSwitcher, this@KeyboardSwitcher, 100)
                                } else if (progressChangedValue == 100) {
                                    cardlistAdapter = KbStickersAdapter(mThemeContext!!, mEmojiList, mDaynamicEmojilist, this@KeyboardSwitcher, this@KeyboardSwitcher, 140)
                                }
                                rvStickerGrid!!.adapter = cardlistAdapter
                                rlSeekbar!!.visibility = View.VISIBLE
                                rvStickerGrid!!.visibility = View.VISIBLE
                            } else {
                                rvStickerGrid!!.visibility = View.GONE
                                rlSeekbar!!.visibility = View.GONE
                            }
                        } else {
                            try {
                                rvStickerGrid!!.visibility = View.GONE
                                rlSeekbar!!.visibility = View.GONE
                                isDaynamic = false
                            } catch (e: Exception) {
                                rvStickerGrid!!.visibility = View.GONE
                                rlSeekbar!!.visibility = View.GONE
                                isDaynamic = false
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onError(anError: ANError) {
                        isDaynamic = false
                        rvStickerGrid!!.visibility = View.GONE
                        rlSeekbar!!.visibility = View.GONE
                    }
                })
    }


    private fun callPopulerStickerWs(mCategoryId: String) {
        AndroidNetworking.post(Constants.INSTANCE.URLLOCAL + Constants.INSTANCE.strWS_emoji_list_category)
                .addBodyParameter(Constants.INSTANCE.str_category, mCategoryId)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(StickerListByTagOrCategory::class.java, object : ParsedRequestListener<StickerListByTagOrCategory> {
                    override fun onResponse(response: StickerListByTagOrCategory?) {
                        Log.i("response", response.toString())
                        var mSuccess = false
                        if (response != null)
                            if (response.status!!.success == (Constants.INSTANCE.one)) {
                                mSuccess = true
                            }
                        if (mSuccess) {
                            var mEmojiList: ArrayList<AllStickerList>? = null
                            mDaynamicEmojilist = response!!.status!!.result
                            if (mDaynamicEmojilist != null && mDaynamicEmojilist!!.size > 0) {
                                rvStickerGrid!!.layoutManager = mLinearLayoutManager
                                isDaynamic = true
                                if (progressChangedValue == 0) {
                                    cardlistAdapter = KbStickersAdapter(mThemeContext!!, mEmojiList, mDaynamicEmojilist, this@KeyboardSwitcher, this@KeyboardSwitcher, 70)
                                } else if (progressChangedValue == 50) {
                                    cardlistAdapter = KbStickersAdapter(mThemeContext!!, mEmojiList, mDaynamicEmojilist, this@KeyboardSwitcher, this@KeyboardSwitcher, 100)
                                } else if (progressChangedValue == 100) {
                                    cardlistAdapter = KbStickersAdapter(mThemeContext!!, mEmojiList, mDaynamicEmojilist, this@KeyboardSwitcher, this@KeyboardSwitcher, 140)
                                }
                                rvStickerGrid!!.adapter = cardlistAdapter
                            } else {
                                isDaynamic = true
                                rvStickerGrid!!.visibility = View.VISIBLE
                                rlSeekbar!!.visibility = View.GONE
                            }
                        } else {
                            try {
                                rvStickerGrid!!.visibility = View.GONE
                                rlSeekbar!!.visibility = View.GONE
                                isDaynamic = false
                            } catch (e: Exception) {
                                rvStickerGrid!!.visibility = View.GONE
                                rlSeekbar!!.visibility = View.GONE
                                isDaynamic = false
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onError(anError: ANError) {
                        rvStickerGrid!!.visibility = View.GONE
                        isDaynamic = false
                        rlSeekbar!!.visibility = View.GONE
                    }
                })
    }
}