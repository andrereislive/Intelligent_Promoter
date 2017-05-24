package com.a1500fh.intelligent_promoter;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.*;


/**
 *
 * @author Andre
 */
public class RestComm extends AsyncTask {
    static final String TAG = "RestComm";
    StringBuilder restResponse;

    OkHttpClient client = new OkHttpClient();
    /**
     * @param args the command line arguments
     */


    /** Exemplo
     * StringBuilder strBuilder = callRest();
     ObjectRecoginition objRec = new ObjectRecoginition(strBuilder);

     System.out.println(objRec.getRecognizedObjectsList().get(0).getLabel());
     System.out.println(objRec.getRecognizedObjectsList().get(0).getBottom());
     */

    public StringBuilder callRest() {

        return null;
    }

    @Override
    protected Object doInBackground(Object[] params) {



        Request request = new Request.Builder()
                .url("http://192.168.0.104:8000/intelligent_promoter/image/")
                //.url("https://reqres.in/api/users/2")
//                .get()
//                .addHeader("content-type", "application/json")
//                .addHeader("cache-control", "no-cache")
//                .addHeader("postman-token", "125b49d2-bf84-a663-fd23-b60e70121dc5")
                .build();

        try {
            Log.i(TAG,"Try response");
           // Response response = client.newCall(request).execute();
            Response response = client.newCall(request).execute();

            Log.i(TAG,"RESPONSE return - doinbackground=");
            restResponse = new StringBuilder(response.body().string());
          return  null;

        } catch (IOException ex) {
            Logger.getLogger(RestComm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }


    public StringBuilder getResponse(){
        int i = 0;
        int iterationsMax = 300;
        // espera um tempo pra processar o rest no doInbackground
        while( restResponse == null && i < iterationsMax){

            i++;
            Log.i(TAG,"Loop getResponse:"+i);

            sleep(1000);
        }
        return restResponse;
    }

    private void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
