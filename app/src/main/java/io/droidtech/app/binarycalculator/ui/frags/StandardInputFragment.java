package io.droidtech.app.binarycalculator.ui.frags;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import io.droidtech.app.R;

public class StandardInputFragment extends Fragment {

    public StandardInputFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_standard_input, container, false);
    }
}
