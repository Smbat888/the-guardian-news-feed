package smbat.com.newsfeed.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import smbat.com.newsfeed.api.models.News;
import smbat.com.newsfeed.api.models.SingleNews;

public interface NewsClient {

    @GET("search")
    Call<News> getBaseJson(
            // API key should always be constant
            @Query("api-key") String apiKey,
            // For getting thumbnail image
            @Query("show-fields") String fields);

    @GET
    Call<SingleNews> getSingleNewsJson(
            @Url String url,
            // API key should always be constant
            @Query("api-key") String apiKey,
            // For getting all blocks
            @Query("show-blocks") String showBlock,
            // For getting thumbnail image
            @Query("show-fields") String fields);
}
