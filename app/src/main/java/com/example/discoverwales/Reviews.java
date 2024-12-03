package com.example.discoverwales;

public class Reviews {
    public static class Review {
        private String reviewerName;
        private String date;
        private float rating;
        private String reviewText;
        private String museum;
        private String profilePictureName;


        public Review(String reviewerName, String date, float rating, String reviewText, String museum, String profilePictureName) {
            this.reviewerName = reviewerName;
            this.date = date;
            this.rating = rating;
            this.reviewText = reviewText;
            this.museum=museum;
            this.profilePictureName= profilePictureName;
        }

        // Getters
        public String getReviewerName() { return reviewerName; }
        public String getDate() { return date; }
        public float getRating() { return rating; }
        public String getReviewText() { return reviewText; }
        public String getMuseum() { return museum; }
        public String getProfilePictureName() {
            return profilePictureName;
        }
    }
}
