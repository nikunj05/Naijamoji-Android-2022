package com.naijamojiapp.app.activityimport android.content.Intentimport android.os.Bundleimport android.view.Viewimport android.view.Windowimport android.view.WindowManagerimport android.widget.RelativeLayoutimport androidx.appcompat.app.AppCompatActivityimport androidx.multidex.BuildConfigimport com.naijamojiapp.app.sqlitedb.DatabaseHelperimport com.naijamojiapp.app.utils.Preferencesimport com.naijamojiapp.Rimport com.naijamojiapp.app.roomDB.AppDatabaseclass SettingActivity : AppCompatActivity(), View.OnClickListener {    var rlBack: RelativeLayout? = null    var rlLogout: RelativeLayout? = null    var rlChangeAvatarStyle: RelativeLayout? = null    var rlConfiguration: RelativeLayout? = null    var rlHowToUseNaijamoji: RelativeLayout? = null    var rlSupport: RelativeLayout? = null    var rlInviteFriend: RelativeLayout? = null    var rlPrivacyPolicy: RelativeLayout? = null    var rlTermsOfService: RelativeLayout? = null    var rlLicenses: RelativeLayout? = null    var rlMyAccount: RelativeLayout? = null    var rlYourStickers: RelativeLayout? = null    lateinit var database: DatabaseHelper    var mDatabase : AppDatabase? = null    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        requestWindowFeature(Window.FEATURE_NO_TITLE)        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)        setContentView(R.layout.activity_setting)        init()    }    private fun init() {        mDatabase = AppDatabase.getDatabaseInstance(this@SettingActivity)//        database = DatabaseHelper(this@SettingActivity)        rlBack = findViewById(R.id.rl_back)        rlBack!!.setOnClickListener(this)        rlLogout = findViewById(R.id.rl_logout)        rlLogout!!.setOnClickListener(this)        rlYourStickers = findViewById(R.id.rl_your_stickers)        rlYourStickers!!.setOnClickListener(this)        rlChangeAvatarStyle = findViewById(R.id.rl_change_avatar_style)        rlChangeAvatarStyle!!.setOnClickListener(this)        rlConfiguration = findViewById(R.id.rl_configuration)        rlConfiguration!!.setOnClickListener(this)        rlHowToUseNaijamoji = findViewById(R.id.rl_how_to_use_naijamoji)        rlHowToUseNaijamoji!!.setOnClickListener(this)        rlSupport = findViewById(R.id.rl_support)        rlSupport!!.setOnClickListener(this)        rlInviteFriend = findViewById(R.id.rl_invite_friend)        rlInviteFriend!!.setOnClickListener(this)        rlPrivacyPolicy = findViewById(R.id.rl_privacy_policy)        rlPrivacyPolicy!!.setOnClickListener(this)        rlTermsOfService = findViewById(R.id.rl_terms_of_service)        rlTermsOfService!!.setOnClickListener(this)        rlLicenses = findViewById(R.id.rl_licenses)        rlLicenses!!.setOnClickListener(this)        rlMyAccount = findViewById(R.id.rl_my_account)        rlMyAccount!!.setOnClickListener(this)    }    override fun onClick(view: View?) {        when (view!!.id) {            R.id.rl_back -> {                finish()            }            R.id.rl_your_stickers-> {                startActivity(Intent(this, YourStickersActivity::class.java))            }            R.id.rl_logout -> {                //database.deleteTable()                mDatabase!!.userDao().deleteRecentTable()                val intent = Intent(this, LoginActivity::class.java)                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)                Preferences.INSTANCE!!.ClearPrefsValue()                startActivity(intent)            }            R.id.rl_change_avatar_style -> {                startActivity(Intent(this, ChangeAvatarStyleActivity::class.java))            }            R.id.rl_configuration -> {                startActivity(Intent(this, ConfigurationActivity::class.java))            }            R.id.rl_how_to_use_naijamoji -> {                startActivity(Intent(this, InstuctionActivity::class.java))            }            R.id.rl_support -> {                startActivity(Intent(this, SupportActivity::class.java))            }            R.id.rl_invite_friend -> {              //  startActivity(Intent(this, InviteFriendsActivity::class.java))                try {                    val shareIntent = Intent(Intent.ACTION_SEND)                    shareIntent.type = "text/plain"                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Naijamoji")                    var shareMessage = "Naijamoji App\n"                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.naijamojiapp"                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)                    startActivity(Intent.createChooser(shareIntent, "choose one"))                } catch (e: Exception) { //e.toString();                }            }            R.id.rl_privacy_policy -> {                startActivity(Intent(this, PrivacyPolicyActivity::class.java))            }            R.id.rl_terms_of_service -> {                startActivity(Intent(this, TermsOfServiceActivity::class.java))            }            R.id.rl_licenses -> {                startActivity(Intent(this, LicensesActivity::class.java))            }            R.id.rl_my_account -> {                startActivity(Intent(this, MyAccountActivity::class.java))            }        }    }}