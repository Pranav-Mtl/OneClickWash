package com.example.admin.oneclickwash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class TutorialFragmentFour extends Fragment {

    public TutorialFragmentFour() {
        // Required empty public constructor
    }
    Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vv=inflater.inflate(R.layout.fragment_tutorial_fragment_four, container, false);

        btn= (Button) vv.findViewById(R.id.ll_tut_five);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SignUpScreen.class));
                getActivity().finish();
            }
        });

        return vv;
    }

}
