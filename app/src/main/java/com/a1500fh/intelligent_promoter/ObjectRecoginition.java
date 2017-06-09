package com.a1500fh.intelligent_promoter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This example uses the org.json.me parser provided by json.org to parse and
 * browse a JSON content.
 * <p>
 * The JSON content is simple abstraction of a file menu as provided here:
 * http://www.json.org/example.html
 * <p>
 * The example then tries to list all the 'menuitem's available in the popup
 * menu. It is assumed the user knows the menu JSON file structure.
 */
public class ObjectRecoginition {
    private static final String TAG = " ObjectRecoginition";

    private static final String strRecognizedObjectsArray = "recognized_objects";
    private static final String strShelfShareArray = "shelf_share_objects";

    private static final String strCleanImage = "clean_image";
    private static final String strProcessedImage = "processed_image";
    private static final String strCleanImageUuidName = "clean_image_uuid_name";
    // se recebeu uma resposta valida de acordo com as keys strCleanImage, strProcessedImage e strShelfShareArray
    // entao validServerResponse deve ser setado com essa variavel
    public static final int RECEIVED_VALID_SERVER_RESPONSE = 2;
    public static final int ERROR_SERVER_RESPONSE = 1;
    public static final int NO_RESPONSE_FROM_SERVER = 0;


    private List<RecognizedObjects> recognizedObjectsList = new ArrayList<>();
    private List<ShelfShare> shelfShareList = new ArrayList<>();

    private Bitmap processedImage;
    private Bitmap cleanImage;
    private int validServerResponse = ERROR_SERVER_RESPONSE;

    public ObjectRecoginition() {

    }

    public ObjectRecoginition(StringBuilder myJsonString) {

        try {
            if (myJsonString != null) {
                // create the data structure to exploit the content
                // the string is created assuming default encoding


                JSONObject jsono = new JSONObject(myJsonString.toString());

                if (validadeJSonObj(jsono)) {
                    String processed = jsono.get(strProcessedImage).toString();
                    String clean = jsono.get(strCleanImage).toString();

                    processedImage = string64toBitmap(processed);
                    setCleanImage(string64toBitmap(clean));

                    JSONArray jArrayRecognizedObjects;
                    if(!jsono.isNull(strRecognizedObjectsArray)){
                        jArrayRecognizedObjects = jsono.getJSONArray(strRecognizedObjectsArray);
                        for (int x = 0; x < jArrayRecognizedObjects.length(); x++) {
                            recognizedObjectsList.add(new RecognizedObjects(jArrayRecognizedObjects.getJSONObject(x)));
                        }
                    }


                    JSONArray jArrayShare;
                    if (!jsono.isNull(strShelfShareArray)) {
                        jArrayShare = jsono.getJSONArray(strShelfShareArray);

                        for (int x = 0; x < jArrayShare.length(); x++) {
                            shelfShareList.add(new ShelfShare(jArrayShare.getJSONObject(x)));
                        }
                    }

                    validServerResponse = RECEIVED_VALID_SERVER_RESPONSE;
                } else
                    validServerResponse = ERROR_SERVER_RESPONSE;
            } else {
                validServerResponse = NO_RESPONSE_FROM_SERVER;
            }
        } catch (JSONException e) {
            validServerResponse = ERROR_SERVER_RESPONSE;
            e.printStackTrace();
        }

    }

    private boolean validadeJSonObj(JSONObject json) {
        if (!json.has(strCleanImage))
            return false;
        if (!json.has(strProcessedImage))
            return false;
        if (!json.has(strRecognizedObjectsArray))
            return false;
        if (!json.has(strShelfShareArray))
            return false;

        return true;
    }


    public Bitmap string64toBitmap(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",") + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public String bitmapToString64(Bitmap bitmap) {
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

        String imageUuidName = UUID.randomUUID().toString();

        String myJson = "{\"" + strCleanImage + "\":\"" + encodedImage + "\",\"" + strCleanImageUuidName + "\":\"" + imageUuidName + "\"}";


        return myJson;
    }

    public List<String> getShelfShareObjects() {
        List<String> shareList = new ArrayList<>();
        for (ShelfShare it : shelfShareList) {
            String share = it.getSharePercentage().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            shareList.add(share + "% / " + it.getProduct());

        }
        return shareList;
    }

    public int getStatus() {

        return validServerResponse;
    }

    public class ShelfShare {
        private BigDecimal sharePercentage;
        private String product;

        private String strSharePercentage = "share_percentage";
        private String strProduct = "product";

        public ShelfShare(JSONObject jObject) {
            try {
                sharePercentage = new BigDecimal(jObject.getString(strSharePercentage));
                product = jObject.getString(strProduct);
            } catch (JSONException ex) {
                Logger.getLogger(ObjectRecoginition.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public BigDecimal getSharePercentage() {
            return sharePercentage;
        }

        public void setSharePercentage(BigDecimal sharePercentage) {
            this.sharePercentage = sharePercentage;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }
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