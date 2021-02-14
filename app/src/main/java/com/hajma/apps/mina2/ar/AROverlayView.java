package com.hajma.apps.mina2.ar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.opengl.Matrix;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.constraintlayout.solver.widgets.Rectangle;

import com.hajma.apps.mina2.ar.helper.LocationHelper;
import com.hajma.apps.mina2.ar.model.ARPoint;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AROverlayView extends View {

    private Context context;
    private float[] rotatedProjectionMatrix = new float[16];
    private Location currentLocation;
    private List<ARPoint> arPoints = new ArrayList<>();
    private JSONObject resultJSON;


    public AROverlayView(Context context, JSONObject resultJSON) {
        super(context);
        this.resultJSON = resultJSON;
        this.context = context;

        //Demo points
        /*arPoints = new ArrayList<ARPoint>() {{
            add(new ARPoint("Resad dagli", 40.3864730, 49.8452825, 0));
            add(new ARPoint("Shopping star", 40.3806869, 49.8397894, 0));
        }};*/


        try {
            if(resultJSON != null) {
                loadNearbyObjects(resultJSON);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void loadNearbyObjects(JSONObject resultJSON) throws JSONException {

        JSONArray objects = resultJSON.getJSONArray("objects");
        for(int i = 0;i < objects.length();i++) {
             JSONObject temp = objects.getJSONObject(i);
             ARPoint point = new ARPoint(
                     temp.getString("name"),
                     temp.getDouble("latitude"),
                     temp.getDouble("longitude"),
                     0
             );
             point.setAddress(temp.getString("address"));
             point.setId(temp.getInt("id"));
             point.setLogo(temp.getString("logo"));

             arPoints.add(point);
        }


    }

    public void updateRotatedProjectionMatrix(float[] rotatedProjectionMatrix) {
        this.rotatedProjectionMatrix = rotatedProjectionMatrix;
        this.invalidate();
    }

    public void updateCurrentLocation(Location currentLocation){
        this.currentLocation = currentLocation;
        this.invalidate();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (currentLocation == null) {
            return;
        }

        final int radius = 30;
        @SuppressLint("DrawAllocation") Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setTextSize(60);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);

        for (int i = 0; i < arPoints.size(); i ++) {


            float[] currentLocationInECEF = LocationHelper.WSG84toECEF(currentLocation);
            float[] pointInECEF = LocationHelper.WSG84toECEF(arPoints.get(i).getLocation());
            float[] pointInENU = LocationHelper.ECEFtoENU(currentLocation, currentLocationInECEF, pointInECEF);

            float[] cameraCoordinateVector = new float[4];
            Matrix.multiplyMV(cameraCoordinateVector, 0, rotatedProjectionMatrix, 0, pointInENU, 0);

            // cameraCoordinateVector[2] is z, that always less than 0 to display on right position
            // if z > 0, the point will display on the opposite
            if (cameraCoordinateVector[2] < 0) {
                float x  = (0.5f + cameraCoordinateVector[0]/cameraCoordinateVector[3]) * canvas.getWidth();
                float y = (0.5f - cameraCoordinateVector[1]/cameraCoordinateVector[3]) * canvas.getHeight();

                String logoURL = arPoints.get(i).getLogo();

                float bitmapX = x - (30 * arPoints.get(i).getName().length() / 2);

                Target target = new Target() {

                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {


                        canvas.drawBitmap(bitmap, bitmapX, y, paint);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };


                if(!logoURL.equals("")) {
                    Picasso.get()
                            .load(logoURL.replace("http:", "https:"))
                            //.resize(200, 200)
                            .into(target);
                }else {
                    canvas.drawCircle(x, y, radius, paint);
                }


                canvas.drawText(arPoints.get(i).getName(), x - (30 * arPoints.get(i).getName().length() / 2), y - 80, paint);
            }
        }
    }





}
