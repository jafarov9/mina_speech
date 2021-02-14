package com.hajma.apps.mina2.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.eventbus.DataEvent;
import com.hajma.apps.mina2.retrofit.ApiUtils;
import com.hajma.apps.mina2.retrofit.MinaInterface;
import com.hajma.apps.mina2.retrofit.model.ProfileApiModel;
import com.hajma.apps.mina2.utils.ConvertUriToFile;
import com.hajma.apps.mina2.utils.PicassoCache;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class GeneralSettingsFragment extends Fragment {

    private ImageView imgGeneralSettingsPicture;
    private ImageButton iButtonChangeProfilePicture;
    private EditText etNameGeneralSettings,
            etUsernameGeneralSettings, etGenderGeneralSettings,
            etEmailGeneralSettings, etPasswordGeneralSettings,
            etConfirmPasswordGeneralSettings;
    private MinaInterface minaInterface;
    private ProfileApiModel user;
    private String token;
    private SharedPreferences sharedPreferences;
    private static int IMAGE_PICK_CODE = 1000;
    private static int PERMISSON_CODE = 1001;
    private String filePath = "";
    private File f;

    public GeneralSettingsFragment(ProfileApiModel user) {
        this.user = user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater
                .inflate(R.layout.fragment_general_settings, container, false);

        minaInterface = ApiUtils.getMinaInterface();
        sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);


        //init views
        imgGeneralSettingsPicture = view.findViewById(R.id.imgGeneralSettingsPicture);
        iButtonChangeProfilePicture = view.findViewById(R.id.iButtonChangeProfilePicture);
        etNameGeneralSettings = view.findViewById(R.id.etNameGeneralSettings);
        etUsernameGeneralSettings = view.findViewById(R.id.etUsernameGeneralSettings);
        etGenderGeneralSettings = view.findViewById(R.id.etGenderGeneralSettings);
        etEmailGeneralSettings = view.findViewById(R.id.etEmailGeneralSettings);
        etPasswordGeneralSettings = view.findViewById(R.id.etPasswordGeneralSettings);
        etConfirmPasswordGeneralSettings = view.findViewById(R.id.etConfirmPasswordGeneralSettings);


        iButtonChangeProfilePicture.setOnClickListener(v -> {
            //check runtime permisson
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED) {
                    //permisson not granted, request it.
                    String permissons[] = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    //show popup for runtime permission
                    requestPermissions(permissons, PERMISSON_CODE);
                }else {
                    //permisson already granted
                    pickImageFromGallery();
                }
            } else {
                //system os is less than marsmallow

            }
        });


        //set disabled edit texts
        etConfirmPasswordGeneralSettings.setEnabled(false);
        etNameGeneralSettings.setEnabled(false);
        etUsernameGeneralSettings.setEnabled(false);
        etGenderGeneralSettings.setEnabled(false);
        etEmailGeneralSettings.setEnabled(false);
        etPasswordGeneralSettings.setEnabled(false);


        if(token != null) {
            loadViews();
        }
        return view;
    }

    private void loadViews() {

        if(user != null) {
            etNameGeneralSettings.setText(user.getName());
            etEmailGeneralSettings.setText(user.getEmail());
            etUsernameGeneralSettings.setText(user.getUsername());
            if(user.getGender() == 1) {
                etGenderGeneralSettings.setText(getResources().getString(R.string._male));
            } else {
                etGenderGeneralSettings.setText(getResources().getString(R.string._female));
            }


            if(!user.getProfile().equals("")) {
                PicassoCache.getPicassoInstance(getActivity())
                        .load(user.getProfile()
                                .replace("http:", "https:"))
                        .resize(100, 100)
                        .into(imgGeneralSettingsPicture);
            }

        }else {
            return;
        }

    }

    private void pickImageFromGallery() {
        //intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == getActivity().RESULT_OK && requestCode == IMAGE_PICK_CODE) {

            //set image to imageView
            Log.e("zzzz", "Burdayam");

            Log.e("zzzz", data.getData().toString());
            imgGeneralSettingsPicture.setImageURI(data.getData());

            f = ConvertUriToFile.convertImageUriToFile(data.getData(),getActivity());
            filePath = f.getAbsolutePath();
            uploadImage();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == PERMISSON_CODE) {

            if(grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                //permission was granted
                pickImageFromGallery();
            } else {
                //permission was denied
                Toast.makeText(getActivity(), "Permission denied..", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void uploadImage() {

        if(!filePath.isEmpty()) {
            File f = new File(filePath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), f);


            MultipartBody.Part part = MultipartBody.Part.createFormData("image", f.getName(), requestBody);

            minaInterface.postChangeProfilePicture(part, "Bearer "+ token).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    if(response.isSuccessful()) {
                        EventBus.getDefault().post(new DataEvent.ProfileImageChange(1));

                        Log.e("fdfdf", "REspinse success");
                    }else {
                        Log.e("fdfdf", response.toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("fdfdf", t.toString());
                }
            });

        }else {
            return;
        }

    }

}
