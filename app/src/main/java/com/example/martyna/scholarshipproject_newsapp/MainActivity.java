package com.example.martyna.scholarshipproject_newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    /**
     * URL for news data from Guardian API
     */
    private static final String NEWS_REQUEST_URL = "http://content.guardianapis.com/search?tag=politics/politics&from-date=2014-01-01&api-key=test";

    /**
     * Constant value for the news loader ID
     */
    private static final int NEWS_LOADER_ID = 1;

    /**
     * Adapter for the list of news
     */
    private NewsAdapter mNewsAdapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    /**
     * ProgressBar that is displayed when the data is loaded
     */
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of news as input
        mNewsAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mNewsAdapter);

        // Find the reference to the progress bar in a layout
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        // Find the reference to the empty text view in a layout and set empty view
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);


        if (isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);

        } else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet);
        }
    }

    // Helper method to check network connection
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String choseSection = sharedPrefs.getString(
                getString(R.string.settings_search_by_key),
                getString(R.string.settings_search_by_default)
        );

        Uri baseUri = Uri.parse(NEWS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("q", choseSection);

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {

        mEmptyStateTextView.setText(R.string.no_news);
        mProgressBar.setVisibility(View.GONE);
        mNewsAdapter.clear();

        if (news != null && !news.isEmpty()) {
            mNewsAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mNewsAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
