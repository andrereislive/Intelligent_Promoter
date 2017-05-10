package com.a1500fh.intelligent_promoter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SELECTED_PICTURE = 100;
    private Uri imageUri;

    private EditText ptName;
    private ImageView ivProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        ptName = (EditText) findViewById(R.id.ptProductName);
        ivProduct = (ImageView) findViewById(R.id.ivProduct);

        ivProduct.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivProduct:
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECTED_PICTURE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode, data);
        if(resultCode == RESULT_OK && requestCode == SELECTED_PICTURE)
        {
            imageUri = data.getData();
            ivProduct.setImageURI(imageUri);
        }
    }
}
