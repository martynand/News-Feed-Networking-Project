package com.example.martyna.scholarshipproject_newsapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * Tag for log messages
     */
    public static final String LOG_TAG = NewsAdapter.class.getName();

    /**
     * ViewHolder for fields of the News.
     */
    static class ViewHolder {
        private TextView titleTextView;
        private TextView sectionTextView;
        private TextView dateTextView;
        private TextView timeTextView;
    }

    /**
     * @param context The current context. Used to inflate the layout file.
     * @param news    A List of News objects to display in a list.
     */

    public NewsAdapter(Activity context, ArrayList<News> news) {
        super(context, 0, news);
    }

    /**
     * @param position    The position in the list of data that should be displayed in the
     *                    list item view.
     * @param convertView The recycled view to populate.
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        final News currentNews = getItem(position);
        ViewHolder holder;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_news, parent, false);

            holder = new ViewHolder();
            holder.titleTextView = (TextView) listItemView.findViewById(R.id.news_title);
            holder.sectionTextView = (TextView) listItemView.findViewById(R.id.news_section);
            holder.dateTextView = (TextView) listItemView.findViewById(R.id.news_date);
            holder.timeTextView = (TextView) listItemView.findViewById(R.id.news_time);
            listItemView.setTag(holder);

        } else {
            holder = (ViewHolder) listItemView.getTag();
        }

        holder.titleTextView.setText(currentNews.getTitle());
        holder.sectionTextView.setText(currentNews.getSection());
        holder.dateTextView.setText(formatDate(currentNews.getDate()));
        holder.timeTextView.setText(formatTime(currentNews.getDate()));

        // Set the proper background color on the section background.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable sectionBackground = (GradientDrawable) holder.sectionTextView.getBackground();

        // Get the appropriate background color based on the current section
        int sectionColor = getSectionColor(currentNews.getSection());

        // Set the color on the section background
        sectionBackground.setColor(sectionColor);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected news.
        listItemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentNews.getNewsUrl()));
                getContext().startActivity(webIntent);
            }
        });

        return listItemView;
    }

    // Convert JSON response publish date to simple date format
    private String formatDate(String s) {
        s = s.substring(0, s.length() - 1);
        String originalFormat = "yyyy-MM-dd'T'HH:mm:ss";
        String newFormat = "dd/MM/yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(originalFormat);
        SimpleDateFormat outputFormat = new SimpleDateFormat(newFormat);
        Date date;
        String newDate = "";
        try {
            date = inputFormat.parse(s);
            newDate = outputFormat.format(date);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "DateTime parse exception: " + e);
            e.printStackTrace();
        }
        return newDate;
    }

    // Convert JSON response publish date to simple time format
    private String formatTime(String s) {
        s = s.substring(0, s.length() - 1);
        String originalFormat = "yyyy-MM-dd'T'HH:mm:ss";
        String newFormat = "HH:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(originalFormat);
        SimpleDateFormat outputFormat = new SimpleDateFormat(newFormat);
        Date date;
        String newTime = "";
        try {
            date = inputFormat.parse(s);
            newTime = outputFormat.format(date);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "DateTime parse exception: " + e);
        }
        return newTime;
    }


    private int getSectionColor(String section) {
        int sectionColorResourceId;
        switch (section) {
            case "Politics":
                sectionColorResourceId = R.color.politics;
                break;
            case "Environment":
                sectionColorResourceId = R.color.environment;
                break;
            case "Education":
                sectionColorResourceId = R.color.education;
                break;
            case "Business":
                sectionColorResourceId = R.color.business;
                break;
            case "UK news":
                sectionColorResourceId = R.color.uk_news;
                break;
            case "Society":
                sectionColorResourceId = R.color.society;
                break;
            case "World news":
                sectionColorResourceId = R.color.world_news;
                break;
            case "Opinion":
                sectionColorResourceId = R.color.opinion;
                break;
            case "Guardian Small Business Network":
                sectionColorResourceId = R.color.guardian_small_business_network;
                break;
            case "Money":
                sectionColorResourceId = R.color.money;
                break;
            default:
                sectionColorResourceId = R.color.other;
                break;
        }
        return ContextCompat.getColor(getContext(), sectionColorResourceId);
    }
}
