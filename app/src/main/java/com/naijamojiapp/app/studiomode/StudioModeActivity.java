package com.naijamojiapp.app.studiomode;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.transition.ChangeBounds;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.naijamojiapp.R;
import com.naijamojiapp.app.cusromsharedialog.ShareDialog;
import com.naijamojiapp.app.customview.CustomDialog;
import com.naijamojiapp.app.customview.CustomProgressDialog;
import com.naijamojiapp.app.response.CommonResponse;
import com.naijamojiapp.app.studiomode.base.BaseActivity;
import com.naijamojiapp.app.studiomode.filters.FilterListener;
import com.naijamojiapp.app.studiomode.filters.FilterViewAdapter;
import com.naijamojiapp.app.studiomode.tools.EditingToolsAdapter;
import com.naijamojiapp.app.studiomode.tools.ToolType;
import com.naijamojiapp.app.utils.CheckConnection;
import com.naijamojiapp.app.utils.Constants;
import com.naijamojiapp.app.utils.FileUtils;
import com.naijamojiapp.app.utils.Preferences;
import com.naijamojiapp.app.utils.Utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import ja.burhanrashid52.photoeditor.SaveSettings;
import ja.burhanrashid52.photoeditor.TextStyleBuilder;
import ja.burhanrashid52.photoeditor.ViewType;

public class StudioModeActivity extends BaseActivity implements OnPhotoEditorListener,
        View.OnClickListener,
        PropertiesBSFragment.Properties,
        EmojiBSFragment.EmojiListener,
        StickerBSFragment.StickerListener, EditingToolsAdapter.OnItemSelected, FilterListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = StudioModeActivity.class.getSimpleName();
    public static final String EXTRA_IMAGE_PATHS = "extra_image_paths";
    private static final int CAMERA_REQUEST = 52;
    private static final int PICK_REQUEST = 53;
    private PhotoEditor mPhotoEditor;
    private PhotoEditorView mPhotoEditorView;
    private PropertiesBSFragment mPropertiesBSFragment;
    private EmojiBSFragment mEmojiBSFragment;
    private StickerBSFragment mStickerBSFragment;
    private TextView mTxtCurrentTool;
    private Typeface mWonderFont;
    private RecyclerView mRvTools, mRvFilters;
    private EditingToolsAdapter mEditingToolsAdapter = new EditingToolsAdapter(this);
    private FilterViewAdapter mFilterViewAdapter = new FilterViewAdapter(this);
    private RelativeLayout mRootView;
    private ConstraintSet mConstraintSet = new ConstraintSet();
    private boolean mIsFilterVisible;
    private TextView tvAddtext;
    private ImageView ivBack, ivSelectText;
    private ImageView imgUndo, imgRedo, imgCamera, imgGallery, imgSave, imgClose;

    private String typefaceGloble = "";
    private String mImageUrl = "";
    private String mImageId = "";
    private String mCharacterlimit = "";
    private RelativeLayout rlClose;
    private RelativeLayout rlBack;

    private ShareDialog shareDialog;
    SwitchCompat switchCompatFontMode;
    TextView tvFontMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen(this);
        setContentView(R.layout.activity_edit_image);
        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                if (getIntent().getExtras().containsKey("selected_sticker")) {
                    if (!TextUtils.isEmpty(getIntent().getExtras().getString("selected_sticker"))) {
                        mImageUrl = getIntent().getExtras().getString("selected_sticker");
                        mImageId = getIntent().getExtras().getString("selected_id");
                        mCharacterlimit = getIntent().getExtras().getString("character_limit");
                        Preferences.Companion.getINSTANCE().SavePrefValue(Preferences.Companion.getINSTANCE().getPREF_mCharacterLimit(),/*mCharacterlimit,*/"");
                    }
                }
            }
        }
        initViews();
        mPropertiesBSFragment = new PropertiesBSFragment();
        mEmojiBSFragment = new EmojiBSFragment();
        mStickerBSFragment = new StickerBSFragment();
        mStickerBSFragment.setStickerListener(this);
        mEmojiBSFragment.setEmojiListener(this);
        mPropertiesBSFragment.setPropertiesChangeListener(this);

        LinearLayoutManager llmTools = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvTools.setLayoutManager(llmTools);
        mRvTools.setAdapter(mEditingToolsAdapter);

        LinearLayoutManager llmFilters = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvFilters.setLayoutManager(llmFilters);
        mRvFilters.setAdapter(mFilterViewAdapter);

        // Typeface type = Typeface.createFromAsset(getAssets(),"fonts/1.ttf");

        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(true) // set flag to make text scalable when pinch
                // .setDefaultTextTypeface(type)
                // .setDefaultEmojiTypeface(type)
                .build(); // build photo editor sdk

        mPhotoEditor.setOnPhotoEditorListener(this);


        if (mImageUrl != null && !TextUtils.isEmpty(mImageUrl)) {
            Glide.with(this).
                    load(mImageUrl).
                    into(mPhotoEditorView.getSource());
        } else {
            Toast.makeText(this, "File not found...", Toast.LENGTH_SHORT).show();
            finish();
        }
//        mPhotoEditorView.getSource().setImageDrawable(drawable);
    }


    private void initViews() {
        mPhotoEditorView = findViewById(R.id.photoEditorView);
        mTxtCurrentTool = findViewById(R.id.txtCurrentTool);
        mRvTools = findViewById(R.id.rvConstraintTools);
        mRvFilters = findViewById(R.id.rvFilterView);
        mRootView = findViewById(R.id.rootView);

        ivSelectText = findViewById(R.id.iv_select_text);
        ivSelectText.setOnClickListener(this);

        tvAddtext = findViewById(R.id.tv_addtext);
        tvAddtext.setOnClickListener(this);

        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);

        rlBack = findViewById(R.id.rl_back);
        rlBack.setOnClickListener(this);

        imgUndo = findViewById(R.id.imgUndo);
        imgUndo.setOnClickListener(this);

        imgRedo = findViewById(R.id.imgRedo);
        imgRedo.setOnClickListener(this);

        imgCamera = findViewById(R.id.imgCamera);
        imgCamera.setOnClickListener(this);

        imgGallery = findViewById(R.id.imgGallery);
        imgGallery.setOnClickListener(this);

        imgSave = findViewById(R.id.imgSave);
        imgSave.setOnClickListener(this);

        imgClose = findViewById(R.id.imgClose);
        imgClose.setOnClickListener(this);

        rlClose = findViewById(R.id.rlClose);
        rlClose.setOnClickListener(this);
        tvFontMode = findViewById(R.id.tvFontMode);
        switchCompatFontMode = findViewById(R.id.switch_font_mode);
        switchCompatFontMode.setOnCheckedChangeListener(this);

       if(Preferences.Companion.getINSTANCE().getGetFontMode().equals("1")){
           switchCompatFontMode.setChecked(true);
           tvFontMode.setText(getString(R.string.disable_black_mode));
       }else {
           switchCompatFontMode.setChecked(false);
           tvFontMode.setText(getString(R.string.enable_black_mode));
       }
    }




    //Long click on added textview
    @Override
    public void onEditTextChangeListener(final View rootView, String text, int colorCode, String typeface) {
        TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(this, text, colorCode, typeface);
        textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
            @Override
            public void onDone(Spannable inputText, int colorCode, String typeface) {
                final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                styleBuilder.withTextColor(colorCode);
                Typeface type2 = Typeface.createFromAsset(getAssets(), typeface);
                styleBuilder.withTextFont(type2);
                mPhotoEditor.editText(rootView, inputText, styleBuilder);
                mTxtCurrentTool.setText(R.string.label_text);
            }
        });
    }
    private Spannable fontcolor(String text) {
        Log.e("colorCodes" + "===>", text);

        int[] androidColors = getResources().getIntArray(R.array.androidcolors);
        Spannable word = new SpannableString(text);
        if(Preferences.Companion.getINSTANCE().getGetFontMode().equals("0")){
            try {
                for (int i = 0; i < word.length(); i++) {
                    word.setSpan(new ForegroundColorSpan(androidColors[new Random().nextInt(androidColors.length)]), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Log.e("colorCodes" + "===>", word.toString() + "===========" + i + "========" + new Gson().toJson(androidColors[new Random().nextInt(androidColors.length)]));
                }
                return word;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return word;
    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.d(TAG, "onAddViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.d(TAG, "onRemoveViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_select_text:
                break;

            case R.id.tv_addtext:
                editViewDisplay();
                break;

            case R.id.imgUndo:
                mPhotoEditor.undo();
                break;

            case R.id.imgRedo:
                mPhotoEditor.redo();
                break;

            case R.id.imgSave:
                saveImage();
                break;

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.rl_back:
                onBackPressed();
                break;

            case R.id.imgCamera:
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                break;

            case R.id.imgGallery:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_REQUEST);
                break;

            case R.id.rlClose:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    mPhotoEditor.clearAllViews();
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    mPhotoEditorView.getSource().setImageBitmap(photo);
                    break;
                case PICK_REQUEST:
                    try {
                        mPhotoEditor.clearAllViews();
                        Uri uri = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        mPhotoEditorView.getSource().setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    @Override
    public void onColorChanged(int colorCode) {
        mPhotoEditor.setBrushColor(colorCode);
        mTxtCurrentTool.setText(R.string.label_brush);
    }

    @Override
    public void onOpacityChanged(int opacity) {
        mPhotoEditor.setOpacity(opacity);
        mTxtCurrentTool.setText(R.string.label_brush);
    }

    @Override
    public void onBrushSizeChanged(int brushSize) {
        mPhotoEditor.setBrushSize(brushSize);
        mTxtCurrentTool.setText(R.string.label_brush);
    }

    @Override
    public void onEmojiClick(String emojiUnicode) {
        mPhotoEditor.addEmoji(emojiUnicode);
        mTxtCurrentTool.setText(R.string.label_emoji);

    }

    @Override
    public void onStickerClick(Bitmap bitmap) {
        mPhotoEditor.addImage(bitmap);
        mTxtCurrentTool.setText(R.string.label_sticker);
    }

    @Override
    public void isPermissionGranted(boolean isGranted, String permission) {
        if (isGranted) {
            saveImage();
        }
    }

    private void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you want to exit without edit sticker ?");
      /*  builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveImage();
            }
        });*/
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();

    }

    @Override
    public void onFilterSelected(PhotoFilter photoFilter) {
        mPhotoEditor.setFilterEffect(photoFilter);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onToolSelected(ToolType toolType) {
        switch (toolType) {
           /* case BRUSH:
                mPhotoEditor.setBrushDrawingMode(true);
                mTxtCurrentTool.setText(R.string.label_brush);
                mPropertiesBSFragment.show(getSupportFragmentManager(), mPropertiesBSFragment.getTag());
                break;*/
            case TEXT:
                editViewDisplay();
                break;
           /* case ERASER:
                mPhotoEditor.brushEraser();
                mTxtCurrentTool.setText(R.string.label_eraser);
                break;
            case FILTER:
                mTxtCurrentTool.setText(R.string.label_filter);
                showFilter(true);
                break;
            case EMOJI:
                mEmojiBSFragment.show(getSupportFragmentManager(), mEmojiBSFragment.getTag());
                break;
            case STICKER:
                mStickerBSFragment.show(getSupportFragmentManager(), mStickerBSFragment.getTag());
                break;*/
        }
    }

    private void editViewDisplay() {
        int[] androidColors = getResources().getIntArray(R.array.androidcolors);
        int mColorCode = androidColors[new Random().nextInt(androidColors.length)];

        final TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(this, "", Color.WHITE, typefaceGloble);
        textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
            @Override
            public void onDone(Spannable inputText, int colorCode, String typeface) {
                final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                //styleBuilder.withTextColor(colorCode);
                Typeface type2 = Typeface.createFromAsset(getAssets(), typeface);
                styleBuilder.withTextFont(type2);
                typefaceGloble = typeface;
                mPhotoEditor.addText(inputText, styleBuilder);
               /* if(inputText.length() < 15){
                }else{
                    CustomDialog.Companion.getInstance().showalert(StudioModeActivity.this, getResources().getString(R.string.enter_fifteen_char));
                }*/
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void showFilter(boolean isVisible) {
        mIsFilterVisible = isVisible;
        //     mConstraintSet.clone(mRootView);
        if (isVisible) {
            mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.START);
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.START);
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.END,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
        } else {
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
            mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.END);
        }

        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(350);
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
//        TransitionManager.beginDelayedTransition(mRootView, changeBounds);
//        mConstraintSet.applyTo(mRootView);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed() {
        if (mIsFilterVisible) {
            showFilter(false);
            mTxtCurrentTool.setText(R.string.app_name);
        } else if (!mPhotoEditor.isCacheEmpty()) {
            showSaveDialog();
        } else {
            super.onBackPressed();
            finish();
        }
    }


    @SuppressLint("MissingPermission")
    private void saveImage() {
        if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//            showLoading("Saving...");
            File file = new File(getExternalCacheDir()
                    + File.separator + ""
                    + System.currentTimeMillis() + ".png");
            try {
                file.createNewFile();

                SaveSettings saveSettings = new SaveSettings.Builder()
                        .setClearViewsEnabled(true)
                        .setTransparencyEnabled(true)
                        .build();
                mPhotoEditor.saveAsFile(file.getAbsolutePath(), saveSettings, new PhotoEditor.OnSaveListener() {
                    @Override
                    public void onSuccess(@NonNull String imagePath) {
                        Log.e("Print email","===>"+imagePath);
                        //hideLoading();
                        //showSnackbar("Image Saved Successfully");
                        //mPhotoEditorView.getSource().setImageURI(Uri.fromFile(new File(imagePath)));

                        if (Uri.fromFile(new File(imagePath)) != null && mImageId != null) {
                            if (CheckConnection.getInstance(StudioModeActivity.this).isConnectingToInternet()) {
                                callStudioModeWs(Uri.fromFile(new File(imagePath)));
                            } else {
                                CustomDialog.Companion.getInstance().showalert(StudioModeActivity.this, getResources().getString(R.string.check_internet_connection));
                            }
                        }


                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) {
//                        hideLoading();
//                        showSnackbar("Failed to save Image");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
//                hideLoading();
//                showSnackbar(e.getMessage());
            }
        }
    }




    private void callStudioModeWs(final Uri uri) {
        final File mEditedSticker = getMultipartImgEditStickerPic(uri);
        final CustomProgressDialog progressDialog = new CustomProgressDialog(StudioModeActivity.this, R.style.progress_dialog_text_style);
        progressDialog.show();

        AndroidNetworking.upload(Constants.Companion.getINSTANCE().getURLLOCAL() + Constants.Companion.getINSTANCE().getStrWS_studio_mode())
                .addMultipartFile(Constants.Companion.getINSTANCE().getStr_sticker_url(), mEditedSticker)
                .addMultipartParameter(Constants.Companion.getINSTANCE().getStr_emoji_id(), mImageId)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(CommonResponse.class, new ParsedRequestListener<CommonResponse>() {
                    @Override
                    public void onResponse(CommonResponse response) {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();

                        boolean mSuccess = false;
                        if (response != null)
                            if (response.getStatus().getSuccess().equalsIgnoreCase(Constants.Companion.getINSTANCE().getOne())) {
                                mSuccess = true;
                            }

                        if (mSuccess) {
                          /*ContentResolver contentResolver = getContentResolver();
                            deleteFileFromMediaStore(contentResolver, FileUtils.getFile(StudioModeActivity.this, Uri.fromFile(new File(imagePath))));*/
                            //  Toast.makeText(StudioModeActivity.this, "Success........", Toast.LENGTH_SHORT).show();
                           /* if (uri != null) {
                                deleteDir(new File(String.valueOf(uri)));
                            }*/
                           try {
                               getContentResolver().delete(uri, null, null);
                           }catch (Exception e){}
                           ShowSaveAndShareDialog(uri);
                        } else {
                            try {
                                //  Toast.makeText(StudioModeActivity.this, "false........", Toast.LENGTH_SHORT).show();
                                CustomDialog.Companion.getInstance().showalert(StudioModeActivity.this, response.getStatus().getMessage());
                            } catch (Exception e) {
                                CustomDialog.Companion.getInstance().showalert(StudioModeActivity.this, getString(R.string.error_message500));
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                        //    Toast.makeText(StudioModeActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void ShowSaveAndShareDialog(final Uri uri) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_studiomode);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        Button btnShare = dialog.findViewById(R.id.btnShare);
        Button btnSaveAndShare = dialog.findViewById(R.id.btnSaveAndShare);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Unique","1");
                share(uri);
                dialog.dismiss();
            }
        });

        btnSaveAndShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(StudioModeActivity.this)
                        .asBitmap()
                        .load(uri)
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable final GlideException e,
                                                        final Object model, final Target<Bitmap> target,
                                                        final boolean isFirstResource) {
                                Log.i("Unique","2");
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap bitmap, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                Log.i("Unique","3");
                                saveImageToInternalStorage(bitmap,uri);
                                return false;
                            }

                        }).priority(com.bumptech.glide.Priority.IMMEDIATE)
                        .submit();

                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                goToMainActivity();
            }
        });
        dialog.show();
    }

    private void share(final Uri uri) {
        Glide.with(this)
                .asBitmap()
                .load(uri)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable final GlideException e,
                                                final Object model, final Target<Bitmap> target,
                                                final boolean isFirstResource) {
                        Log.i("Unique","2");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap bitmap, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        Log.i("Unique","3");
                        shareBitmap(bitmap,uri );
                        return false;
                    }

                }).priority(com.bumptech.glide.Priority.IMMEDIATE)
                .submit();
    }

    private void shareBitmap(Bitmap bitmap ,Uri image ) {
        try {
            File file = new File(this.getExternalCacheDir(), "share.png");
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
            Canvas canvas = new  Canvas(newBitmap);
            canvas.drawColor(Color.TRANSPARENT);
            canvas.drawBitmap(bitmap, 0f, 0f, null);
            newBitmap.setHasAlpha(true);
            newBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            try {
                fOut.flush();
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            file.setReadable(true, false);
            ShareDialog.Builder  builder  = new ShareDialog.Builder();
            builder.setType(ShareDialog.TYPE_IMAGE);
            builder.showAsList(true);
            shareDialog = builder.build();
            shareDialog.setShareContent(Uri.fromFile(file).toString(), String.valueOf(image),bitmap);
            shareDialog.show(this.getSupportFragmentManager());
            Log.i("Unique","4");
        } catch (Exception e) {
            Log.i("Unique","5");
            e.printStackTrace();
        }
    }

    // Custom method to save a bitmap into internal storage
    protected Uri saveImageToInternalStorage(Bitmap bitmap,Uri uri) {
        try {
           // String root = getExternalCacheDir().getName();
            File myDir = new File(getFilesDir() + "/Naijamoji");
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            Random generator = new Random();
            int n = 10000;
            n = generator.nextInt(n);
            String fname = "Image-" + n + ".png";
            File file = new File(myDir, fname);
            if (file.exists())
                file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
                Canvas canvas = new Canvas(newBitmap);
                canvas.drawColor(Color.TRANSPARENT);
                canvas.drawBitmap(bitmap, 0f, 0f, null);
                newBitmap.setHasAlpha(true);
                newBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                file.setReadable(true, false);
                share(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
            MediaScannerConnection.scanFile(this, new String[]{file.toString()}, new String[]{file.getName()}, null);
            return Uri.parse(file.getAbsolutePath());
        } catch (Exception e) {
            return Uri.EMPTY;
        }
    }


    private void goToMainActivity() {
               /*Intent intent = new Intent(StudioModeActivity.this, HomeActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(intent);*/
               finish();
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    private File getMultipartImgEditStickerPic(Uri uri) {
        if (uri != null) {
            if (!TextUtils.isEmpty(uri.toString())) {
                File file = FileUtils.getFile(this, uri);
                return file;
            } else {
                return null;
            }
        } else {
            return FileUtils.getFile(this, uri);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if(isChecked){
            Preferences.Companion.getINSTANCE().SavePrefValue(Preferences.Companion.getINSTANCE().getPREF_FONT_MODE(),"1");
            tvFontMode.setText(getString(R.string.disable_black_mode));
        }else {
            Preferences.Companion.getINSTANCE().SavePrefValue(Preferences.Companion.getINSTANCE().getPREF_FONT_MODE(),"0");
            tvFontMode.setText(getString(R.string.enable_black_mode));
        }
    }
}
