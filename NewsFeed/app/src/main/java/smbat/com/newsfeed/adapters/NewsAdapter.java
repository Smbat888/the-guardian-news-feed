package smbat.com.newsfeed.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smbat.com.newsfeed.R;
import smbat.com.newsfeed.activities.HomeActivity;
import smbat.com.newsfeed.activities.NewsDetailActivity;
import smbat.com.newsfeed.api.models.Result;
import smbat.com.newsfeed.utils.Utils;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> implements Filterable {

    public static final String CURRENT_ITEM_API_URL = "smbat.com.newsfeed.adapters.CURRENT_ITEM_API_URL";

    private static final String IMAGE_TRANSITION_NAME = "newsImage";
    private static final String TEXT_TRANSITION_NAME = "newsTitle";

    private final Context context;
    private final List<Result> newsList;
    private List<Result> newsListFiltered;

    public NewsAdapter(final Context context, final List<Result> newsList) {
        this.context = context;
        this.newsList = newsList;
        newsListFiltered = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        final Result news = newsListFiltered.get(position);
        holder.newsTitle.setText(news.getWebTitle());
        final String category = news.getSectionName();
        holder.newsCategory.setText(category);
        bindImage(holder, news);

        holder.itemView.setOnClickListener(getOnItemClickListener(holder, news.getApiUrl()));
    }

    private void bindImage(final NewsViewHolder holder, final Result news) {
        if (null == news.getFields()) {
            if (null != news.getImageBytes()) {
                holder.newsImage.setImageBitmap(Utils.getBitmapFromBytes(news.getImageBytes()));
            }
            return;
        }
        final String thumbnailImage = news.getFields().getThumbnail();
        Picasso.get()
                .load(thumbnailImage)
                .placeholder(R.drawable.news_image_placeholder)
                .into(holder.newsImage);
    }

    @Override
    public int getItemCount() {
        return newsListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    newsListFiltered = newsList;
                } else {
                    final List<Result> filteredList = new ArrayList<>();
                    for (final Result row : newsList) {
                        if (row.getWebTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    newsListFiltered = filteredList;
                }
                final FilterResults filterResults = new FilterResults();
                filterResults.values = newsListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                newsListFiltered = (ArrayList<Result>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.news_title)
        TextView newsTitle;
        @BindView(R.id.news_category)
        TextView newsCategory;
        @BindView(R.id.news_image)
        ImageView newsImage;

        NewsViewHolder(final LayoutInflater inflater, final ViewGroup parent) {
            super(inflater.inflate(R.layout.news_item, parent, false));
            ButterKnife.bind(this, itemView);
        }

    }

    /* Helper Methods */

    private View.OnClickListener getOnItemClickListener(final NewsViewHolder holder,
                                                        final String apiUrl) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra(CURRENT_ITEM_API_URL, apiUrl);
                final Pair<View, String> pairImage =
                        Pair.create((View) holder.newsImage, IMAGE_TRANSITION_NAME);
                final Pair<View, String> pairTitle =
                        Pair.create((View) holder.newsTitle, TEXT_TRANSITION_NAME);
                @SuppressWarnings("unchecked") final ActivityOptionsCompat optionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                                (HomeActivity) context, pairImage, pairTitle);
                context.startActivity(intent, optionsCompat.toBundle());
            }
        };
    }
}
