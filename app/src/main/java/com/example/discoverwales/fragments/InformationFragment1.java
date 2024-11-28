package com.example.discoverwales.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.discoverwales.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InformationFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InformationFragment1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "cardiff_castle";

    // TODO: Rename and change types of parameters
    private String museum;

    public InformationFragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param museum Parameter 1.
     * @return A new instance of fragment InformationFragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static InformationFragment1 newInstance(String museum) {
        InformationFragment1 fragment = new InformationFragment1();
        Bundle args = new Bundle();
        System.out.println("MUSEUM: "+museum);
        args.putString(ARG_PARAM1, museum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            System.out.println("SIUUUUUU");
            museum = getArguments().getString(ARG_PARAM1);
        }*/

        if (getActivity() != null) {
            museum = getActivity().getIntent().getStringExtra("museum");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_information1, container, false);
        if (museum != null) {
            if (museum.equals("cardiff_castle")) {
                // Find a TextView in the layout and set its text
                TextView textView = rootView.findViewById(R.id.first_text_view);
                textView.setText("Welcome to Cardiff Castle!");
            }
        }

        return rootView;

    }
}