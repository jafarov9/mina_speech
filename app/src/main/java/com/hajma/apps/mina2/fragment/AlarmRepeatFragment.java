package com.hajma.apps.mina2.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hajma.apps.mina2.R;

import java.util.ArrayList;

public class AlarmRepeatFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private CheckBox cbxSunday, cbxMonday, cbxTuesday
            , cbxWednesday, cbxThursday, cbxFriday, cbxSaturday;

    private ArrayList<String> weekDays;
    private ImageButton iButtonBackAlarmRepeat;


    public AlarmRepeatFragment(ArrayList<String> weekDays) {
        this.weekDays = weekDays;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view
                = inflater.inflate(R.layout.alarm_repeat_fragment, container, false);

        //init views
        iButtonBackAlarmRepeat = view.findViewById(R.id.iButtonBackAlarmRepeat);
        cbxSunday = view.findViewById(R.id.cbxSunday);
        cbxMonday = view.findViewById(R.id.cbxMonday);
        cbxTuesday = view.findViewById(R.id.cbxTuesday);
        cbxWednesday = view.findViewById(R.id.cbxWednesday);
        cbxThursday = view.findViewById(R.id.cbxThursday);
        cbxFriday = view.findViewById(R.id.cbxFriday);
        cbxSaturday = view.findViewById(R.id.cbxSaturday);

        for(int i = 0; i < weekDays.size();i++) {
            switch (weekDays.get(i)) {
                case "Sunday" :
                    cbxSunday.setChecked(true);
                    break;

                case "Monday" :
                    cbxMonday.setChecked(true);
                    break;

                case "Tuesday" :
                    cbxTuesday.setChecked(true);
                    break;

                case "Wednesday" :
                    cbxWednesday.setChecked(true);
                    break;
                case "Thursday" :
                    cbxThursday.setChecked(true);
                    break;
                case "Friday" :
                    cbxFriday.setChecked(true);
                    break;

                case "Saturday" :
                    cbxSaturday.setChecked(true);
                    break;

            }
        }

        //set onchecked listener to check boxes
        cbxSunday.setOnCheckedChangeListener(this);
        cbxMonday.setOnCheckedChangeListener(this);
        cbxTuesday.setOnCheckedChangeListener(this);
        cbxWednesday.setOnCheckedChangeListener(this);
        cbxThursday.setOnCheckedChangeListener(this);
        cbxFriday.setOnCheckedChangeListener(this);
        cbxSaturday.setOnCheckedChangeListener(this);

        iButtonBackAlarmRepeat.setOnClickListener(v -> getActivity().onBackPressed());

        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {

            case R.id.cbxSunday :

                if (isChecked) {
                    weekDays.add("Sunday");
                }else {
                    weekDays.remove("Sunday");
                }
                break;

            case R.id.cbxMonday :


                if (isChecked) {
                    weekDays.add("Monday");
                }else {
                    weekDays.remove("Monday");
                }

                break;

            case R.id.cbxTuesday :

                if (isChecked) {
                    weekDays.add("Tuesday");
                }else {
                    weekDays.remove("Tuesday");
                }

                break;

            case R.id.cbxWednesday :

                if (isChecked) {
                    weekDays.add("Wednesday");
                }else {
                    weekDays.remove("Wednesday");
                }

                break;

            case R.id.cbxThursday :

                if (isChecked) {
                    weekDays.add("Thursday");
                }else {
                    weekDays.remove("Thursday");
                }

                break;

            case R.id.cbxFriday :

                if (isChecked) {
                    weekDays.add("Friday");
                }else {
                    weekDays.remove("Friday");
                }

                break;

            case R.id.cbxSaturday :

                if (isChecked) {
                    weekDays.add("Saturday");
                }else {
                    weekDays.remove("Saturday");
                }

                break;

        }

    }

    public ArrayList<String> getWeekDays() {
        return weekDays;
    }

}
