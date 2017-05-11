package com.a1500fh.intelligent_promoter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLoadShelf:
                break;
            case R.id.ivShelf:
                break;
            case R.id.btnProduct:
                startActivity(new Intent(this, ListProductsActivity.class));
                break;
        }
    }
}
