package com.a1500fh.intelligent_promoter;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.*;


/**
 * @author Andre
 */
public class RestComm extends AsyncTask {
    private static final String TAG = "RestComm";


    public static final int SEND_IMAGE_TO_SERVER = 0;
    public static final String URL_SEND_IMAGE_TO_SERVER = "http://192.168.5.104:8000/intelligent_promoter/image/";

    public static final int ASK_PROCESSED_IMAGE = 1;
    public static final String URL_ASK_PROCESSED_IMAGE = "http://192.168.5.104:8000/intelligent_promoter/image/";

    private static final int CONNECTION_TIMEOUT_SEC = 60; // tempo de vida da conexao
    private static final int WRITE_TIMEOUT_SEC = 60;
    private static final int READ_TIMEOUT_SEC = 60; // tempo de vida do socket


    StringBuilder restResponse;

  OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT_SEC, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT_SEC, TimeUnit.SECONDS)
                .build();
    private Bitmap cleanImage;
    /**
     * @param args the command line arguments
     */


    /**
     * Exemplo
     * StringBuilder strBuilder = askServerForTheProcessedImage();
     * ObjectRecoginition objRec = new ObjectRecoginition(strBuilder);
     * <p>
     * System.out.println(objRec.getRecognizedObjectsList().get(0).getLabel());
     * System.out.println(objRec.getRecognizedObjectsList().get(0).getBottom());
     */

    public StringBuilder callRest() {

        return null;
    }

    @Override
    protected Object doInBackground(Object[] params) {

        if ((int) (params[0]) == RestComm.SEND_IMAGE_TO_SERVER) {

            ObjectRecoginition objRec = new ObjectRecoginition();
            objRec.setCleanImage((Bitmap) params[1]);
            sendImageToServer(objRec.getCleanImageJson());
        }

        if ((int) (params[0]) == RestComm.ASK_PROCESSED_IMAGE) {
            askImageFromServer();
        }


        return null;
    }

    private void sendImageToServer(String json) {


        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");

        RequestBody body = RequestBody.create(mediaType, json);


        Request request = new Request.Builder()
                .url(URL_SEND_IMAGE_TO_SERVER)
                .post(body)

                .build();
        try {
            Response response = client.newCall(request).execute();
            restResponse = new StringBuilder(response.body().string());
        } catch (IOException ex) {
            Logger.getLogger(RestComm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void askImageFromServer() {
        Request request = new Request.Builder()
                .url(URL_ASK_PROCESSED_IMAGE)
                .build();
        try {

            Response response = client.newCall(request).execute();
            restResponse = new StringBuilder(response.body().string());

        } catch (IOException ex) {
            Logger.getLogger(RestComm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void executeSendImageToServer(Bitmap image) {

        Object[] obj = new Object[]{SEND_IMAGE_TO_SERVER, image};
        this.execute(obj);

    }

    public void executeAskImageFromServer() {
        Object[] obj = new Object[]{ASK_PROCESSED_IMAGE};
        this.execute(obj);

    }

    public StringBuilder getResponse() {
        int i = 0;
        int iterationsMax = CONNECTION_TIMEOUT_SEC;
        // espera um tempo pra processar o rest no doInbackground
        while (restResponse == null && i < iterationsMax) {

            i++;
            Log.i(TAG, "Loop getResponse:" + i);

            sleep(1000);
        }
        return restResponse;
    }

    /**
     * Cria os parametros que são necessarios para a chamada do metodo execute.
     *
     * @return
     */


    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
