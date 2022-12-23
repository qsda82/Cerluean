package com.example.datingapptest.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.datingapptest.Home_data_fragment.VIP_Post_Fragment;
import com.example.datingapptest.R;
import com.example.datingapptest.personal_data_setting_activity.Personal_Data_Revise_home_Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {
    private FloatingActionButton main_fab, res_fab, vip_fab;
    private Boolean isopen;
    private TextView demand_txt, supply_txt;
    private Animation fabopen_anim, fabclose_anim;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        main_fab = (FloatingActionButton) getView().findViewById(R.id.main_fab);
        res_fab = (FloatingActionButton) getView().findViewById(R.id.res_fab);
        vip_fab = (FloatingActionButton) getView().findViewById(R.id.vip_fab);
        demand_txt = (TextView) getView().findViewById(R.id.textView21);
        supply_txt = (TextView) getView().findViewById(R.id.textView20);
        isopen = false;
        fabopen_anim = AnimationUtils.loadAnimation(HomeFragment.this.getActivity(), R.anim.fab_open);
        fabclose_anim = AnimationUtils.loadAnimation(HomeFragment.this.getActivity(), R.anim.fab_close);
        fab_action();

    }

    private void fab_action() {
        main_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isopen) {
                    res_fab.startAnimation(fabclose_anim);
                    vip_fab.startAnimation(fabclose_anim);
                    demand_txt.setVisibility(View.INVISIBLE);
                    supply_txt.setVisibility(View.INVISIBLE);
                    isopen = false;
                } else {
                    res_fab.startAnimation(fabopen_anim);
                    vip_fab.startAnimation(fabopen_anim);
                    demand_txt.setVisibility(View.VISIBLE);
                    supply_txt.setVisibility(View.VISIBLE);
                    isopen = true;
                }
            }
        });

        res_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new Personal_Data_Revise_home_Fragment(), null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        vip_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new VIP_Post_Fragment(), null)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

}
