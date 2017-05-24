package com.a1500fh.intelligent_promoter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.widget.ImageView;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;

public class ShelfShareActivity extends AppCompatActivity {
    private ImageView ivShelf;
    private static String TAG = "ShelfShareActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf_share);

        ivShelf = (ImageView) findViewById(R.id.ivRecognition);
        // Calls the Rest WS
        ObjectRecoginition imgObjRec = callRest();
        ivShelf.setImageBitmap(imgObjRec.getProcessedImage());


}

    public ObjectRecoginition callRest(){
        Log.i(TAG,"callRest()");
        RestComm rest = new RestComm();
        rest.execute("");

        Log.i(TAG,"RESPONSE=");
        Log.i(TAG,rest.getResponse().toString());
        ObjectRecoginition objRec = new ObjectRecoginition(rest.getResponse());

        return objRec;
    }
}

//ivShelf.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Bitmap bitmap = Bitmap.createBitmap(
//                    ivShelf.getWidth() , // Width
//                    ivShelf.getHeight(), // Height
//                    Bitmap.Config.ARGB_8888 // Config
//            );
//
//            // Initialize a new Canvas instance
//            Canvas canvas = new Canvas(bitmap);
//
//            // Draw a solid color to the canvas background
//
//
//            // Initialize a new Paint instance to draw the Rectangle
//            Paint paint = new Paint();
//            paint.setStyle(Paint.Style.STROKE);
//            paint.setColor(Color.GREEN);
//            paint.setAntiAlias(true);
//
//            // Set a pixels value to padding around the rectangle
//            int padding = 50;
//
//                /*
//                    public Rect (int left, int top, int right, int bottom)
//                        Create a new rectangle with the specified coordinates. Note: no range
//                        checking is performed, so the caller must ensure that left <= right and
//                        top <= bottom.
//
//                    Parameters
//                        left : The X coordinate of the left side of the rectangle
//                        top : The Y coordinate of the top of the rectangle
//                        right : The X coordinate of the right side of the rectangle
//                        bottom : The Y coordinate of the bottom of the rectangle
//
//                */
//
//            // Initialize a new Rect object
//            Rect rectangle = new Rect(
//                    padding, // Left
//                    padding, // Top
//                    canvas.getWidth() - padding, // Right
//                    canvas.getHeight() - padding // Bottom
//            );
//
//                /*
//                    public void drawRect (Rect r, Paint paint)
//                        Draw the specified Rect using the specified Paint. The rectangle will be
//                        filled or framed based on the Style in the paint.
//
//                    Parameters
//                        r : The rectangle to be drawn.
//                        paint : The paint used to draw the rectangle
//                */
//
//            // Finally, draw the rectangle on the canvas
//            canvas.drawRect(rectangle, paint);
//
//            // Display the newly created bitmap on app interface
//            ivShelf.setImageBitmap(bitmap);
//        }
//    });