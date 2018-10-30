package smbat.com.newsfeed.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smbat.com.newsfeed.api.NewsClient;
import smbat.com.newsfeed.api.ServiceGenerator;
import smbat.com.newsfeed.api.models.Content;
import smbat.com.newsfeed.api.models.Fields;
import smbat.com.newsfeed.api.models.Result;
import smbat.com.newsfeed.api.models.SingleNews;
import smbat.com.newsfeed.constants.Constants;
import smbat.com.newsfeed.providers.NewsDataProvider;

import static smbat.com.newsfeed.activities.NewsDetailActivity.PINNED_NEWS_SHARED_PREF_KEY_FILE;

/**
 * Task to load pinned news by calling API requests separately for each news stored in
 * shared preferences (pinned).
 */
public class LoadPinnedNewsTask extends AsyncTask<Void, Void, List<Result>> {

    private final NewsDataProvider.PinnedNewsCallback callback;
    private final SharedPreferences sharedPreferences;
    private final NewsClient client;
    private final List<Result> results = new ArrayList<>();

    public LoadPinnedNewsTask(final NewsDataProvider.PinnedNewsCallback callback,
                              final Context context) {
        this.callback = callback;
        sharedPreferences = context.getSharedPreferences(PINNED_NEWS_SHARED_PREF_KEY_FILE,
                Context.MODE_PRIVATE);
        client = ServiceGenerator.createService(NewsClient.class);
    }

    @Override
    protected List<Result> doInBackground(Void... voids) {
        final Map<String, ?> keys = sharedPreferences.getAll();
        for (final Map.Entry<String, ?> entry : keys.entrySet()) {
            callForEachNews(entry);
        }
        return results;
    }

    /* Helper Methods */

    /**
     * Calls and makes API request for getting each pinned news stored in shared preferences.
     *
     * @param entry Entity to get specified news api url from shared preferences
     */
    private void callForEachNews(final Map.Entry<String, ?> entry) {
        final String apiUrl = entry.getValue().toString();
        final Call<SingleNews> call = client.getSingleNewsJson(apiUrl, Constants.API_KEY,
                Constants.SHOW_BLOCKS, Constants.SHOW_FIELDS_THUMBNAIL);
        call.enqueue(new Callback<SingleNews>() {
            @Override
            public void onResponse(@NonNull Call<SingleNews> call, @NonNull Response<SingleNews> response) {
                final SingleNews body = response.body();
                if (null == body) {
                    return;
                }
                final Content newsContent = body.getResponse().getContent();
                final Result result = new Result();
                result.setSectionName(newsContent.getSectionName());
                result.setWebTitle(newsContent.getWebTitle());
                final Fields fields = new Fields();
                fields.setThumbnail(newsContent.getFields().getThumbnail());
                result.setFields(fields);
                result.setApiUrl(newsContent.getApiUrl());
                results.add(result);
                callback.onPinnedNewsLoaded(result);
            }

            @Override
            public void onFailure(@NonNull Call<SingleNews> call, @NonNull Throwable t) {
                Log.e("Fail to get results: ", t.getMessage());
            }
        });
    }
}
