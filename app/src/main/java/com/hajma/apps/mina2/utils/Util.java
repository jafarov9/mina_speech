package com.hajma.apps.mina2.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hajma.apps.mina2.C;
import com.hajma.apps.mina2.receiver.AlarmReceiver;
import com.hajma.apps.mina2.retrofit.MinaInterface;
import com.hajma.apps.mina2.retrofit.model.Alarm;
import com.hajma.apps.mina2.retrofit.model.ContactsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class Util {

    //play social sound
    public void socialSound(String url, MediaPlayer mediaPlayer, Context context){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                if (!mp.isPlaying()) {
                    mp.start();
                } else {
                    mp.pause();
                }
                //audioReady = true;

            });

        } catch (IllegalArgumentException e) {
            Toast.makeText(context.getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(context.getApplicationContext(), "You might not set the URI ", Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Toast.makeText(context.getApplicationContext(), "You might not set the ", Toast.LENGTH_LONG).show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void errorPlayer(int url,MediaPlayer mp,Context context){
        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mp.reset();
            mp = MediaPlayer.create(context,url);
            mp.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void openDialog(int type, String message, FragmentManager fm) {
        SimpleErrorDialog errorDialog = new SimpleErrorDialog(message, type);
        errorDialog.show(fm, "dlg");
    }


    //get all contacts
    public ArrayList<ContactsModel.Contact> getAllContacts(Context context) {
        ArrayList<ContactsModel.Contact> nameList = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
//                String phoneNo = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                String identifier = UUID.randomUUID().toString();
//                String contactSurname = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY));
//                String companyName = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.));
                List<String> number = new ArrayList<>();
//                number.add("");
                if (cur.getInt(cur.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String company = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY));
                        String lastName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DATA2));
                        String firstName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        number.add(phone);



                        ContactsModel.Contact contact = new ContactsModel.Contact(company,number,lastName, UUID.randomUUID().toString(),firstName);
                        nameList.add(contact);

                        for(int i = 0;i < nameList.size();i++) {
                            Log.e("cfcf", nameList.get(i).toString());
                            Log.e("cfcf", "******************************************");
                        }

                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
        return nameList;
    }


    //upload contact
    public void uploadContact(Context context,String id,ArrayList<ContactsModel.Contact> contacts,String token, SharedPreferences sharedPref){

        Gson gson = new Gson();

        try {
            JSONArray tempContacts = new JSONArray(gson.toJson(contacts));
            JSONObject params = new JSONObject();
            params.put("device_type", 2);
            params.put("device_id",id);
            params.put("contacts", (Object) tempContacts);


            final String requestBody = params.toString();

            //Toast.makeText(context, requestBody, Toast.LENGTH_LONG).show();

            Log.e("yaxsi", params.toString());

            HttpsTrustManager.allowAllSSL();
            StringRequest request = new StringRequest(Request.Method.POST, C.URL_CONTACTS_UPLOAD,
                    response ->  {
                        sharedPref.edit().putBoolean("isContactsUploaded", true).apply();
                    },
                    error -> {
                        Log.i("yaxsi","error: "+error.toString());
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Accept", "application/json");
                    Log.e("axaxa", token);
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
            Volley.newRequestQueue(context).add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    //cancel alarm method
    public void cancelAlarm(Context context, Alarm alarm, AlarmManager alarmManager){

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(alarm.getTime().substring(0,2)));
        calendar.set(Calendar.MINUTE, Integer.parseInt(alarm.getTime().substring(3,5)));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);

    }

    //cancel weekly alarm
    public void cancelWeeklyAlarm(Context context, Alarm alarm, AlarmManager alarmManager, int dayOfWeek) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(alarm.getTime().substring(0,2)));
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calendar.set(Calendar.MINUTE, Integer.parseInt(alarm.getTime().substring(3,5)));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Accept the change here at this line to avoid skipping of current week.
        if(calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, new GregorianCalendar().get(Calendar.DAY_OF_WEEK)-1);
        }


        Intent myIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);



    }

    public void cancelRepeatingAlarm(Context context, Alarm alarm, AlarmManager alarmManager) {

        List<String> repeatList = alarm.getRepeat();

        for (int i = 0;i < repeatList.size();i++) {
            switch (repeatList.get(i)) {
                case "Sunday" :
                    cancelWeeklyAlarm(context, alarm, alarmManager, Calendar.SUNDAY);
                    break;

                case "Monday" :
                    cancelWeeklyAlarm(context, alarm, alarmManager, Calendar.MONDAY);
                    break;

                case "Tuesday" :
                    cancelWeeklyAlarm(context, alarm, alarmManager, Calendar.TUESDAY);
                    break;
                case "Wednesday" :
                    cancelWeeklyAlarm(context, alarm, alarmManager, Calendar.WEDNESDAY);
                    break;

                case "Thursday" :
                    cancelWeeklyAlarm(context, alarm, alarmManager, Calendar.THURSDAY);
                    break;

                case "Friday" :
                    cancelWeeklyAlarm(context, alarm, alarmManager, Calendar.FRIDAY);
                    break;

                case "Saturday" :
                    cancelWeeklyAlarm(context, alarm, alarmManager, Calendar.SATURDAY);
                    break;
            }
        }

    }


    //start alarm
    public void startAlarm(Context context, Alarm alarm, AlarmManager alarmManager){

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(alarm.getTime().substring(0,2)));
        calendar.set(Calendar.MINUTE, Integer.parseInt(alarm.getTime().substring(3,5)));
        calendar.set(Calendar.SECOND,0);
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        myIntent.putExtra(C.ALARMMUSIC,alarm.getRingtone());
        //myIntent.putExtra(C.SALUTATION,alarm.getRepeat());



        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.i("data--->",calendar.getTimeInMillis() + "");
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
    }


    public void setAlarmWeekly(Context context, Alarm alarm, AlarmManager alarmManager, int dayOfWeek) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(alarm.getTime().substring(0,2)));
        calendar.set(Calendar.MINUTE, Integer.parseInt(alarm.getTime().substring(3,5)));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        // Accept the change here at this line to avoid skipping of current week.
        if(calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, new GregorianCalendar().get(Calendar.DAY_OF_WEEK)-1);
        }


        Intent myIntent = new Intent(context, AlarmReceiver.class);
        myIntent.putExtra(C.ALARMMUSIC,alarm.getRingtone());
        //myIntent.putExtra(C.SALUTATION,alarm.getRepeat());

        Log.e("ererer", "Alarm time: "+calendar.getTimeInMillis());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
    }


    public void setAlarmDays(Context context, Alarm alarm, AlarmManager alarmManager) {

        List<String> days = alarm.getRepeat();

        for(int i = 0;i < days.size();i++) {
            switch (days.get(i)) {
                case "Sunday" :
                    setAlarmWeekly(context, alarm, alarmManager, Calendar.SUNDAY);
                    break;

                case "Monday" :
                    setAlarmWeekly(context, alarm, alarmManager, Calendar.MONDAY);
                    break;

                case "Tuesday" :
                    setAlarmWeekly(context, alarm, alarmManager, Calendar.TUESDAY);
                    break;
                case "Wednesday" :
                    setAlarmWeekly(context, alarm, alarmManager, Calendar.WEDNESDAY);
                    break;

                case "Thursday" :
                    setAlarmWeekly(context, alarm, alarmManager, Calendar.THURSDAY);
                    break;

                case "Friday" :
                    setAlarmWeekly(context, alarm, alarmManager, Calendar.FRIDAY);
                    break;

                case "Saturday" :
                    setAlarmWeekly(context, alarm, alarmManager, Calendar.SATURDAY);
                    break;

            }
        }

    }


    //get and set shared prefence
    public void setSharedPreference(Context context,String preferenceName,String value){
        SharedPreferences.Editor defaults = context.getSharedPreferences(C.DEFAULTS, Context.MODE_PRIVATE).edit();
        defaults.putString(preferenceName, value);
        defaults.apply();
    }

    public  String getSharedPreference(String preferenceName,Context context){
        SharedPreferences preference = context.getSharedPreferences(C.DEFAULTS,Context.MODE_PRIVATE);
        return preference.getString(preferenceName,null);
    }



}
