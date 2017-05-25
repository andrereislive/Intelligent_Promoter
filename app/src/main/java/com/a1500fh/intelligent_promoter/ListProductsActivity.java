package com.a1500fh.intelligent_promoter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.a1500fh.controller.DBController;
import com.a1500fh.model.CustomAdapter;
import com.a1500fh.model.Produto;

import java.util.List;

import static com.a1500fh.intelligent_promoter.Parameters.DBNAME;
import static com.a1500fh.intelligent_promoter.Parameters.DBVERSION;

public class ListProductsActivity extends AppCompatActivity {
    ListView productList;
    DBController controllerDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_products);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        productList = (ListView) findViewById(R.id.listProduct);
        controllerDb = new DBController(this, DBNAME, null, DBVERSION);

        CustomAdapter cst = new CustomAdapter(carregarListaDeProdutos(),getApplicationContext());
        productList.setAdapter(cst);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListProductsActivity.this, ProductActivity.class));
            }
        });
    }

    private List<Produto> carregarListaDeProdutos(){
        List<Produto> proList = controllerDb.list_produto();

        return proList;

    }
}
