package com.example.newsapp;

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
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {


     /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = NewsActivity.class.getSimpleName();

    /**
     * The constant value for the news loader ID. We have the option of choosing any integer.
     * This is helpful if you are using multiple loaders
     */
    private static final int NEWS_LOADER_ID = 1;

    /** Adapter fot the list for the news articles
     *
     */
    private NewsAdapter newsAdapter;

    /**
     * This TextView is available when the list is blank
     */
    private TextView emptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        // Locate the reference to the {@link ListView} in the layout
        ListView newsListView = findViewById(R.id.list);

        // Create the new {@link NewsAdapter} that will create the empty list for news article
        newsAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Put the adapter on the {@link ListView}
        // so the list will be populated in the user interface
        newsListView.setAdapter(newsAdapter);

        // Create an item click listener on the ListView, that sends the intent to the Internet
        // to open the website that provides information about the selected article
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the recent news article that was selected
                News currentNewsArticle = newsAdapter.getItem(position);

                // Change the String URL into a URI object (to pass into a Intent constructor)
                assert currentNewsArticle != null;
                Uri newsUri = Uri.parse(currentNewsArticle.getNewsArticleUrl());

                // Create the new intent to look at the news article URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to initiate the new activity
                startActivity(websiteIntent);

            }
        });

        // Locate the reference to the {@link TextView} in the layout
        emptyStateTextView = findViewById(R.id.blank_view);

        // Retrieve the reference to the ConnectivityManager to check the network
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Retrieve the details on the recently active default network data
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // If there is connectivity collect the data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get the reference to the LoaderManager to communicate with loaders
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader and also pass the int for the ID constant above
            // pass the null for the bundle. Pass the activity for the LoaderCallbacks
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);

            // Set the {@link TextView} to let us know if the display is empty
            newsListView.setEmptyView(emptyStateTextView);
        } else {
            // If not display the error
            // Before that conceal the loading indicator so the error message is displayed
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Make the empty state display when the internet connection isn't active
            emptyStateTextView.setText(R.string.no_internet_connection);

        }
    }

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        // Create a new loader for the given URL
        return new NewsLoader(this, createSearchUrl());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        // Conceal the loading indicator since the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No articles found."
        emptyStateTextView.setText(R.string.no_news);

        // Clear the data adapter of the previous news data
        newsAdapter.clear();

        // If there is a relevant list of {@link New}s then include them in the adapter's
        // data set. This will initiate the ListView to be updated
        if (news != null && !news.isEmpty()) {
            newsAdapter.addAll(news);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Erase the adapter of the previous article data
        newsAdapter.clear();

    }

    /**
     * Example of the desired URL:
     * https://content.guardianapis.com/search?api-key=25e0aceb-363b-4118-8db6-bdbfe58e70de
     */
    private String createSearchUrl() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // 'getString' retrieves the String value from the preferences.
        // The second parameter will be the default value for the preference
        String filterBy = sharedPrefs.getString(getString(R.string.settings_filter_by_key), getString(R.string.settings_filter_by_default));

        // 'getString' retrieves the String value from the preferences.
        // The second parameter will be the default value for the preference
        String arrangeBy = sharedPrefs.getString(getString(R.string.settings_arrange_by_key), getString(R.string.settings_arrange_by_default));



        // Create the Guardian API search string
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(getString(R.string.transfer_protocol))
                .authority(getString(R.string.transfer_authority))
                .appendPath(getString(R.string.transfer_path))
                .appendQueryParameter(getString(R.string.query_tag), getString(R.string.query_param))
                .appendQueryParameter(getString(R.string.query_tag_from_date), getString(R.string.query_value_from_date));
        if (!filterBy.equals(getString(R.string.settings_filter_by_all_value))) {
            builder = builder.appendQueryParameter(getString(R.string.query_tag_section), arrangeBy);
        }
        builder = builder.appendQueryParameter(getString(R.string.query_tag_show_tag), getString(R.string.query_value_show_tag))
                .appendQueryParameter(getString(R.string.query_tag_show_fields), getString(R.string.query_value_show_fields))
                .appendQueryParameter(getString(R.string.query_tag_arrange_by), arrangeBy)
                .appendQueryParameter(getString(R.string.query_tag_api_key), getString(R.string.query_value_api_key));

        Log.d(LOG_TAG, builder.build().toString());
        return builder.build().toString();
    }


    @Override
    // The method initialize the contents for the Activity's menu options.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in the XML file
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
