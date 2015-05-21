package com.mabi87.aimagecrope;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mabi87.imagecroper.ImageCroper;

import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    // Layout Components
    private FrameLayout mContainerImageCroper;
    private TextView mTextViewWidth;
    private TextView mTextViewHeight;
    private TextView mTextBoxX;
    private TextView mTextBoxY;
    private TextView mTextBoxWidth;
    private TextView mTextBoxHeight;

    // Components
    private ImageCroper mImageCroper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadGUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            mImageCroper = new ImageCroper(getApplicationContext(), selectedImageUri);

            mImageCroper.setOnCropViewChangedListener(new ImageCroper.OnCropViewChangedListener() {
                @Override
                public void onCropViewChanged(int viewWidth, int viewHeight) {
                    mTextViewWidth.setText("view width: " + viewWidth);
                    mTextViewHeight.setText("view height: " + viewHeight);
                }
            });

            mImageCroper.setOnCropBoxChangedListener(new ImageCroper.OnCropBoxChangedListener() {
                @Override
                public void onCropBoxChange(int boxX, int boxY, int boxWidth, int boxHeight) {
                    mTextBoxX.setText("box x: " + boxX);
                    mTextBoxY.setText("box y: " + boxY);
                    mTextBoxWidth.setText("box width: " + boxWidth);
                    mTextBoxHeight.setText("box height: " + boxHeight);
                }
            });

            mContainerImageCroper.addView(mImageCroper);
        } else {
            finish();
        }
    }

    private void loadGUI() {
        setContentView(R.layout.activity_main);

        mContainerImageCroper = (FrameLayout) findViewById(R.id.containerImageCroper);

        mTextViewWidth = (TextView) findViewById(R.id.textViewWidth);
        mTextViewHeight = (TextView) findViewById(R.id.textViewHeight);
        mTextBoxX = (TextView) findViewById(R.id.textBoxX);
        mTextBoxY = (TextView) findViewById(R.id.textBoxY);
        mTextBoxWidth = (TextView) findViewById(R.id.textBoxWidth);
        mTextBoxHeight = (TextView) findViewById(R.id.textBoxHeight);
    }

    public void onButtonLoadClicked(View v) {
        Intent lIntent = new Intent(Intent.ACTION_PICK);
        lIntent.setType("image/*");
        lIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(lIntent, 1000);
    }

    public void onButtonCropClicked(View v) {
        if(mImageCroper != null) {
            Bitmap cropedImage = mImageCroper.crop();

            try {
                FileOutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/thumb.png");
                cropedImage.compress(Bitmap.CompressFormat.PNG, 40, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
