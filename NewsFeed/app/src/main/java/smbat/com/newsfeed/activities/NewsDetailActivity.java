package smbat.com.newsfeed.activities;

import android.annotation.SuppressLint;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import smbat.com.newsfeed.R;
import smbat.com.newsfeed.adapters.NewsAdapter;
import smbat.com.newsfeed.api.models.Content;
import smbat.com.newsfeed.providers.NewsDataProvider;
import smbat.com.newsfeed.utils.Utils;

public class NewsDetailActivity extends AppCompatActivity implements
        NewsDataProvider.DetailNewsCallback {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.news_image)
    ImageView newsImage;
    @BindView(R.id.news_title)
    TextView newsTitle;
    @BindView(R.id.news_description)
    TextView newsDescription;
    @BindView(R.id.news_web_content)
    WebView newsWebContent;
    @BindView(R.id.save_news)
    FloatingActionButton saveNewsButton;
    @BindView(R.id.pin_news)
    FloatingActionButton pinNewsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        initializeToolbar();
        initializeDataProvider();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNewsDetailLoaded(Content singleNews) {
        initializeUI(singleNews);
    }

    /* Helper Methods */

    private void initializeDataProvider() {
        final String apiUrl = getIntent().getStringExtra(NewsAdapter.CURRENT_ITEM_API_URL);
        final NewsDataProvider dataProvider = NewsDataProvider.getInstance();
        dataProvider.loadNewsDetail(this, this, apiUrl);
    }

    private void initializeToolbar() {
        toolbar.setTitle(null);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initializeUI(final Content singleNews) {
        Picasso.get()
                .load(singleNews.getFields().getThumbnail())
                .placeholder(R.drawable.news_image_placeholder)
                .into(newsImage);
        newsTitle.setText(singleNews.getWebTitle());
        newsDescription.setText(singleNews.getBlocks().getBody().get(0).getBodyTextSummary());
        newsWebContent.getSettings().setJavaScriptEnabled(true);
        newsWebContent.loadUrl(singleNews.getWebUrl());
        handleButtonsClick(singleNews);
    }

    private void handleButtonsClick(final Content singleNews) {
        saveNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Utils.saveNewsInDB(singleNews, NewsDetailActivity.this);
                    Toast.makeText(NewsDetailActivity.this,
                            R.string.news_saved_message, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Log.d("Failed To Save News", e.getLocalizedMessage());
                    Toast.makeText(NewsDetailActivity.this,
                            R.string.failed_to_save, Toast.LENGTH_SHORT).show();
                }
            }
        });
        pinNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NewsDetailActivity.this,
                        R.string.news_pinned_message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
