package com.a1500fh.intelligent_promoter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.a1500fh.utils.ImageCapture;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;
    private static final String IMG_PATH = Environment.getExternalStorageDirectory() + File.separator + "image.jpg";

    private ImageView ivShelf;
    private Button btnLoadShelf;
    private Button btnListProduct;
    private Bitmap cameraImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ivShelf = (ImageView) findViewById(R.id.ivShelf);
        btnLoadShelf = (Button) findViewById(R.id.btnLoadCamera);
        btnListProduct = (Button) findViewById(R.id.btnLoad);

        btnLoadShelf.setOnClickListener(this);
        ivShelf.setOnClickListener(this);
        btnListProduct.setOnClickListener(this);

        // Desebilita camera se dispositivo nao tiver
        if (!hasCamera())
            btnLoadShelf.setEnabled(false);

        File file = new File(IMG_PATH);

        if (file.exists()) {
          file.delete();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoadCamera:
                launchCameraFullRes(v);
                break;

            case R.id.btnLoad:
                File file = new File(IMG_PATH);
                if (file.exists()) {
                    Intent intent = new Intent(this, ShelfShareActivity.class);
                    intent.putExtra("image_source", IMG_PATH);



                    startActivity(intent);
                }else
                {
                    Toast.makeText(this, "Please take a photo of the shelf first", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(cameraImage != null)
        {
            ivShelf.setImageBitmap(cameraImage);
        }
    }

    /**
     * Check if device has any camera
     *
     * @return
     */
    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public void launchCameraFullRes(View view) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File file = new File(IMG_PATH);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            cameraImage = (Bitmap) extras.get("data");
            ivShelf.setImageBitmap(cameraImage);

        }
        //Check that request code matches ours:
        if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE) {
            //Get our saved file into a bitmap object:
            //File file = new File(IMG_PATH);
            Bitmap bitmap =  ImageCapture.rotateBitmapOrientation(IMG_PATH);//ImageCapture.decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 700);
            cameraImage = bitmap;
            ivShelf.setImageBitmap(cameraImage);
        }

    }

}
