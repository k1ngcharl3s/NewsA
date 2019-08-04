package com.example.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

class NewsAdapter extends ArrayAdapter<News> {

    NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Examine to see if the current view is being reused, if not maximize the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        // Retrieve the {@link News} object located a this position of the list
        News currentNews = getItem(position);

        // Locate the TextView in the news_list_item.xml layout with the ID section_name
        TextView sectionTextView = listItemView.findViewById(R.id.section_name);
        assert currentNews != null;
        sectionTextView.setText(currentNews.getNewsArticleSection());

        // Locate the TextView in the news_list_item.xml layout that has the ID date and time
        TextView dateAndTimeTextView = listItemView.findViewById((R.id.date_time));
        // https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
        String publicationDateTime = new SimpleDateFormat("MMM dd, yyyy, hh:mm a",
                Locale.getDefault()).format(currentNews.getNewsArticleDateTime());
        dateAndTimeTextView.setText(publicationDateTime);

        if (!TextUtils.isEmpty(currentNews.getNewsThumbnail())) {
            // Locate the ImageView in the news_list_item.xml layout w/ the thumbnail.
            ImageView thumbnailImageView = listItemView.findViewById(R.id.thumbnail);
            // https://square.github.io/picasso/
            Picasso.get().load(currentNews.getNewsThumbnail()).resize(150, 90).into(thumbnailImageView);
        }

        // Locate the TextView in the news_list.item.xml with the article name
        TextView articleTitleTextView = listItemView.findViewById(R.id.article_name);
        articleTitleTextView.setText(currentNews.getNewsArticleName());

        // Locate the TextView in the news_list_item.xml layout with the writer
        TextView authorTextView = listItemView.findViewById(R.id.writer);
        authorTextView.setText(currentNews.getNewsArticleWriter());

        // Retrieve the entire list item layout so it can be displayed
        return listItemView;
    }
}
