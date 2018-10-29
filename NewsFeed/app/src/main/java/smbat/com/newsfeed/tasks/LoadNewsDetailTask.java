package smbat.com.newsfeed.tasks;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smbat.com.newsfeed.api.NewsClient;
import smbat.com.newsfeed.api.ServiceGenerator;
import smbat.com.newsfeed.api.models.Content;
import smbat.com.newsfeed.api.models.Result;
import smbat.com.newsfeed.api.models.SingleNews;
import smbat.com.newsfeed.constants.Constants;
import smbat.com.newsfeed.providers.NewsDataProvider;

public class LoadNewsDetailTask extends AsyncTask<Void, Void, Result> {

    private final NewsDataProvider.DetailNewsCallback callback;
    private final String singleNewsAPIUrl;
    private Content newsContent;

    public LoadNewsDetailTask(final NewsDataProvider.DetailNewsCallback callback,
                              final String singleNewsAPIUrl) {

        this.callback = callback;
        this.singleNewsAPIUrl = singleNewsAPIUrl;
    }

    @Override
    protected Result doInBackground(Void... voids) {
        final NewsClient client =
                ServiceGenerator.createService(NewsClient.class);
        final Call<SingleNews> call = client.getSingleNewsJson(singleNewsAPIUrl, Constants.API_KEY,
                Constants.SHOW_BLOCKS, Constants.SHOW_FIELDS_THUMBNAIL);
        call.enqueue(new Callback<SingleNews>() {
            @Override
            public void onResponse(@NonNull Call<SingleNews> call, @NonNull Response<SingleNews> response) {
                final SingleNews body = response.body();
                if (null == body) {
                    return;
                }
                newsContent = body.getResponse().getContent();
                callback.onNewsDetailLoaded(newsContent);
            }

            @Override
            public void onFailure(@NonNull Call<SingleNews> call, @NonNull Throwable t) {
                Log.e("Fail to get results: ", t.getMessage());
            }
        });
        return null;
    }
}
