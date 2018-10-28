package smbat.com.newsfeed.tasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import java.util.Arrays;
import java.util.List;

import smbat.com.newsfeed.database.AppDataBase;
import smbat.com.newsfeed.database.entities.News;
import smbat.com.newsfeed.providers.NewsDataProvider;


public class LoadNewsFromDBTask extends AsyncTask<Void, Void, List<News>> {

    private final NewsDataProvider.NewsFromDBCallback callback;
    @SuppressLint("StaticFieldLeak")
    private final Context context;

    public LoadNewsFromDBTask(final NewsDataProvider.NewsFromDBCallback callback,
                              final Context context) {
        this.callback = callback;
        this.context = context;
    }

    @Override
    protected List<News> doInBackground(Void... voids) {
        final AppDataBase appDataBase = AppDataBase.getAppDatabase(context);
        final News[] allSavedNews = appDataBase.newsDao().loadAll();
        return Arrays.asList(allSavedNews);
    }

    @Override
    protected void onPostExecute(List<News> news) {
        super.onPostExecute(news);
        callback.onNewsLoadedFromDB(news);
    }
}
