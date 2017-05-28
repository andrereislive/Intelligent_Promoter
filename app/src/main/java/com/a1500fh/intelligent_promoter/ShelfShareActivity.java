package com.a1500fh.intelligent_promoter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.a1500fh.utils.ImageCapture;

import java.io.File;

public class ShelfShareActivity extends AppCompatActivity {
    private Bitmap cleanImage;
    private ImageView ivShelf;

    private static String TAG = "ShelfShareActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf_share);

        ivShelf = (ImageView) findViewById(R.id.ivRecognition);

        // pega a imagem salva na tela anterior, ela esta em um arquivo
        // se a imagem fosse passada por parametro, o processamento entre enviar a img de uma tela
        // pra outra  é muito maior, e a app trava
        String imageSource = getIntent().getStringExtra("image_source");
        cleanImage = loadImageTaken(imageSource);

        // Envia a imagem para o servidor
        // servidor retorna a imagem processada
        ObjectRecoginition imgObjRec = sendCleanImageToServer(cleanImage);

        if (imgObjRec.getProcessedImage() != null)
            ivShelf.setImageBitmap(imgObjRec.getProcessedImage());
    }

    private Bitmap loadImageTaken(String imageSource) {
        File file;
        Bitmap bitmap = null;
        File fileCheck = new File(imageSource);
        if (fileCheck.exists()) {
            bitmap = ImageCapture.rotateBitmapOrientation(imageSource);
        } else {
            Toast toast = Toast.makeText(this, "Image not found!", Toast.LENGTH_SHORT);
        }
        return bitmap;
    }

    /**
     * Recebe um Bitmap envia para o servidor e o servidor retorna um json contendo algumas informacoes e mais a imagem processada,
     * todos esses dados são serializados no ObjectRecognition que podera ser usado mais afrente
     */
    private ObjectRecoginition sendCleanImageToServer(Bitmap cleanImage) {
        RestComm rest = new RestComm();
        rest.executeSendImageToServer(cleanImage);
        ObjectRecoginition objRec = new ObjectRecoginition(rest.getResponse());
        return objRec;
    }

}
