package com.example.virtualtravelapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.virtualtravelapp.R;
import com.example.virtualtravelapp.activity.AdminDiaDanhActivity;

public class AdminFragment extends Fragment {
    CardView crdLocation, crdPlace, crdIntro, crdRestaurant, crdHotel, crdVehicle, crdExper, crdAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_admin, container, false);
        getActivity().setTitle("Quản trị du lịch");
        crdLocation = (CardView) root.findViewById(R.id.crd_Location);
        crdPlace = (CardView) root.findViewById(R.id.crd_Place);
        crdIntro = (CardView) root.findViewById(R.id.crd_Intro);
        crdRestaurant = (CardView) root.findViewById(R.id.crd_Restaurant);
        crdHotel = (CardView) root.findViewById(R.id.crd_Hotel);
        crdVehicle = (CardView) root.findViewById(R.id.crd_Vehicle);
        crdExper = (CardView) root.findViewById(R.id.crd_Exper);
        crdAccount = (CardView) root.findViewById(R.id.crd_Account);
        crdLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdminDiaDanhActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}
