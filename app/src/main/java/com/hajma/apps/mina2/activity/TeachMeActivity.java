package com.hajma.apps.mina2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.retrofit.ApiUtils;
import com.hajma.apps.mina2.retrofit.MinaInterface;
import com.hajma.apps.mina2.utils.LocaleHelper;
import com.hajma.apps.mina2.utils.Util;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeachMeActivity extends AppCompatActivity {

    private ImageButton iButtonMicTeachMe;
    private ImageButton iButtonBackTeachMe;
    private boolean havePermissions;
    private static final int REQUEST_PERMISSON_CODE = 1000;
    private long eventTime = 0;
    private long downTime = 0;
    private MediaRecorder mediaRecorder;
    private String pathSave;
    private int errorID;
    private MinaInterface minaInterface;
    private Util util = new Util();


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teach_me);

        //from android M, you need runtime permission
        if(checkPermissionFromDevice()) {
            havePermissions = true;
        } else {
            havePermissions = false;
            requestPermission();
        }

        errorID = getIntent().getIntExtra("errorID", 0);
        minaInterface = ApiUtils.getMinaInterface();


        //init variables
        iButtonMicTeachMe = findViewById(R.id.iButtonMicTeachMe);
        iButtonBackTeachMe = findViewById(R.id.iButtonBackTeachMe);

        iButtonBackTeachMe.setOnClickListener(v -> onBackPressed());

        iButtonMicTeachMe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:



                        if(havePermissions) {
                            iButtonMicTeachMe.setPressed(true);
                            downTime = event.getDownTime();

                            pathSave = Objects.requireNonNull(getExternalFilesDir(null))
                                    .getAbsolutePath() + "/record.3gp";

                            setupMediaRecorder();
                            try {
                                mediaRecorder.prepare();
                                mediaRecorder.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        } else {
                            if(checkPermissionFromDevice()) {
                                havePermissions = true;
                            } else {
                                havePermissions = false;
                                requestPermission();
                            }
                        }

                        return true;

                    case MotionEvent.ACTION_UP:

                        iButtonMicTeachMe.setPressed(false);

                        eventTime = event.getEventTime();

                        long holdTime = (eventTime - downTime);

                        //Log.e("jkjk", "Hold time: " + holdTime);


                        if(holdTime > 1000) {
                            Log.e("jkjk", "Buraxilib: " + eventTime);

                            mediaRecorder.stop();
                            setupMediaRecorder();

                            sendApiRequest();

                            return true;
                        } else {
                            mediaRecorder.reset();
                        }

                }

                return false;
            }
        });

    }

    private void sendApiRequest() {

        if(!pathSave.isEmpty()) {

            File f = new File(pathSave);
            RequestBody sound = RequestBody.create(MediaType.parse("media/type"), f);
            RequestBody idBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(errorID));

            MultipartBody.Part part
                    = MultipartBody.Part.createFormData("sound", f.getName(), sound);


            minaInterface.postTeachMe(idBody, part).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()) {
                        util.errorPlayer(R.raw.learn_sound_az, new MediaPlayer(), TeachMeActivity.this);
                        finish();
                    }else {

                        String s = null;
                        try {
                            s = response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Log.e("asasas", s);

                        Toast.makeText(TeachMeActivity.this, getResources().getString(R.string._currently_learning), Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(TeachMeActivity.this, "Response fail", Toast.LENGTH_LONG).show();
                    finish();
                }
            });

        }

    }

    private void setupMediaRecorder() {

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO
                },
                REQUEST_PERMISSON_CODE
        );

    }



    private boolean checkPermissionFromDevice() {

        int write_external_storage_result
                = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result
                = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);


        return write_external_storage_result == PackageManager.PERMISSION_GRANTED
                && record_audio_result == PackageManager.PERMISSION_GRANTED;
    }


    //upload recorded sound to server
    private void uploadAudioToServer() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_PERMISSON_CODE :

                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();

                }

                break;
        }


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}
