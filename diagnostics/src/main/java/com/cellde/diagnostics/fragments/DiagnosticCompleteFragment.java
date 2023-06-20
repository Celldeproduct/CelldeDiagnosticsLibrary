package com.cellde.diagnostics.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cellde.diagnostics.LibActivity;
import com.cellde.diagnostics.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiagnosticCompleteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiagnosticCompleteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DiagnosticCompleteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiagnosticCompleteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiagnosticCompleteFragment newInstance(String param1, String param2) {
        DiagnosticCompleteFragment fragment = new DiagnosticCompleteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((LibActivity) getActivity())
                .setActionBarTitle("Complete");
        return inflater.inflate(R.layout.fragment_diagnostic_complete, container, false);
    }
}