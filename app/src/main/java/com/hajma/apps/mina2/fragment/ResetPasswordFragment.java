package com.hajma.apps.mina2.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.retrofit.ApiUtils;
import com.hajma.apps.mina2.retrofit.MinaInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordFragment extends Fragment {

    private EditText etCode;
    private EditText etNewPassword;
    private Button btnResetNewPassword;
    private ProgressBar pbNewPasswordLoading;
    private MinaInterface minaInterface;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.reset_password_fragment, container, false);

        minaInterface = ApiUtils.getMinaInterface();


        //init views
        etCode = view.findViewById(R.id.etCode);
        etNewPassword = view.findViewById(R.id.etNewPassword);
        btnResetNewPassword = view.findViewById(R.id.btnResetNewPassword);
        pbNewPasswordLoading = view.findViewById(R.id.pbNewPasswordLoading);

        btnResetNewPassword.setOnClickListener(v -> {
            if(editTextControl()) {
                resetPassword();
            }
        });

        return view;
    }

    private void resetPassword() {

        btnResetNewPassword.setVisibility(View.INVISIBLE);
        pbNewPasswordLoading.setIndeterminate(true);
        pbNewPasswordLoading.setVisibility(View.VISIBLE);


        String code = etCode.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();

        minaInterface.resetPassword(code, newPassword).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                pbNewPasswordLoading.setIndeterminate(false);
                pbNewPasswordLoading.setVisibility(View.GONE);
                btnResetNewPassword.setVisibility(View.VISIBLE);

                if(response.isSuccessful()) {

                    FragmentManager fm = getActivity().getSupportFragmentManager();

                    Fragment loginFragment = new LoginFragment();

                    //clear back stack fragments
                    for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                        fm.popBackStack();
                    }

                    fm.beginTransaction()
                            .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                            .add(R.id.sign_fragment_container, loginFragment, "loginFragment")
                            .addToBackStack("loginFragment")
                            .commit();

                }else {

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
                pbNewPasswordLoading.setIndeterminate(false);
                pbNewPasswordLoading.setVisibility(View.GONE);
                btnResetNewPassword.setVisibility(View.VISIBLE);
            }
        });


    }

    private boolean editTextControl() {
        if(etCode.getText().toString().trim().isEmpty()) {
            etCode.setError(getActivity().getResources().getString(R.string._required_field));
            return false;
        }

        if(etNewPassword.getText().toString().trim().isEmpty()) {
            etNewPassword.setError(getActivity().getResources().getString(R.string._required_field));
            return false;
        }
        return true;
    }
}
