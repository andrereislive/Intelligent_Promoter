package com.a1500fh.intelligent_promoter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import static com.a1500fh.utils.ImageCapture.scaleBitmapToFitImageView;

public class ShelfShareActivity extends AppCompatActivity {
    private Bitmap cleanImage;
    private ImageView ivShelf;
    private ListView lvShare;
    ZoomImageThumb zoom = new ZoomImageThumb();
    ObjectRecoginition imgObjRec;
    SubProcess mySubProcessThread;
    CountDown countdown;
    RestComm rest;


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
        mySubProcessThread = new SubProcess(this);
        mySubProcessThread.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (countdown != null)
            countdown.stopThread();

        if (mySubProcessThread != null) {
            mySubProcessThread.stopThread();

        }
        if (rest != null) {
            rest.stopProcess();
        }


    }

    public int isServerOnline() {
        rest = new RestComm();
        return rest.isServerOnline();

    }

    private class SubProcess extends Thread {

        private Context context;
        private AppCompatActivity activity;
        private Boolean stop = false;

        public SubProcess(AppCompatActivity activity) {
            this.context = activity.getApplicationContext();
            this.activity = activity;

        }

        public void stopThread() {
            stop = true;
        }

        @Override
        public void run() {
            // Se server estiver off entao saia do run

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MessageUtils.Toast(context, "Connecting to Server...", 3000);
                }
            });

            int serverStatus = isServerOnline();
            switch (serverStatus) {
                case RestComm.SERVER_ONLINE: {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MessageUtils.Toast(context, "Connected!", 1000);

                        }
                    });
                }
                break;
                case RestComm.SERVER_OFFLINE: {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MessageUtils.Toast(context, "Server is offline!", 20000);
                            activity.onBackPressed();

                        }
                    });

                }
                break;
                case RestComm.SERVER_ERROR: {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MessageUtils.Toast(context, "Server Returned an Error!", 20000);
                            activity.onBackPressed();

                        }
                    });

                }
                break;

            }
            if (serverStatus != RestComm.SERVER_ONLINE)
                return;

            else {

                countdown = new CountDown(getApplicationContext());
                countdown.start();
                // Envia a imagem para o servidor
                // servidor retorna a imagem processada
                imgObjRec = sendCleanImageToServer(cleanImage);
                countdown.stopThread();


                if (imgObjRec != null) {

                    switch (imgObjRec.getStatus()) {
                        case ObjectRecoginition.NO_RESPONSE_FROM_SERVER: {
                            if (!stop) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MessageUtils.Toast(context, "No Response From Server!", 20000);
                                    }
                                });
                            }

                        }
                        break;

                        case ObjectRecoginition.ERROR_SERVER_RESPONSE: {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MessageUtils.Toast(context, "Server Returned an Invalid Response!", 20000);
                                }
                            });
                        }
                        break;
                        case ObjectRecoginition.RECEIVED_VALID_SERVER_RESPONSE: {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    List<String> shareList = imgObjRec.getShelfShareObjects();
                                    if (!stop) {
                                        if (shareList.size() == 0)
                                            MessageUtils.Toast(context, "Products not Recognized!", 20000);
                                        ArrayAdapter<String> arrayAdapter = new CustomAdapterShare(shareList, context);

                                        lvShare.setAdapter(arrayAdapter);

                                        if (imgObjRec.getProcessedImage() != null) {
                                            ivShelf.setImageBitmap(imgObjRec.getProcessedImage());
                                        }
                                    }
                                }
                            });
                        }
                        break;
                    }
                }
            }


        }


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
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String message = "Processing Image (" + String.valueOf(countDown) + ")";
                        MessageUtils.Toast(getApplicationContext(), message, 1000);

                    }

                });
                countDown--;

            }

        }
    }


    public void btnIvClick(View v) {
        switch (v.getId()) {
            case R.id.ivRecognition:
                if (imgObjRec != null && imgObjRec.getProcessedImage() != null) {
                    ImageView ivExpanded = (ImageView) findViewById(R.id.expanded_image_share);

                    zoom.zoomImageFromThumb(ivShelf, imgObjRec.getProcessedImage(), ivExpanded, R.id.container_share, this);
                }
                break;
        }
    }

    private Bitmap loadImageTaken(String imageSource) {
        File file;
        Bitmap bitmap = null;
        File fileCheck = new File(imageSource);
        if (fileCheck.exists()) {
            int width = ImageCapture.getScreenWidth(getWindowManager());
            int height = ImageCapture.getScreenHeight(getWindowManager());
            // diminui a resolucao da imagem para evitar problemas de estouro da memoria heap
            Bitmap bitmapScaled = scaleBitmapToFitImageView(width, height, imageSource);
            bitmap = ImageCapture.rotateBitmapOrientation(bitmapScaled, imageSource);
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
        rest = new RestComm();
        rest.executeSendImageToServer(cleanImage);

        ObjectRecoginition objRec = new ObjectRecoginition(rest.getResponse());

        return objRec;
    }


}
