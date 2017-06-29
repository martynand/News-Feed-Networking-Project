package com.example.martyna.scholarshipproject_newsapp;

import android.os.Parcel;
import android.os.Parcelable;

public class News implements Parcelable {

    /**
     * News title
     */
    private String mTitle;
    /**
     * News section
     */
    private String mSection;
    /**
     * Publish date
     */
    private String mDate;
    /**
     * News URL
     */
    private String mNewsUrl;

    /**
     * Create a new News object from four inputs
     *
     * @param title   is the title of the news
     * @param section is the author of the news
     * @param date    is the publish date of the news
     * @param newsUrl is the URL of the news
     */
    public News(String title, String section, String date, String newsUrl) {
        mTitle = title;
        mSection = section;
        mDate = date;
        mNewsUrl = newsUrl;
    }

    /**
     * Get the title of the news
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Get the section of the news
     */
    public String getSection() {
        return mSection;
    }

    /**
     * Get the publish date of the news
     */
    public String getDate() {
        return mDate;
    }

    /**
     * Get the URL of the news
     */
    public String getNewsUrl() {
        return mNewsUrl;
    }


    protected News(Parcel in) {
        mTitle = in.readString();
        mSection = in.readString();
        mDate = in.readString();
        mNewsUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mSection);
        dest.writeString(mDate);
        dest.writeString(mNewsUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };
}
