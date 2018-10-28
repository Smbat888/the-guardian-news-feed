package smbat.com.newsfeed.tasks;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import smbat.com.newsfeed.api.models.Result;
import smbat.com.newsfeed.providers.NewsDataProvider;

public class LoadPinnedNewsTask extends AsyncTask<Void, Void, ArrayList<Result>> {

    private final NewsDataProvider.PinnedNewsCallback callback;

    public LoadPinnedNewsTask(final NewsDataProvider.PinnedNewsCallback callback,
                              final Context context) {
        this.callback = callback;
    }

    @Override
    protected ArrayList<Result> doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Result> news) {
        super.onPostExecute(news);
        callback.onPinnedNewsLoaded(news);
    }
}
