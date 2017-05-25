package com.a1500fh.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;

import com.a1500fh.model.Produto;

import java.util.ArrayList;
import java.util.List;

import static com.a1500fh.model.Produto.PRODUTO_ID;
import static com.a1500fh.model.Produto.PRODUTO_NOME;
import static com.a1500fh.model.Produto.PRODUTO_ENTIDADE;

/**
 * Created by Andre on 25/05/2017.
 */


public class DBController extends SQLiteOpenHelper {
private static final String TAG = "DBController";

    public DBController(Context context, String db_name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, db_name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createImageTable = "";
        String createProdutosTable = "CREATE TABLE " + PRODUTO_ENTIDADE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " + PRODUTO_NOME + " TEXT UNIQUE);";
        sqLiteDatabase.execSQL(createProdutosTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PRODUTO_ENTIDADE + ";");
        onCreate(sqLiteDatabase);
    }

    public void insert_produto(String nome) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUTO_NOME, nome);
        Log.i(TAG,nome);
        this.getWritableDatabase().insertOrThrow(PRODUTO_ENTIDADE, "", contentValues);


    }

    public void delete_produto(String nome) {
        this.getWritableDatabase().delete(PRODUTO_ENTIDADE, PRODUTO_NOME + "='" + nome + "';", null);
    }

    public void update_produto(String id, String novo_nome) {
        this.getWritableDatabase().execSQL("UPDATE " + PRODUTO_ENTIDADE + " SET " + PRODUTO_NOME + "='" + novo_nome + "' WHERE " + PRODUTO_ID + "='" + id + "';");
    }

    public List<Produto> list_produto() {
        Cursor cursor = this.getWritableDatabase().rawQuery("SELECT * FROM " + PRODUTO_ENTIDADE + ";", null);
        List<Produto> lista = new ArrayList<>();
        while (cursor.moveToNext()) {
            Produto pro = new Produto();
            pro.setId(cursor.getInt(0));
            pro.setNome(cursor.getString(1));
            lista.add(pro);
        }

        return lista;
    }
}
