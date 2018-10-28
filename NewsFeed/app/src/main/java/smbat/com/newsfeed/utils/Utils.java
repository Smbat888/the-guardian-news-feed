package smbat.com.newsfeed.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import smbat.com.newsfeed.api.models.Content;
import smbat.com.newsfeed.database.AppDataBase;
import smbat.com.newsfeed.database.entities.News;

public class Utils {

    private static final int TEXT_SUMMARY_POSITION_IN_BODY = 0;

    private Utils() {
        throw new UnsupportedOperationException();
    }

    public static void saveNewsInDB(final Content newsContent, final Context context) throws IOException {
        final AppDataBase appDataBase = AppDataBase.getAppDatabase(context);
        final News newsForSave = new News();
        newsForSave.setNewsTitle(newsContent.getWebTitle());
        newsForSave.setNewsCategory(newsContent.getSectionName());
        newsForSave.setNewsDescription(newsContent.getBlocks().getBody()
                .get(TEXT_SUMMARY_POSITION_IN_BODY).getBodyTextSummary());
        Utils.getImageBytes(newsForSave, newsContent.getFields().getThumbnail());
        appDataBase.newsDao().insert(newsForSave);
    }

    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivity =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null == connectivity) {
            return false;
        } else {
            final NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (null != info) {
                for (final NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static void getImageBytes(final News newsForSave, final String thumbnailUrl) throws IOException {
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(thumbnailUrl)
                .build();
        final CallbackFuture future = new CallbackFuture();
        client.newCall(request).enqueue(future);
        Response response = null;
        try {
            response = future.get();
            final ResponseBody responseBody = response.body();
            if (null == responseBody) {
                return;
            }
            final byte[] bytes = responseBody.bytes();
            newsForSave.setNewsImage(bytes);
        } catch (Exception e) {
            Log.d("GETImageBytes", e.getLocalizedMessage());
        }
    }

    public static Bitmap getBitmapFromBytes(final byte[] imageBytes) {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    static class CallbackFuture extends CompletableFuture<Response> implements Callback {
        public void onResponse(@NonNull Call call, @NonNull Response response) {
            super.complete(response);

        }

        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            super.completeExceptionally(e);
            Log.d("GETImageBytes", e.getLocalizedMessage());
        }
    }
}
