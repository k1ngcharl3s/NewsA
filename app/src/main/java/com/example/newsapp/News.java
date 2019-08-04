package com.example.newsapp;

import java.util.Date;

class News {

    /**
     * Section for news
     */
    private final String mNewsArticleSection;

    /**
     * Article date and time for the articles
     */
    private final Date mNewsArticleDateTime;

    /**
     * Writer of the article
     */
    private final String mNewsArticleWriter;

    /**
     * Name of the article
     */
    private final String mNewsArticleName;

    /**
     * The URL address for the Article
     */
    private final String mNewsArticleUrl;

    /**
     * The URL for the thumbnail
     */
    private final String mNewsThumbnail;

    /**
     * Create the new News object for the article.
     *
     * @param newsArticleSection is the name for the section where the News articles are
     * @param newsArticleDateTime is the date and time of the News that is published
     * @param newsArticleWriter is the name of the writer for the News article
     * @param newsArticleName is the name of the News article
     * @param newsArticleUrl is the URL for the article
     * @param newsThumbnail is the URL for the thumbnail images that is associated with the article
     */

    public News(String newsArticleSection, Date newsArticleDateTime, String newsArticleWriter, String newsArticleName,
                String newsArticleUrl, String newsThumbnail) {
        mNewsArticleSection = newsArticleSection;
        mNewsArticleDateTime = newsArticleDateTime;
        mNewsArticleWriter = newsArticleWriter;
        mNewsArticleName = newsArticleName;
        mNewsArticleUrl = newsArticleUrl;
        mNewsThumbnail = newsThumbnail;

    }
    /** Get the name for the section where the News articles are located
     *
     */
    public String getNewsArticleSection () {
        return mNewsArticleSection;
    }
    /** Get the date and time for the News article that is published
     *
     */
    public Date getNewsArticleDateTime() {
        return mNewsArticleDateTime;
    }
    /** Get the name of the writer for the News Article
     *
     */
    public String getNewsArticleWriter(){
        return mNewsArticleWriter;
    }
    /** Get the name of the News Article
     *
     */
    public String getNewsArticleName() {
        return mNewsArticleName;
    }
    /** Get the URL for the News Article
     *
     */
    public String getNewsArticleUrl() {
        return mNewsArticleUrl;
    }
    /** Get the URL for the thumbnail associated with the Article
     *
     */
    public String getNewsThumbnail(){
        return mNewsThumbnail;
    }

}
