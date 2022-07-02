package com.naijamojiapp.app.cropview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RelativeLayout;


import androidx.appcompat.app.AppCompatActivity;

import com.naijamojiapp.R;
import com.naijamojiapp.app.utils.Utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CropActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout rlCancel, rlDone;
    private CropperView cvImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        ids();
        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                if (getIntent().getExtras().containsKey("image")) {
                    try {
                        //mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(getIntent().getExtras().getString("image")));
                        final Uri imageUri = Uri.parse(getIntent().getExtras().getString("image"));
                        final Bitmap selectedImage = Utility.decodeFile(Utility.getPath(CropActivity.this, imageUri));
                        /*final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);*/
                        cvImage.setImageBitmap(selectedImage);

                    } catch (Exception e) {
                        //handle exception
                    }

                    /*Uri imageURI = Utility.getImageUri(getIntent().getExtras().getString("image"));             // photoFile is a File class.
                    String imagePath = Utility.getPath(CropActivity.this, imageURI);
                    Bitmap myBitmap  = BitmapFactory.decodeFile(getIntent().getExtras().getString("image"));*/

//                mBitmap = getRoundedCornerBitmap(mBitmap,0);
//            mBitmap = BitmapFactory.decodeFile(imageGalleryAdapter.getItem(0).getImageUri().toString());
                    cvImage.setMaxZoom(cvImage.getWidth());
                }
            }
        }


    }

    private Bitmap imageOreintationValidator(String path) {
        Bitmap bitmap = null;
        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bitmap;
    }



    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void ids() {
        rlCancel = (RelativeLayout) findViewById(R.id.rl_cancel);
        rlCancel.setOnClickListener(this);
        rlDone = (RelativeLayout) findViewById(R.id.rl_done);
        rlDone.setOnClickListener(this);
        cvImage = (CropperView) findViewById(R.id.crop_image);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_cancel:
                finish();
                break;
            case R.id.rl_done:
                cvImage.getCroppedBitmapAsync(new CropperCallback() {
                    @Override
                    public void onCropped(Bitmap bitmap) {
                        if (bitmap != null) {
                            Uri imagePath = getImageUri(CropActivity.this, bitmap);
                            Intent intent = new Intent();
                            intent.putExtra("cropimage", imagePath.toString());
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }

                    @Override
                    public void onOutOfMemoryError() {

                    }
                });
                break;
        }
    }
}