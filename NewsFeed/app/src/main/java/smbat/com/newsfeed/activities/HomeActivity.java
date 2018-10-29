package smbat.com.newsfeed.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smbat.com.newsfeed.R;
import smbat.com.newsfeed.adapters.NewsAdapter;
import smbat.com.newsfeed.api.models.Result;
import smbat.com.newsfeed.database.entities.News;
import smbat.com.newsfeed.providers.NewsDataProvider;
import smbat.com.newsfeed.utils.Utils;

public class HomeActivity extends AppCompatActivity implements NewsDataProvider.NewsCallback,
        NewsDataProvider.PinnedNewsCallback, NewsDataProvider.NewsFromDBCallback {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.pin_items_list)
    RecyclerView pinnedNewsRecyclerView;
    @BindView(R.id.news_items_list)
    RecyclerView newsRecyclerView;

    private boolean isInGridMode = false;
    private NewsAdapter newsListAdapter;
    private NewsAdapter pinnedNewsAdapter;
    private List<Result> pinnedList = new ArrayList<>();
    private NewsDataProvider dataProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initializeDataProvider();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        initializeSearchView(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_view_list) {
            if (isInGridMode) {
                newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                item.setIcon(R.drawable.ic_list);
                isInGridMode = false;
                return true;
            }
            newsRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                    StaggeredGridLayoutManager.VERTICAL));
            item.setIcon(R.drawable.ic_grid);
            isInGridMode = true;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNewsLoaded(final List<Result> newsList) {
        initializeNewsListView(newsList);
        initializePinnedNewsListView();
        pinnedNewsRecyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onNewsLoadedFromDB(List<News> newsList) {
        initializeNewsListViewFromDB(newsList);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.isNetworkAvailable(this)) {
            pinnedList.clear();
            dataProvider.loadPinnedNews(this, this);
        }
    }

    @Override
    public void onPinnedNewsLoaded(Result newPinnedNews) {
        pinnedList.add(newPinnedNews);
        if (null != pinnedNewsAdapter) {
            pinnedNewsAdapter.notifyDataSetChanged();
        }
    }

    /* Helper Methods */

    private void initializeDataProvider() {
        dataProvider = NewsDataProvider.getInstance();
        if (Utils.isNetworkAvailable(this)) {
            dataProvider.loadNews(this);
            dataProvider.loadPinnedNews(this, this);
            return;
        }
        dataProvider.loadNewsFromDB(this, this);
    }

    private void initializeNewsListView(final List<Result> newsList) {
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsRecyclerView.setHasFixedSize(true);
        newsListAdapter = new NewsAdapter(this, newsList);
        newsRecyclerView.setAdapter(newsListAdapter);
    }

    private void initializePinnedNewsListView() {
        pinnedNewsRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        pinnedNewsRecyclerView.setHasFixedSize(true);
        pinnedNewsAdapter = new NewsAdapter(this, pinnedList, true);
        pinnedNewsRecyclerView.setAdapter(pinnedNewsAdapter);
    }

    private void initializeNewsListViewFromDB(final List<News> newsList) {
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsRecyclerView.setHasFixedSize(true);
        final List<Result> results = new ArrayList<>();
        for (final News savedNews : newsList) {
            final Result result = new Result();
            result.setWebTitle(savedNews.getNewsTitle());
            result.setSectionName(savedNews.getNewsCategory());
            result.setDescription(savedNews.getNewsDescription());
            result.setImageBytes(savedNews.getNewsImage());
            results.add(result);
        }
        newsListAdapter = new NewsAdapter(this, results);
        newsRecyclerView.setAdapter(newsListAdapter);
    }

    private void initializeSearchView(Menu menu) {
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
        }
        searchView.setMaxWidth(Integer.MAX_VALUE);
        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                newsListAdapter.getFilter().filter(query);
                pinnedNewsAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                newsListAdapter.getFilter().filter(query);
                pinnedNewsAdapter.getFilter().filter(query);
                return false;
            }
        });
    }
}
