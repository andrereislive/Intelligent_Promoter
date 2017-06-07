package com.a1500fh.intelligent_promoter;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.a1500fh.utils.ImageCapture;
import com.a1500fh.utils.PathUtil;
import com.a1500fh.utils.ZoomImageThumb;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;

    private static String IMG_PATH_CAMERA; // se capturar da camera entao salva nesta pasta

    private String imgPath; // caminho da imagem escolhida

    private ImageView ivShelf;
    private ImageButton btnLoadShelf;
    private ImageButton btnListProduct;
    private Bitmap chosenImage;


    private static final int RESULT_LOAD_IMG = 1;

    ZoomImageThumb zoom = new ZoomImageThumb();
// ############ Parei akii ############################
    // ##################################################

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
            }
            if (checkSelfPermission(Manifest.permission.MANAGE_DOCUMENTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.MANAGE_DOCUMENTS}, 1);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
            }


        }





        setContentView(R.layout.activity_main);
        IMG_PATH_CAMERA = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + File.separator + "image.jpg";
        imgPath = IMG_PATH_CAMERA;
        ivShelf = (ImageView) findViewById(R.id.ivShelf);

        btnLoadShelf = (ImageButton) findViewById(R.id.btnCamera);
        btnListProduct = (ImageButton) findViewById(R.id.btnLoad);

        btnLoadShelf.setOnClickListener(this);
        ivShelf.setOnClickListener(this);
        btnListProduct.setOnClickListener(this);


        Log.i(TAG, imgPath);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ivShelf:
                if (chosenImage != null)
                    zoom.zoomImageFromThumb(ivShelf, chosenImage, (ImageView) findViewById(R.id.expanded_image), R.id.container, this);
                break;
            case R.id.btnCamera:
                selectImage();
                break;

            case R.id.btnLoad:
                File file = new File(imgPath);
                if (file.exists()) {
                    Intent intent = new Intent(this, ShelfShareActivity.class);
                    intent.putExtra("image_source", imgPath);

                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Please take a photo of the shelf first", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (chosenImage != null) {
            ivShelf.setImageBitmap(chosenImage);
        }
    }


    private void selectImage() {
        Log.i(TAG, "Select Image");
        final CharSequence[] items = {"Camera", "Gallery",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Camera")) {
                    launchCameraFullRes();
                } else if (items[item].equals("Gallery")) {
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void launchCameraFullRes() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imgPath = IMG_PATH_CAMERA;
        File file = new File(imgPath);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);



    }

    private void galleryIntent() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
// Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Check that request code matches ours:
        if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE) {
            //Get our saved file into a bitmap object:
            //File file = new File(imgPath);

            if (new File(imgPath).exists()) {

                Bitmap bitmap = ImageCapture.rotateBitmapOrientation(imgPath);
                chosenImage = bitmap;
                ivShelf.setImageBitmap(chosenImage);
            } else {

            }
            return;
        }


        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                && null != data) {
            // Get the Image from data
            Uri selectedImage = data.getData();
            imgPath = PathUtil.getRealPathFromURI(this, selectedImage);
            Bitmap bm = ImageCapture.rotateBitmapOrientation(imgPath);

            chosenImage = bm;
            ivShelf.setImageBitmap(bm);
            return;
        }
        imgPath = null;
        chosenImage = null;

    }


}
