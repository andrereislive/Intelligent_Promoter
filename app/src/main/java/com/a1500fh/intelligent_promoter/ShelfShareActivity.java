package com.a1500fh.intelligent_promoter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.a1500fh.model.CustomAdapterShare;
import com.a1500fh.utils.ImageCapture;
import com.a1500fh.utils.MessageUtils;
import com.a1500fh.utils.ZoomImageThumb;

import java.io.File;
import java.util.List;

public class ShelfShareActivity extends AppCompatActivity {
    private Bitmap cleanImage;
    private ImageView ivShelf;
    private ListView lvShare;
    ZoomImageThumb zoom = new ZoomImageThumb();
    ObjectRecoginition imgObjRec;

    private static String TAG = "ShelfShareActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf_share);

        ivShelf = (ImageView) findViewById(R.id.ivRecognition);
        lvShare = (ListView) findViewById(R.id.lvShare);

        // pega a imagem salva na tela anterior, ela esta em um arquivo
        // se a imagem fosse passada por parametro, o processamento entre enviar a img de uma tela
        // pra outra  é muito maior, e a app trava
        String imageSource = getIntent().getStringExtra("image_source");
        cleanImage = loadImageTaken(imageSource);

        // Inicia thread que faz comunicacao com servidor


        SubProcess myThread = new SubProcess(getApplicationContext());
        myThread.start();

    }


    private class SubProcess extends Thread {

        private Context context;


        public SubProcess(Context context) {
            this.context = context;

        }

        @Override
        public void run() {

            CountDown countdown = new CountDown(getApplicationContext());
            countdown.start();
            // Envia a imagem para o servidor
            // servidor retorna a imagem processada
            imgObjRec = sendCleanImageToServer(cleanImage);
            countdown.stopThread();
            if (imgObjRec != null) {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<String> shareList = imgObjRec.getShelfShareObjects();
                        if (shareList.size() == 0)
                            MessageUtils.Toast(context,"No Response From Server!",20000);
                        ArrayAdapter<String> arrayAdapter = new CustomAdapterShare(shareList, context);

                        lvShare.setAdapter(arrayAdapter);

                        if (imgObjRec.getProcessedImage() != null) {
                            ivShelf.setImageBitmap(imgObjRec.getProcessedImage());
                        }
                    }

                });
            }
        }

        ;
    }


    private class CountDown extends Thread {

        private Context context;
        private Boolean stopThread = false;
        private int countDown = RestComm.CONNECTION_LIFE;


        public CountDown(Context context) {
            this.context = context;

        }

        public void stopThread() {
            stopThread = true;
        }


        @Override
        public void run() {
            // Envia a imagem para o servidor
            // servidor retorna a imagem processada

            while (stopThread == false && countDown > 0) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String message = "Processing Image (" + String.valueOf(countDown) + ")";
                        MessageUtils.Toast(getApplicationContext(), message, 1000);

                    }

                });
                countDown--;
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public void btnIvClick(View v) {
        switch (v.getId()) {
            case R.id.ivRecognition:
                if (imgObjRec != null && imgObjRec.getProcessedImage() != null)
                    zoom.zoomImageFromThumb(ivShelf, imgObjRec.getProcessedImage(), (ImageView) findViewById(R.id.expanded_image_share), R.id.container_share, this);
                break;
        }
    }

    private Bitmap loadImageTaken(String imageSource) {
        File file;
        Bitmap bitmap = null;
        File fileCheck = new File(imageSource);
        if (fileCheck.exists()) {
            bitmap = ImageCapture.rotateBitmapOrientation(imageSource);
        } else {
            Toast.makeText(this, "Image not found!", Toast.LENGTH_SHORT).show();
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
