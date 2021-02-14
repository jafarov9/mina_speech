package com.hajma.apps.mina2.fragment;

import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hajma.apps.mina2.C;
import com.hajma.apps.mina2.R;
import com.hajma.apps.mina2.adapter.AlarmAdapter;
import com.hajma.apps.mina2.retrofit.ApiUtils;
import com.hajma.apps.mina2.retrofit.MinaInterface;
import com.hajma.apps.mina2.retrofit.model.Alarm;
import com.hajma.apps.mina2.utils.Util;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

public class FragmentAlarm extends Fragment {

    private ImageButton iButtonAddAlarm;
    private RecyclerView rvAlarmList;
    private SharedPreferences sharedPreferences;
    private String token;
    private MinaInterface minaInterface;
    private AlarmAdapter alarmAdapter;
    private ArrayList<Alarm> alarmList = new ArrayList<>();
    private Util util = new Util();
    private AlarmManager alarmManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =
                inflater.inflate(R.layout.fragment_alarm, container, false);

        minaInterface = ApiUtils.getMinaInterface();
        sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);


        iButtonAddAlarm = view.findViewById(R.id.iButtonAddAlarm);

        iButtonAddAlarm.setOnClickListener(v -> {

            Fragment addAlarmFragment = new AddAlarmFragment();

            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                    .replace(R.id.alarm_container, addAlarmFragment, "addAlarmFragment")
                    .addToBackStack("addAlarmFragment")
                    .commit();

        });

        rvAlarmList = view.findViewById(R.id.rvAlarmList);

        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView() {

        alarmList = new ArrayList<>();
        alarmAdapter = new AlarmAdapter(alarmList, getActivity());
        rvAlarmList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvAlarmList.setAdapter(alarmAdapter);

        if(token != null) {
            loadRecyclerViewData();

        }
    }

    private void loadRecyclerViewData() {
        Log.e("alatest", token);

        alarmList.clear();
        alarmAdapter.notifyDataSetChanged();

        minaInterface.getAlarmDetails("Bearer " +token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.isSuccessful()) {

                    Log.e("alatest", "Response success");

                    try {
                        String s = response.body().string();
                        JSONObject success = new JSONObject(s).getJSONObject("success");
                        JSONArray data = success.getJSONArray("data");

                        for(int i = 0;i < data.length();i++) {
                            JSONObject j = data.getJSONObject(i);
                            JSONArray repeat = j.getJSONArray("repeat");
                            Alarm a = new Alarm();


                            a.setId(j.getInt("id"));
                            a.setAlarm_mode(j.getString("alarm_mode"));
                            a.setLabel(j.getString("label"));
                            a.setRingtone(j.getString("ringtone"));
                            a.setSnooze_time(j.getString("snooze_time"));
                            a.setTime(j.getString("time"));
                            a.setStatus(j.getBoolean("status"));

                            List<String> repeatList = new ArrayList<>();

                            for(int k = 0; k < repeat.length();k++) {
                                String tmp = repeat.getString(k);

                                repeatList.add(tmp);
                            }

                            a.setRepeat(repeatList);

                            alarmList.add(a);
                        }


                        alarmAdapter.notifyDataSetChanged();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        String errorBOdy = response.errorBody().string();
                        Log.e("alatest", errorBOdy);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("alatest", "Response fail: "+t.getMessage());

            }
        });

        delete();

    }


    private void delete() {
        alarmAdapter.setOnItemClickListener((imageView, position) -> {
            //delete alarm api
            deleteAlarmFromServer(alarmAdapter.getAlarmAt(position).getId());

            //delete alarm from alarm manager
            List<String> rDays = alarmAdapter.getAlarmAt(position).getRepeat();
            if(rDays.size() > 0) {
                util.cancelRepeatingAlarm(getContext(), alarmAdapter.getAlarmAt(position), alarmManager);
            } else {
                util.cancelAlarm(getContext(),alarmAdapter.getAlarmAt(position),alarmManager);
            }
        });

        alarmAdapter.setOnCheckedChangeListener((sw, position) -> {
            if(sw.isChecked()){
                alarmAdapter.getAlarmAt(position).setStatus(true);

                //update alarm status api
                try {
                    changeAlarmStatusFromServer(alarmAdapter.getAlarmAt(position).getId(), true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //alarmViewModel.update(alarmAdapter.getAlarmAt(position));

                List<String> daysRepeat = new ArrayList<>();

                if(daysRepeat.size() > 0) {
                    util.startAlarm(getContext(),alarmAdapter.getAlarmAt(position),alarmManager);
                } else {
                    util.setAlarmDays(getActivity(), alarmAdapter.getAlarmAt(position), alarmManager);
                }

            }else {
                alarmAdapter.getAlarmAt(position).setStatus(false);

                //update alarm status api
                try {
                    changeAlarmStatusFromServer(alarmAdapter.getAlarmAt(position).getId(), false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                List<String> rDays = alarmAdapter.getAlarmAt(position).getRepeat();

                if(rDays.size() > 0) {
                    util.cancelRepeatingAlarm(getContext(), alarmAdapter.getAlarmAt(position), alarmManager);
                }else {
                    util.cancelAlarm(getContext(),alarmAdapter.getAlarmAt(position), alarmManager);
                }

            }
        });
    }

    private void deleteAlarmFromServer(int id) {

        String url = C.URL_DELETE_ALARM + String.valueOf(id);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("fsss", response);
                loadRecyclerViewData();
            }


        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("fsss", error.toString());
                Toast.makeText(getActivity(), "Delete error", Toast.LENGTH_LONG).show();
            }

        }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Accept", "application/json");
                Log.e("axaxa", token);
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };


        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private void changeAlarmStatusFromServer(int id, boolean status) throws JSONException {

        String url = C.URL_CHANGE_ALARM_STATUS + String.valueOf(id);

        JSONObject statusObject = new JSONObject();
        statusObject.put("status", status);

        final String requestBody = statusObject.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("fsss", response);

            }


        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("fsss", error.toString());
                Toast.makeText(getActivity(), "Delete error", Toast.LENGTH_LONG).show();
            }

        }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + token);
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return requestBody == null ? null : requestBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };


        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}
