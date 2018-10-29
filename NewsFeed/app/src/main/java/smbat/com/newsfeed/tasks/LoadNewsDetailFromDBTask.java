package smbat.com.newsfeed.tasks;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.SoftReference;

import smbat.com.newsfeed.database.AppDataBase;
import smbat.com.newsfeed.database.entities.News;
import smbat.com.newsfeed.providers.NewsDataProvider;

public class LoadNewsDetailFromDBTask extends AsyncTask<Void, Void, News> {

    private final NewsDataProvider.DetailNewsFromDBCallback callback;
    private final SoftReference<Context> context;
    private final int newsId;

    public LoadNewsDetailFromDBTask(final NewsDataProvider.DetailNewsFromDBCallback callback,
                                    final SoftReference<Context> context, final int newsId) {
        this.callback = callback;
        this.context = context;
        this.newsId = newsId;
    }

    @Override
    protected News doInBackground(Void... voids) {
        final AppDataBase appDataBase = AppDataBase.getAppDatabase(context.get());
        return appDataBase.newsDao().loadNewsById(newsId);
    }

    @Override
    protected void onPostExecute(News news) {
        super.onPostExecute(news);
        callback.onNewsDetailLoadedFromDB(news);
    }
}
