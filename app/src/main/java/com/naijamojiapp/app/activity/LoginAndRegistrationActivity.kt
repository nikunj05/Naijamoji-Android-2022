package com.naijamojiapp.app.activity


import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.naijamojiapp.app.fragment.LoginFragment
import com.naijamojiapp.app.fragment.SignUpFragment
import com.naijamojiapp.R

class LoginAndRegistrationActivity : AppCompatActivity() {
    private var allTabs: TabLayout? = null
    private var fragmentOne: Fragment? = null
    private var fragmentTwo: Fragment? = null
    private var tvSignUpNew: TextView? = null
    private var tvSignInNew: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login_and_signup)
        setUpViews()
    }

    private fun setUpViews() {
        allTabs = findViewById(R.id.tabs)
        tvSignUpNew = findViewById(R.id.tvSignUpNew)
        tvSignInNew = findViewById(R.id.tvSignInNew)
        tvSignUpNew!!.setTextColor(ContextCompat.getColor(this@LoginAndRegistrationActivity, R.color.gray_new))
        tvSignInNew!!.setTextColor(ContextCompat.getColor(this@LoginAndRegistrationActivity, R.color.white))
        getAllWidgets()
    }

    private fun getAllWidgets() {
        val linearLayout = allTabs!!.getChildAt(0) as LinearLayout
        linearLayout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        val drawable = GradientDrawable()
        drawable.setColor(ContextCompat.getColor(this.getApplicationContext(), R.color.grey))
        drawable.setSize(0, 0)
        linearLayout.dividerPadding = 5
        linearLayout.dividerDrawable = drawable
        bindWidgetsWithAnEvent()
    }

    private fun bindWidgetsWithAnEvent() {
        allTabs!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                setCurrentTabFragment(tab.position)
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        setupTabLayout()
    }

    private fun setupTabLayout() {
        allTabs!!.removeAllTabs()

        fragmentOne = SignUpFragment.newInstance()
        fragmentTwo = LoginFragment.newInstance()

        allTabs!!.addTab(allTabs!!.newTab().setText(getString(R.string.signup)))
        allTabs!!.addTab(allTabs!!.newTab().setText(getString(R.string.signin)))
    }

    private fun setCurrentTabFragment(tabPosition: Int) {
        when (tabPosition) {
            0 ->{
                tvSignUpNew!!.setTextColor(ContextCompat.getColor(this@LoginAndRegistrationActivity, R.color.white))
                tvSignInNew!!.setTextColor(ContextCompat.getColor(this@LoginAndRegistrationActivity, R.color.gray_new))
                replaceFragment(this.fragmentOne!!)
            }
            1 ->{
                tvSignUpNew!!.setTextColor(ContextCompat.getColor(this@LoginAndRegistrationActivity, R.color.gray_new))
                tvSignInNew!!.setTextColor(ContextCompat.getColor(this@LoginAndRegistrationActivity, R.color.white))
                replaceFragment(this.fragmentTwo!!)
            }
        }
    }

    fun replaceFragment(fragment: Fragment) {
        try {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.frame_container, fragment)
            //ft.addToBackStack(ft!!.javaClass?.name)
            //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            ft.commit()
        } catch (e: Exception) {
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}




