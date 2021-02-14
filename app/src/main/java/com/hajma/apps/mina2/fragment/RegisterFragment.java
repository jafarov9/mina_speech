package com.hajma.apps.mina2.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hajma.apps.mina2.C;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.retrofit.ApiUtils;
import com.hajma.apps.mina2.retrofit.MinaInterface;
import com.hajma.apps.mina2.utils.Util;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private Button btnRegister;
    private EditText etSignName;
    private EditText etSignEmail;
    private EditText etSignUsername;
    private EditText etPhoneNumber;
    private EditText etSignPassword;
    private EditText etSignRePassword;
    private Spinner spinnerGender;
    private int gender = 2;
    private MinaInterface minaInterface;
    private CountryCodePicker ccp;
    private Util util = new Util();
    private ProgressBar pbRegisterLoading;

    @SuppressLint("CommitPrefEdits")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign, container, false);

        minaInterface = ApiUtils.getMinaInterface();





        //init views
        pbRegisterLoading = view.findViewById(R.id.pbRegisterLoading);
        ccp = view.findViewById(R.id.ccp);
        btnRegister = view.findViewById(R.id.btnRegister);
        etSignName = view.findViewById(R.id.etSignName);
        etSignEmail = view.findViewById(R.id.etSignEmail);
        etSignUsername = view.findViewById(R.id.etSignUsername);
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
        etSignPassword = view.findViewById(R.id.etSignPassword);
        etSignRePassword = view.findViewById(R.id.etSignRePassword);
        spinnerGender = view.findViewById(R.id.spinnerGender);


        spinnerGender.setAdapter(new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"FEMALE", "MALE"}
        ));

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    gender = 2;
                }else {
                    gender = 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                gender = 2;
            }
        });

        ccp = view.findViewById(R.id.ccp);
        ccp.setCountryForPhoneCode(994);
        etPhoneNumber.setHint("70 911 23 11");

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                switch (ccp.getSelectedCountryCodeAsInt()) {
                    case 994 :
                        etPhoneNumber.setHint("70 911 23 11");
                        break;

                    case 7 :
                        etPhoneNumber.setHint("495 123 4567");
                        break;

                    case 90 :
                        etPhoneNumber.setHint("216 555 55 55");
                        break;

                    default: etPhoneNumber.setHint("Phone");
                }
            }
        });


        btnRegister.setOnClickListener(v -> {

            if(editTextControl()) {
                if (passwordContainControl()) {
                    if (passwordsEqualControl()) {
                        attemptRegister();
                    }
                }
            }


        });

        return view;
    }

    private void attemptRegister() {

        //hide register button
        btnRegister.setVisibility(View.INVISIBLE);
        //show progress bar loading register
        pbRegisterLoading.setVisibility(View.VISIBLE);
        pbRegisterLoading.setIndeterminate(true);



        String email = etSignEmail.getText().toString().trim().replaceAll(" ", "");
        String username = etSignUsername.getText().toString().trim().replaceAll(" ", "");
        String name = etSignName.getText().toString().trim();
        String password = etSignPassword.getText().toString().trim();
        String c_password = etSignRePassword.getText().toString().trim();
        String phone = ccp.getSelectedCountryCode() + etPhoneNumber.getText().toString().trim().replaceAll(" ", "");

        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody usernameBody = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody passwordBody = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody c_passwordBody = RequestBody.create(MediaType.parse("text/plain"), c_password);
        RequestBody phoneBody = RequestBody.create(MediaType.parse("text/plain"), phone);
        RequestBody genderBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(gender));


        minaInterface.postRegister(emailBody, usernameBody, nameBody, genderBody, passwordBody, c_passwordBody, phoneBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()) {

                    pbRegisterLoading.setVisibility(View.GONE);
                    pbRegisterLoading.setIndeterminate(false);
                    btnRegister.setVisibility(View.VISIBLE);


                    try {
                        String s = response.body().string();

                        //set login detailes shared preferences
                        JSONObject success = new JSONObject(s).getJSONObject("success");
                        String token = success.getString("token");
                        String phone = success.getString("mobile");

                        openVerifyFragment(phone, token);


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                    pbRegisterLoading.setVisibility(View.GONE);
                    pbRegisterLoading.setIndeterminate(false);
                    btnRegister.setVisibility(View.VISIBLE);

                    try {
                        String s = response.errorBody().string();

                        JSONObject errObject = new JSONObject(s).getJSONObject("error");
                        Iterator<String> iter = errObject.keys();
                        String key = iter.next();

                        JSONArray keyObjArray = errObject.getJSONArray(key);
                        String errorMessage = (String) keyObjArray.get(0);

                        util.openDialog(C.ALERT_TYPE_LOGIN_ERROR, errorMessage, getActivity().getSupportFragmentManager());
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                pbRegisterLoading.setVisibility(View.GONE);
                pbRegisterLoading.setIndeterminate(false);
                btnRegister.setVisibility(View.VISIBLE);


                String err = getActivity().getResources().getString(R.string.check_your_internet_connection);
                util.openDialog(C.ALERT_TYPE_LOGIN_ERROR, err, getActivity().getSupportFragmentManager());
            }
        });


    }

    //open verify fragment
    private void openVerifyFragment(String phone, String token) {

        Fragment verifyFragment = new VerifyFragment(phone, token, 0);

        //open verify screen
        Objects.requireNonNull(getActivity())
                .getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                .replace(R.id.sign_fragment_container, verifyFragment, "verifyFragment")
                .commit();

    }

    //edittexts empty control
    private boolean editTextControl() {
        if(etSignEmail.getText().toString().trim().isEmpty()) {
            etSignEmail.setError(getActivity().getResources().getString(R.string._required_field));
            return false;
        }

        if(etSignUsername.getText().toString().trim().isEmpty()) {
            etSignUsername.setError(getActivity().getResources().getString(R.string._required_field));
            return false;
        }

        if(etSignName.getText().toString().trim().isEmpty()) {
            etSignName.setError(getActivity().getResources().getString(R.string._required_field));
            return false;
        }

        if(etSignPassword.getText().toString().trim().isEmpty()) {
            etSignPassword.setError(getActivity().getResources().getString(R.string._required_field));
            return false;
        }

        if(etSignRePassword.getText().toString().trim().isEmpty()) {
            etSignRePassword.setError(getActivity().getResources().getString(R.string._required_field));
            return false;
        }

        if(etPhoneNumber.getText().toString().trim().isEmpty()) {
            etPhoneNumber.setError(getActivity().getResources().getString(R.string._required_field));
            return false;
        }

        return true;
    }

    //passwords equal control
    private boolean passwordsEqualControl() {
        String p1 = etSignPassword.getText().toString().trim();
        String p2 = etSignRePassword.getText().toString().trim();

        if(p1.equals(p2)) {
            return true;
        }else {
            etSignRePassword.setError(getActivity().getResources().getString(R.string._pass_equal));
            return false;
        }
    }

    //password space contain control
    private boolean passwordContainControl() {
        if(etSignPassword.getText().toString().trim().contains(" ")) {
            etSignPassword.setError(getActivity().getResources().getString(R.string._cannot_contain_spaces));
            return false;
        }else {
            return true;
        }
    }


}
