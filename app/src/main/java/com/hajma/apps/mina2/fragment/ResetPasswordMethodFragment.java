package com.hajma.apps.mina2.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hajma.apps.mina2.C;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.retrofit.ApiUtils;
import com.hajma.apps.mina2.retrofit.MinaInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordMethodFragment extends Fragment {

    private EditText etResetEmail;
    private EditText etResetNumber;
    private TextView txtAlreadyHaveAccountResetPassword;
    private ProgressBar pbResetPasswordLoading;
    private MinaInterface minaInterface;
    private int forgotType;
    private Button btnResetPassword;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reset_password_method_fragment, container, false);

        minaInterface = ApiUtils.getMinaInterface();

        //init views
        btnResetPassword = view.findViewById(R.id.btnResetPassword);
        etResetEmail = view.findViewById(R.id.etResetEmail);
        etResetNumber = view.findViewById(R.id.etResetNumber);
        txtAlreadyHaveAccountResetPassword = view.findViewById(R.id.txtAlreadyHaveAccountResetPassword);
        pbResetPasswordLoading = view.findViewById(R.id.pbResetPasswordLoading);

        etResetEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().equals("")) {
                    etResetNumber.setEnabled(true);
                    forgotType = -1;
                }else {
                    etResetNumber.setEnabled(false);
                    forgotType = C.FORGOT_TYPE_EMAIL;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etResetNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")) {
                    etResetEmail.setEnabled(true);
                    forgotType = -1;
                }else {
                    etResetEmail.setEnabled(false);
                    forgotType = C.FORGOT_TYPE_PHONE;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editTextControl()) {

                    switch (forgotType) {
                        case C.FORGOT_TYPE_EMAIL :
                            resetPasswordWithEmail();
                            break;

                        case C.FORGOT_TYPE_PHONE :
                            resetPasswordWithPhone();
                            break;
                    }


                }

            }
        });

        return view;
    }

    private void resetPasswordWithPhone() {

        btnResetPassword.setVisibility(View.INVISIBLE);
        pbResetPasswordLoading.setIndeterminate(true);
        pbResetPasswordLoading.setVisibility(View.VISIBLE);

        String phone = etResetNumber.getText().toString().trim();

        minaInterface.forgotPasswordWithPhone(phone).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                pbResetPasswordLoading.setIndeterminate(false);
                pbResetPasswordLoading.setVisibility(View.GONE);
                btnResetPassword.setVisibility(View.VISIBLE);

                if(response.isSuccessful()) {
                    try {
                        String s = response.body().string();

                        JSONObject success = new JSONObject(s).getJSONObject("success");
                        Toast.makeText(getActivity(), success.getString("message"), Toast.LENGTH_LONG).show();
                        openResetPasswordFragment();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String s = response.errorBody().string();
                        JSONObject error = new JSONObject(s).getJSONObject("error");

                        Toast.makeText(getActivity(), error.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(getActivity(), "Check internet connection", Toast.LENGTH_LONG).show();
                pbResetPasswordLoading.setIndeterminate(false);
                pbResetPasswordLoading.setVisibility(View.GONE);
                btnResetPassword.setVisibility(View.VISIBLE);
            }
        });


    }

    private void resetPasswordWithEmail() {

        btnResetPassword.setVisibility(View.INVISIBLE);
        pbResetPasswordLoading.setIndeterminate(true);
        pbResetPasswordLoading.setVisibility(View.VISIBLE);

        String email = etResetEmail.getText().toString().trim();

        minaInterface.forgotPasswordWithEmail(email).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                pbResetPasswordLoading.setIndeterminate(false);
                pbResetPasswordLoading.setVisibility(View.GONE);
                btnResetPassword.setVisibility(View.VISIBLE);

                if(response.isSuccessful()) {
                    try {
                        String s = response.body().string();

                        JSONObject success = new JSONObject(s).getJSONObject("success");
                        Toast.makeText(getActivity(), success.getString("message"), Toast.LENGTH_LONG).show();
                        openResetPasswordFragment();



                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String s = response.errorBody().string();
                        JSONObject error = new JSONObject(s).getJSONObject("error");

                        Toast.makeText(getActivity(), error.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(getActivity(), "Check internet connection", Toast.LENGTH_LONG).show();
                pbResetPasswordLoading.setIndeterminate(false);
                pbResetPasswordLoading.setVisibility(View.GONE);
                btnResetPassword.setVisibility(View.VISIBLE);
            }
        });



    }

    private void openResetPasswordFragment() {

        Fragment resetPasswordFragment = new ResetPasswordFragment();

        Objects.requireNonNull(getActivity())
                .getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                .replace(R.id.sign_fragment_container, resetPasswordFragment, "resetPasswordFragment")
                .addToBackStack("resetPasswordFragment")
                .commit();



    }

    private boolean editTextControl() {
        if(!etResetEmail.getText().toString().trim().isEmpty() | !etResetNumber.getText().toString().trim().isEmpty()) {
            return true;
        }else {
            Toast.makeText(getActivity(), "Choose one forgot type", Toast.LENGTH_LONG).show();
            return false;
        }

    }



}
