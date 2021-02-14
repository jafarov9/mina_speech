package com.hajma.apps.mina2.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.utils.LocaleHelper;

public class QiblaActivity extends AppCompatActivity implements SensorEventListener {

    private ImageView imgCompass;
    private LottieAnimationView lottiBackQibla;
    private int mAzimuth;

    private SensorManager mSensorManager;
    private Sensor mRotationV, mAccelerometer, mMagnetometer;

    private float[] rMat = new float[9];
    private float[] orientation = new float[9];
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean haveSensor = false, haveSensor2 = false;
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qibla);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        lottiBackQibla = findViewById(R.id.lottiBackQibla);

        imgCompass = findViewById(R.id.qiblaMile);

        lottiBackQibla.setOnClickListener(v -> onBackPressed());

        start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {


        Log.e("jjj", "Onsensor changed");


        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rMat, event.values);
            mAzimuth = (int) ((Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360);
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;

        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;

        }

        if(mLastMagnetometerSet && mLastAccelerometerSet) {
            SensorManager.getRotationMatrix(rMat, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(rMat, orientation);
            mAzimuth = (int) ((Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360);
        }

        mAzimuth = Math.round(mAzimuth);

        imgCompass.setRotation(mAzimuth - 207);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void start() {

        Log.e("JJJ", "Burdayam");

        if(mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null) {
            if(mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null || mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) {
                Log.e("JJJ", "No sensor alert");
                noSensorAlert();
            } else {
                mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

                haveSensor = mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
                haveSensor2 = mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
                Log.e("JJJ", "Register accelor and magnometer");


            }
        }else {
            mRotationV = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            haveSensor = mSensorManager.registerListener(this, mRotationV, SensorManager.SENSOR_DELAY_UI);

        }

    }

    private void noSensorAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Your device does\'nt support the compass")
                .setCancelable(false)
                .setNegativeButton("CLOSE", (dialog, which) -> finish());

        alertDialog.show();

    }

    private void stop() {
        if(haveSensor && haveSensor2) {
            mSensorManager.unregisterListener(this, mAccelerometer);
            mSensorManager.unregisterListener(this, mMagnetometer);
        } else {
            if(haveSensor) {
                mSensorManager.unregisterListener(this, mRotationV);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        start();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}
