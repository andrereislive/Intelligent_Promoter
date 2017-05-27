package com.a1500fh.intelligent_promoter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This example uses the org.json.me parser provided by json.org to parse and
 * browse a JSON content.
 *
 * The JSON content is simple abstraction of a file menu as provided here:
 * http://www.json.org/example.html
 *
 * The example then tries to list all the 'menuitem's available in the popup
 * menu. It is assumed the user knows the menu JSON file structure.
 *
 */
public class ObjectRecoginition {
    private static final String TAG = " ObjectRecoginition";
    private List<RecognizedObjects> recognizedObjectsList = new ArrayList<>();
    private Bitmap processedImage;
    private Bitmap cleanImage;

    public ObjectRecoginition(){

    }
    public ObjectRecoginition(StringBuilder myJsonString) {

        try {

            // create the data structure to exploit the content
            // the string is created assuming default encoding
            JSONObject jsono = new JSONObject(myJsonString.toString());
            String processed = jsono.get("processed_image").toString();
            String clean = jsono.get("clean_image").toString();

            processedImage =  string64toBitmap(processed);
            setCleanImage(string64toBitmap(clean));

            // get the JSONObject named "menu" from the root JSONObject
            JSONArray jArray = jsono.getJSONArray("recognized_objects");
            for (int x = 0; x < jArray.length(); x++) {
                recognizedObjectsList.add(new RecognizedObjects(jArray.getJSONObject(x)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }




    public Bitmap string64toBitmap(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
    public String bitmapToString64(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT).replace("\n", "");
    }

    public String getArrays() {
        return "";
    }

    /**
     * @return the recognizedObjectsList
     */
    public List<RecognizedObjects> getRecognizedObjectsList() {
        return recognizedObjectsList;
    }

    public Bitmap getProcessedImage() {
        return processedImage;
    }

    public Bitmap getCleanImage() {
        return cleanImage;
    }



    public void setCleanImage(Bitmap cleanImage) {
        this.cleanImage = cleanImage;
    }

    public String getCleanImageJson() {
       String encodedImage = bitmapToString64(cleanImage);

        String myJson ="{\"clean_image\":\"" +encodedImage+"\"}";


        return myJson;
    }



    public class RecognizedObjects {

        private String label;
        private Boolean nms;
        private BigDecimal score;
        private BigDecimal shelfShare;
        private Integer left;
        private Integer top;
        private Integer right;
        private Integer bottom;

        private String strLabel = "label";
        private String strNms = "nms";
        private String strScore = "score";
        private String strShelfShare = "shelf_share";
        private String strLeft = "left";
        private String strTop = "top";
        private String strRight = "right";
        private String strBottom = "bottom";

        public RecognizedObjects() {
        }

        private RecognizedObjects(JSONObject jObject) {
            try {
                label = jObject.getString(strLabel);
                nms = jObject.getBoolean(strNms);
                score = new BigDecimal(jObject.getString(strScore));
               // shelfShare = new BigDecimal(jObject.getString(strShelfShare));
                left = jObject.getInt(strLeft);
                top = jObject.getInt(strTop);
                right = jObject.getInt(strRight);
                bottom = jObject.getInt(strBottom);
            } catch (JSONException ex) {
                Logger.getLogger(ObjectRecoginition.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        /**
         * @return the label
         */
        public String getLabel() {
            return label;
        }

        /**
         * @param label the label to set
         */
        public void setLabel(String label) {
            this.label = label;
        }

        /**
         * @return the nms
         */
        public Boolean getNms() {
            return nms;
        }

        /**
         * @param nms the nms to set
         */
        public void setNms(Boolean nms) {
            this.nms = nms;
        }

        /**
         * @return the score
         */
        public BigDecimal getScore() {
            return score;
        }

        /**
         * @param score the score to set
         */
        public void setScore(BigDecimal score) {
            this.score = score;
        }

        /**
         * @return the left
         */
        public Integer getLeft() {
            return left;
        }

        /**
         * @param left the left to set
         */
        public void setLeft(Integer left) {
            this.left = left;
        }

        /**
         * @return the top
         */
        public Integer getTop() {
            return top;
        }

        /**
         * @param top the top to set
         */
        public void setTop(Integer top) {
            this.top = top;
        }

        /**
         * @return the right
         */
        public Integer getRight() {
            return right;
        }

        /**
         * @param right the right to set
         */
        public void setRight(Integer right) {
            this.right = right;
        }

        /**
         * @return the bottom
         */
        public Integer getBottom() {
            return bottom;
        }

        /**
         * @param bottom the bottom to set
         */
        public void setBottom(Integer bottom) {
            this.bottom = bottom;
        }

        public String getStrShelfShare() {
            return strShelfShare;
        }

        public void setStrShelfShare(String strShelfShare) {
            this.strShelfShare = strShelfShare;
        }
    }
}