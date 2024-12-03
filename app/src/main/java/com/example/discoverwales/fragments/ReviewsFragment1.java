package com.example.discoverwales.fragments;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.discoverwales.CulturalLibrary;
import com.example.discoverwales.Library_info1;
import com.example.discoverwales.MakeReview;
import com.example.discoverwales.R;
import com.example.discoverwales.ReviewAdapter;
import com.example.discoverwales.Reviews;
import com.example.discoverwales.connectionPG;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewsFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewsFragment1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final connectionPG con = new connectionPG();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int currentPage = 1;  // Start on the first page
    private static final int REVIEWS_PER_PAGE = 4;


    // TODO: Rename and change types of parameters
    private String museum;
    private String mParam2;
    private RecyclerView reviewsRecyclerView;
    private ReviewAdapter reviewAdapter;
    private List<Reviews.Review> reviewList;


    public ReviewsFragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReviewsFragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewsFragment1 newInstance(String param1, String param2) {
        ReviewsFragment1 fragment = new ReviewsFragment1();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_reviews1, container, false);;
        reviewsRecyclerView = rootView.findViewById(R.id.reviewsRecyclerView);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reviewAdapter = new ReviewAdapter(new ArrayList<>());
        TextView noReviewsMessage = rootView.findViewById(R.id.noReviewsMessage);
        Button makeReview=rootView.findViewById(R.id.makeReviewButton);
        Bundle extras = getActivity().getIntent().getExtras();
        makeReview.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), MakeReview.class);
            intent.putExtra("email", extras.getString("email"));
            intent.putExtra("museum", museum);
            startActivity(intent);
        });

        Button previous=rootView.findViewById(R.id.previousButton);
        reviewsRecyclerView.setAdapter(reviewAdapter);
        Button next=rootView.findViewById(R.id.nextButton);
        reviewList=fetchReviews(currentPage);
        updateButtonStates(noReviewsMessage, previous, next, reviewList.size());
        //previous.setEnabled(currentPage > 1);
        previous.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;  // Decrease the current page
                reviewList=fetchReviews(currentPage);  // Fetch reviews for the new page
                updateButtonStates(noReviewsMessage, previous, next, reviewList.size());
            }
        });

        next.setOnClickListener(v -> {
            currentPage++;  // Increase the current page
            reviewList=fetchReviews(currentPage);  // Fetch reviews for the new page
            updateButtonStates(noReviewsMessage, previous, next, reviewList.size());
        });

        // Set adapter
        return rootView;
    }

    public List<Reviews.Review> fetchReviews(int page) {
        List<Reviews.Review> reviews = new ArrayList<>();
        try {
            // Connect to the database
            Connection connection = con.connectionDB();
            int offset = (page - 1) * REVIEWS_PER_PAGE;
            // Execute SQL query
            String query = "SELECT reviewer_id, date, rating, review_text, museum FROM \"Reviews\" WHERE museum= ? ORDER BY date DESC LIMIT " + REVIEWS_PER_PAGE + " OFFSET " + offset;
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, museum);
            ResultSet rs = pstmt.executeQuery();
            // Process the result set
            while (rs.next()) {
                String query2 = "SELECT \"firstName\", \"lastName\", \"profilePicture\" FROM \"Users\" WHERE email = ?";
                PreparedStatement pstmt2 = connection.prepareStatement(query2);
                pstmt2.setString(1, rs.getString("reviewer_id"));
                ResultSet rs2=pstmt2.executeQuery();
                rs2.next();
                String reviewerName = rs2.getString("firstName")+" "+rs2.getString("lastName");
                String date = rs.getString("date");
                float rating = rs.getFloat("rating");
                String reviewText = rs.getString("review_text");
                String museum = rs.getString("museum");
                String profilePicture=rs2.getString("profilePicture");


                reviews.add(new Reviews.Review(reviewerName, date, rating, reviewText, museum, profilePicture));
            }


            //if(!reviews.isEmpty()) {
                reviewAdapter.updateData(reviews);
            //}

            rs.close();
            pstmt.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return reviews;
    }

    private void updateButtonStates(TextView noReviewsMessage, Button previous, Button next, int reviewsFetched) {
        // Disable "Previous" button on the first page
        if (reviewList.isEmpty()) {
            noReviewsMessage.setVisibility(View.VISIBLE);
            reviewsRecyclerView.setVisibility(View.GONE);
        } else {
            noReviewsMessage.setVisibility(View.GONE);
            reviewsRecyclerView.setVisibility(View.VISIBLE);
        }

        previous.setEnabled(currentPage > 1);

        // Disable "Next" button if fewer than REVIEWS_PER_PAGE reviews are fetched
        next.setEnabled(reviewsFetched == REVIEWS_PER_PAGE);
    }

}
