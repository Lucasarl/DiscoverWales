package com.example.discoverwales.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.discoverwales.R;
import com.example.discoverwales.WebViewTouchListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VirtualTourFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VirtualTourFragment1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ViewPager2 viewPager;
    private WebViewTouchListener touchListener;


    // TODO: Rename and change types of parameters
    private String museum;
    private String mParam2;

    public VirtualTourFragment1() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof WebViewTouchListener) {
            touchListener = (WebViewTouchListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement WebViewTouchListener");
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VirtualTourFragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static VirtualTourFragment1 newInstance(String param1, String param2) {
        VirtualTourFragment1 fragment = new VirtualTourFragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            museum = getActivity().getIntent().getStringExtra("museum");
        }
    }
    private WebView webView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_virtual_tour1, container, false);


        webView = rootView.findViewById(R.id.webview);

        webView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    if (touchListener != null) {
                        touchListener.onWebViewTouched(true); // Notify parent to disable swiping
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (touchListener != null) {
                        touchListener.onWebViewTouched(false); // Notify parent to enable swiping
                    }
                    break;
            }
            return false; // Let WebView handle the touch event
        });


        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        webView.setWebViewClient(new WebViewClient());

        String websiteUrl="";

        if(museum.equals("national_museum")) {
            websiteUrl = "https://museum.wales/learning/activity/551/Explore-National-Museum-Cardiff-in-360/";
        } else if(museum.equals("cardiff_castle")) {
            websiteUrl = "https://www.360cities.net/image/the-library-of-the-castle-aparments-cardiff-wales-uk";
        } else if(museum.equals("coal_museum")) {
            websiteUrl = "https://artsandculture.google.com/story/RAVBWlMD58yUIg";
        } else if(museum.equals("legion_museum")) {
            websiteUrl = "https://museum.wales/learning/activity/600/Explore-Roman-Caerleon-in-360/";
        } else if (museum.equals("st_fagans")) {
            websiteUrl = "https://museum.wales/learning/activity/542/Explore-St-Fagans-in-360/";
        } else if (museum.equals("cardiff_museum")) {
            websiteUrl = "https://www.google.com/maps/@51.4802757,-3.1770924,3a,75y,239.49h,102.18t/data=!3m7!1e1!3m5!1sfSKsB3EdgkhGu_opV1bVCQ!2e0!6shttps:%2F%2Fstreetviewpixels-pa.googleapis.com%2Fv1%2Fthumbnail%3Fcb_client%3Dmaps_sv.tactile%26w%3D900%26h%3D600%26pitch%3D-12.180006467865852%26panoid%3DfSKsB3EdgkhGu_opV1bVCQ%26yaw%3D239.49223137888163!7i16384!8i8192?entry=ttu&g_ep=EgoyMDI0MTEyNC4xIKXMDSoASAFQAw%3D%3D";
        }

        webView.loadUrl(websiteUrl);
        viewPager = getActivity().findViewById(R.id.view_pager);


        return rootView;
    }
}