package com.a1500fh.intelligent_promoter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final String TAG = "MainActivity";
    static final int REQUEST_IMAGE_CAPTURE =1;

    private ImageView ivShelf;
    private Button btnLoadShelf;
    private Button btnListProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivShelf = (ImageView) findViewById(R.id.ivShelf);
        btnLoadShelf = (Button) findViewById(R.id.btnLoadShelf);
        btnListProduct = (Button) findViewById(R.id.btnProduct);

        btnLoadShelf.setOnClickListener(this);
        ivShelf.setOnClickListener(this);
        btnListProduct.setOnClickListener(this);

        // Desebilita camera se dispositivo nao tiver
       if(!hasCamera())
        btnLoadShelf.setEnabled(false);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLoadShelf:
                launchCamera(v);
                break;
            case R.id.ivShelf:
                startActivity(new Intent(this, ShelfShareActivity.class));
                break;
            case R.id.btnProduct:
                startActivity(new Intent(this, ListProductsActivity.class));
                break;
        }
    }

    /**
     * Check if device has any camera
     * @return
     */
    private boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    /**
     * Lauch the camera
     */
    public void launchCamera(View view){
        Log.i(TAG,"Launch Camera()");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //take pic and pass results to onActivityResults
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE&& resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            ivShelf.setImageBitmap(photo);

        }
    }
}
