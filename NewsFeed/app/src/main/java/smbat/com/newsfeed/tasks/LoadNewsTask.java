package smbat.com.newsfeed.tasks;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smbat.com.newsfeed.api.NewsClient;
import smbat.com.newsfeed.api.ServiceGenerator;
import smbat.com.newsfeed.api.models.News;
import smbat.com.newsfeed.api.models.Result;
import smbat.com.newsfeed.constants.Constants;
import smbat.com.newsfeed.providers.NewsDataProvider;

public class LoadNewsTask extends AsyncTask<Void, Void, List<Result>>  {

    private final NewsDataProvider.NewsCallback callback;
    private List<Result> results;

    public LoadNewsTask(final NewsDataProvider.NewsCallback callback) {
        this.callback = callback;
    }

    @Override
    protected List<Result> doInBackground(Void... voids) {
        // Create a Retrofit client with the ServiceGenerator class
        final NewsClient client = ServiceGenerator.createService(NewsClient.class);
        final Call<News> call = client.getBaseJson(Constants.API_KEY, Constants.SHOW_FIELDS_THUMBNAIL);
        // Call the endpoint and respond to failure and success events
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(@NonNull Call<News> call, @NonNull Response<News> response) {
                // Parse the response to match the List of JSONArray objects
                final News body = response.body();
                if (null == body) {
                    return;
                }
                results = body.getResponse().getResults();
                callback.onNewsLoaded(results);
            }

            @Override
            public void onFailure(@NonNull Call<News> call, @NonNull Throwable t) {
                Log.e("Fail to get results: ", t.getMessage());
            }
        });
        return results;
    }
}
