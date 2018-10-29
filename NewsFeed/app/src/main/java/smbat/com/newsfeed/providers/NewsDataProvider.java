package smbat.com.newsfeed.providers;

import android.content.Context;

import java.lang.ref.SoftReference;
import java.util.List;

import smbat.com.newsfeed.api.models.Content;
import smbat.com.newsfeed.api.models.Result;
import smbat.com.newsfeed.database.entities.News;
import smbat.com.newsfeed.tasks.LoadNewsDetailFromDBTask;
import smbat.com.newsfeed.tasks.LoadNewsDetailTask;
import smbat.com.newsfeed.tasks.LoadNewsFromDBTask;
import smbat.com.newsfeed.tasks.LoadNewsTask;
import smbat.com.newsfeed.tasks.LoadPinnedNewsTask;

public class NewsDataProvider {

    private static volatile NewsDataProvider sInstance;
    private static final Object lockObject = new Object();

    private NewsDataProvider() {
    }

    public static NewsDataProvider getInstance() {
        if (sInstance == null) {
            synchronized (lockObject) {
                if (sInstance == null) {
                    // if instance is null, initialize
                    sInstance = new NewsDataProvider();
                }

            }
        }
        return sInstance;
    }

    public void loadNews(final NewsCallback callback) {
        new LoadNewsTask(callback).execute();
    }

    public void loadNewsFromDB(final NewsFromDBCallback callback, final SoftReference<Context> context) {
        new LoadNewsFromDBTask(callback, context).execute();
    }

    public void loadNewsDetail(final DetailNewsCallback callback,
                               final String apiUrl) {
        new LoadNewsDetailTask(callback, apiUrl).execute();
    }

    public void loadNewsDetailFromDB(final DetailNewsFromDBCallback callback,
                                     final SoftReference<Context> context,
                                     final int newsId) {
        new LoadNewsDetailFromDBTask(callback, context, newsId).execute();
    }

    public void loadPinnedNews(final PinnedNewsCallback callback, final Context context) {
        new LoadPinnedNewsTask(callback, context).execute();
    }

    public interface NewsCallback {
        void onNewsLoaded(List<Result> newsList);
    }

    public interface PinnedNewsCallback {
        void onPinnedNewsLoaded(Result newPinnedNews);
    }

    public interface DetailNewsCallback {
        void onNewsDetailLoaded(Content singleNews);
    }

    public interface NewsFromDBCallback {
        void onNewsLoadedFromDB(List<News> newsList);
    }

    public interface DetailNewsFromDBCallback {
        void onNewsDetailLoadedFromDB(News singleNews);
    }

}
