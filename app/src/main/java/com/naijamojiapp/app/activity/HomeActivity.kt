package com.naijamojiapp.app.activityimport android.content.Intentimport android.os.Buildimport android.os.Bundleimport android.os.Handlerimport android.text.TextUtilsimport android.util.Logimport android.view.Viewimport android.view.Windowimport android.view.WindowManagerimport android.widget.ImageViewimport android.widget.Toastimport androidx.appcompat.app.AppCompatActivityimport androidx.fragment.app.Fragmentimport androidx.fragment.app.FragmentTransactionimport androidx.recyclerview.widget.LinearLayoutManagerimport androidx.recyclerview.widget.RecyclerViewimport com.androidnetworking.utils.Utilsimport com.naijamojiapp.Rimport com.naijamojiapp.app.Bottombar.OnTabSelectListenerimport com.naijamojiapp.app.adapter.newadapter.CatTabInAppAdapterimport com.naijamojiapp.app.fragment.CategoryStickersFragmentimport com.naijamojiapp.app.fragment.RecentAndPopularFragmentimport com.naijamojiapp.app.interfaces.newApp.CatListTabClickedInAppimport com.naijamojiapp.app.roomDB.AppDatabaseimport com.naijamojiapp.app.roomDB.entity.CatListTabimport com.naijamojiapp.app.studiomode.StudioModeActivityimport com.naijamojiapp.app.utils.AutoStartHelperimport com.naijamojiapp.app.utils.Utilityclass HomeActivity : AppCompatActivity(), View.OnClickListener, OnTabSelectListener, CatListTabClickedInApp {    var ivSettings: ImageView? = null    var ivSearch: ImageView? = null    private var frag: Fragment? = null    internal var doubleBackToExitPressedOnce = false    //new    var mDatabase: AppDatabase? = null    var rvCatListTab : RecyclerView? = null    var mCatTabInAppAdapter : CatTabInAppAdapter? = null    var mCategoryTagList = ArrayList<CatListTab>()    var mCategoryId : String = ""    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        requestWindowFeature(Window.FEATURE_NO_TITLE)        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)        setContentView(R.layout.activity_home)        if (intent != null) {            if (intent.extras != null) {                if (intent.extras!!.containsKey("formKeyboard")) {                    if (!TextUtils.isEmpty(intent.extras!!.getString("formKeyboard"))) {                        if (intent.extras!!.getString("formKeyboard").equals("0")) {                            val intent = Intent(this, ConfigurationActivity::class.java)                            startActivity(intent)                        } else if (intent.extras!!.getString("formKeyboard").equals("1")) {                            val intent = Intent(this, ConfigurationActivity::class.java)                            startActivity(intent)                        } else if (intent.extras!!.getString("formKeyboard").equals("2")) {                            val intent = Intent(this, ConfigurationActivity::class.java)                            startActivity(intent)                        } else if (intent.extras!!.getString("formKeyboard").equals("3")) {                            val intent = Intent(this, ConfigurationActivity::class.java)                            startActivity(intent)                        } else if (intent.extras!!.getString("formKeyboard").equals("4")) {                            //Log.i("adapter"+"==Home",intent.extras!!.getString("selected_sticker") + " " + intent.extras!!.getString("selected_id"))                            val intentt = Intent(this, StudioModeActivity::class.java)                            intentt.putExtra("selected_sticker", intent.extras!!.getString("selected_sticker"))                            intentt.putExtra("selected_id", intent.extras!!.getString("selected_id"))                            intentt.putExtra("character_limit", intent.extras!!.getString("character_limit"))                            startActivity(intentt)                        }                    }                }            }        }        init()    }    private fun init() {        ivSettings = findViewById(R.id.iv_settings)        ivSettings!!.setOnClickListener(this)        ivSearch = findViewById(R.id.iv_search)        ivSearch!!.setOnClickListener(this)        ivSearch!!.setTag("0")        ivSearch!!.setImageResource(R.drawable.search_new)        frag = RecentAndPopularFragment.newInstance("CategoryView", "1")        if (frag != null) {            do {                getSupportFragmentManager().popBackStackImmediate()            } while (getSupportFragmentManager().getBackStackEntryCount() > 0)            if (frag != null) {                val ft = getSupportFragmentManager().beginTransaction()                ft.replace(R.id.main_fragment, frag!!)                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)                ft.commit()                getSupportFragmentManager().executePendingTransactions()            }        }        //Cat List BottomBar        mDatabase = AppDatabase.getDatabaseInstance(this@HomeActivity)        rvCatListTab = findViewById(R.id.rvCatListTab)        rvCatListTab!!.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)        mCatTabInAppAdapter = CatTabInAppAdapter(this@HomeActivity, mDatabase!!.userDao().allCatList as ArrayList<CatListTab>, this@HomeActivity)        rvCatListTab!!.adapter = mCatTabInAppAdapter        mCatTabInAppAdapter!!.firstPostionItemMakeSelected()    }    override fun onTabSelected(tabId: Int) {        openFragment(tabId)    }    fun openFragment(tabId: Int) {        Log.i("fragname", "openFragment CALLED!!!!!")        selectFragment(tabId)    }    override fun onClick(view: View?) {        when (view!!.id) {            R.id.iv_settings -> {                startActivity(Intent(this, SettingActivity::class.java))            }            R.id.iv_search -> {                mCatTabInAppAdapter!!.firstPostionItemMakeSelected()                if (ivSearch!!.getTag() == "0") {                    ivSearch!!.setTag("1")                    ivSearch!!.setImageResource(R.drawable.back_arrow)                    frag = RecentAndPopularFragment.newInstance("SearchView", "1")                } else {                    ivSearch!!.setTag("0")                    ivSearch!!.setImageResource(R.drawable.search_new)                    frag = RecentAndPopularFragment.newInstance("CategoryView", "1")                }                if (supportFragmentManager.backStackEntryCount > 0) {                    supportFragmentManager.popBackStack()                }                val ft = supportFragmentManager.beginTransaction()                ft.replace(R.id.main_fragment, frag!!, frag!!.getTag())                ft.addToBackStack(frag!!.javaClass.simpleName)                ft.commitAllowingStateLoss()            }        }    }    private fun selectFragment(tabId: Int) {        when (tabId) {            R.id.tab_recent -> {                ivSearch!!.setTag(ivSearch!!.getTag())                if (ivSearch!!.getTag() != null && ivSearch!!.getTag().equals("0")) {                    frag = RecentAndPopularFragment.newInstance("CategoryView", mCategoryId)                } else {                    frag = RecentAndPopularFragment.newInstance("SearchView", mCategoryId)                }            }        }        if (frag != null) {            do {                getSupportFragmentManager().popBackStackImmediate()            } while (getSupportFragmentManager().getBackStackEntryCount() > 0)            if (frag != null) {                val ft = getSupportFragmentManager().beginTransaction()                ft.replace(R.id.main_fragment, frag!!)                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)                ft.commit()                getSupportFragmentManager().executePendingTransactions()            }        }    }    override fun onBackPressed() {        try {            if (doubleBackToExitPressedOnce) {                val homeIntent = Intent(Intent.ACTION_MAIN)                homeIntent.addCategory(Intent.CATEGORY_HOME)                homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP                startActivity(homeIntent)                super.onBackPressed()                return            }            this.doubleBackToExitPressedOnce = true            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show()            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)        } catch (e: Exception) {        }    }    override fun mTabClick(resultm: CatListTab?, position: Int, isClose: Boolean?, isLogin: String?, isPositionZero: Boolean?) {        mCategoryId = resultm!!.id        if(isPositionZero!!.equals(true)){            ivSearch!!.setTag(ivSearch!!.getTag())            if (ivSearch!!.getTag() != null && ivSearch!!.getTag().equals("0")) {                frag = RecentAndPopularFragment.newInstance("CategoryView", "0")            } else {                frag = RecentAndPopularFragment.newInstance("SearchView", "0")            }        }else{            ivSearch!!.setTag("0")            ivSearch!!.setImageResource(R.drawable.search_new)            frag = CategoryStickersFragment.newInstance(resultm.id!!)        }        if (frag != null) {            do {                getSupportFragmentManager().popBackStackImmediate()            } while (getSupportFragmentManager().getBackStackEntryCount() > 0)            if (frag != null) {                val ft = getSupportFragmentManager().beginTransaction()                ft.replace(R.id.main_fragment, frag!!)                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)                ft.commit()                getSupportFragmentManager().executePendingTransactions()            }        }    }}