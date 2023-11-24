package project.heko.ui.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import project.heko.R;
import project.heko.dto.HomePreviewDto;

public class HomePageRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public List<HomePreviewDto> mItemList;

    public HomePageRecycleAdapter(List<HomePreviewDto> itemList) {
        super();
        mItemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_row, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            Log.i("XX", "Adapter Loading");
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position).getId().equals(HomePreviewDto.NULL_KEY) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView cover;
        TextView latest_chap;
        TextView latest_vol;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.rv_book_title);
            cover = itemView.findViewById(R.id.rv_book_image);
            latest_chap = itemView.findViewById(R.id.rv_latest_chap);
            latest_vol = itemView.findViewById(R.id.rv_latest_vol);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.rv_progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed
        viewHolder.progressBar.setIndeterminate(true);
        viewHolder.progressBar.setVisibility(View.VISIBLE);
    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {
        HomePreviewDto item = mItemList.get(position);
        viewHolder.title.setText(item.getTitle());
        if (!item.getLatest_vol().equals("") || item.getLatest_vol() == null)
            viewHolder.latest_vol.setText(item.getLatest_vol());
        if (!item.getLatest_chap().equals("") || item.getLatest_chap() == null)
            viewHolder.latest_chap.setText(item.getLatest_chap());
        if (!item.getBook_cover().equals("") || item.getBook_cover() == null)
            Picasso.get().load(item.getBook_cover())
                    .error(R.drawable.nocover)
                    .into(viewHolder.cover);
    }


}
