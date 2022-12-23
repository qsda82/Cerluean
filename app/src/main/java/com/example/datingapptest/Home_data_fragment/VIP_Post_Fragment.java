package com.example.datingapptest.Home_data_fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.datingapptest.R;
import com.example.datingapptest.airport_list;
import com.example.datingapptest.personal_data_setting_activity.Personal_Data_Revise_Birthday_Fragment;
import com.example.datingapptest.register_page;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VIP_Post_Fragment extends Fragment {
    private Spinner spin_supply_demand, spin_airline_alliance, spin_viproom_name,spin_sex,spin_payment;
    private AutoCompleteTextView auto_complete_airport;
    private TextView date;
    private EditText  Terminal_building,ps,age;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home_vip_post, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        spin_supply_demand = (Spinner) getView().findViewById(R.id.supply_demand);
        auto_complete_airport = (AutoCompleteTextView) getView().findViewById(R.id.auto_complete_airport);
        spin_airline_alliance = (Spinner) getView().findViewById(R.id.airline_alliance);
        spin_viproom_name = (Spinner) getView().findViewById(R.id.viproom_name);
        spin_sex=(Spinner)getView().findViewById(R.id.dating_sex);
        spin_payment=(Spinner)getView().findViewById(R.id.payment);
        date=(TextView)getView().findViewById(R.id.date);
        age=(EditText) getView().findViewById(R.id.dating_age);
        Terminal_building=(EditText) getView().findViewById(R.id.Terminal_building);
        ps=(EditText) getView().findViewById(R.id.ps);
        init();
    }

    private void init() {
        final airport_list airport_list = new airport_list();
        String[] choose_sup_dem = new String[]{"供給", "需求"};
        String[] sex = new String[]{"男", "女"};
        final String[] pay = new String[]{"是", "否"};
        ArrayAdapter<String> adaptersup_demand = new ArrayAdapter<String>(VIP_Post_Fragment.this.getContext(), android.R.layout.simple_list_item_1, choose_sup_dem);
        adaptersup_demand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_supply_demand.setAdapter(adaptersup_demand);

        String[] choose_alliance = new String[]{"無", "星空聯盟（Star Alliance）", "寰宇一家（OneWorld）", "天合聯盟（SkyTeam）"};
        /*航空聯盟*/
        ArrayAdapter<String> adapteralliance = new ArrayAdapter<String>(VIP_Post_Fragment.this.getContext(), android.R.layout.simple_list_item_1, choose_alliance);
        adapteralliance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_airline_alliance.setAdapter(adapteralliance);
        spin_airline_alliance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getSelectedItem().toString() == "星空聯盟（Star Alliance）") {
                    star_alliance_vip_choose(airport_list.Star_Alliance_vip_room);
                    auto_complete_airport.setEnabled(true);
                }else if(parent.getSelectedItem().toString() == "寰宇一家（OneWorld）"){
                    star_alliance_vip_choose(airport_list.OneWordld_vip_room);
                    auto_complete_airport.setEnabled(true);
                }else if(parent.getSelectedItem().toString() == "天合聯盟（SkyTeam）"){
                    star_alliance_vip_choose(airport_list.SkyTeam_vip_room);
                    auto_complete_airport.setEnabled(true);
                }else if (parent.getSelectedItem().toString() == "無"){
                    auto_complete_airport.setEnabled(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(VIP_Post_Fragment.this.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthofYear, int dayOfMonth) {
                        //顯示生日（月份要+1，因為這個方法是從0開始算的）
                       date.setText(String.format("%d/%d/%d", year, monthofYear + 1, dayOfMonth));
                    }
                    //設定初始的顯示日期
                }, 2020, 0, 1).show();
            }
        });

        ArrayAdapter<String> adaptersex= new ArrayAdapter<String>(VIP_Post_Fragment.this.getContext(), android.R.layout.simple_list_item_1, sex);
        adaptersex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_sex.setAdapter(adaptersex);

        ArrayAdapter<String> adapterpayment= new ArrayAdapter<String>(VIP_Post_Fragment.this.getContext(), android.R.layout.simple_list_item_1, pay);
        adapterpayment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_payment.setAdapter(adapterpayment);

        spin_supply_demand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).toString()=="供給"){
                    Terminal_building.setEnabled(true);
                    spin_payment.setEnabled(false);
                }else if(parent.getItemAtPosition(position).toString()=="需求"){
                    Terminal_building.setEnabled(false);
                    spin_payment.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void star_alliance_vip_choose(final List<String> viproom) {
        airport_list airport_list = new airport_list();
        /*機場*/
        ArrayAdapter<String> adapterairport = new ArrayAdapter<String>(VIP_Post_Fragment.this.getContext(), android.R.layout.simple_list_item_1, airport_list.airport);
        adapterairport.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        auto_complete_airport.setAdapter(adapterairport);

        auto_complete_airport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<String> show_vip_room = new ArrayList<String>();
                for (int i = 0; i < viproom.size(); i++) {
                    if (viproom.get(i).substring(1, 4).trim().equals(parent.getItemAtPosition(position).toString().trim())) {
                        show_vip_room.add(viproom.get(i).substring(5));
                    }
                }
                /*貴賓室*/
                ArrayAdapter<String> adapterviproom_name = new ArrayAdapter<String>(VIP_Post_Fragment.this.getContext(), android.R.layout.simple_spinner_item, show_vip_room);
                adapterviproom_name.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin_viproom_name.setAdapter(adapterviproom_name);
            }
        });
    }
}
