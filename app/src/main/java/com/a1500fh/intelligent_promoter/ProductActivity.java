package com.a1500fh.intelligent_promoter;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.a1500fh.controller.DBController;

import static com.a1500fh.intelligent_promoter.Parameters.DBNAME;
import static com.a1500fh.intelligent_promoter.Parameters.DBVERSION;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {
private static final String TAG = "ProductActivity";

    private EditText txtNome;
    DBController controllerDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        txtNome = (EditText) findViewById(R.id.txtNome);

        controllerDb = new DBController(this, DBNAME, null, DBVERSION);

    }
    public void btn_click(View v) {
        switch (v.getId()) {
            case R.id.btnSaveProdutct:
                inserirProduto();
                startActivity(new Intent(ProductActivity.this, ListProductsActivity.class));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSaveProdutct:
                inserirProduto();
                startActivity(new Intent(ProductActivity.this, ListProductsActivity.class));
                break;
        }
    }

    private void inserirProduto() {
        try {
            Log.i(TAG,"Inserindo");
            controllerDb.insert_produto(txtNome.getText().toString());
        } catch (SQLiteException ex) {
            Toast.makeText(ProductActivity.this, "Already Exists", Toast.LENGTH_SHORT).show();
        }
    }


}
