package project.heko.ui.search;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import project.heko.R;
import project.heko.models.SearchResult;

public class SearchAdapter extends FirestoreRecyclerAdapter<SearchResult, SearchHolder> {
    public SearchAdapter(@NonNull FirestoreRecyclerOptions<SearchResult> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SearchHolder holder, int position, @NonNull SearchResult model) {
        // Bind the Chat object to the ChatHolder
        String[] temp = model.getDisplay().split("-");
        String title = temp[0].trim();
        String author = temp[1].trim();
        holder.title.setText(title);
        holder.author.setText(author);
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_list_item, parent, false);
        return new SearchHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDataChanged() {
        super.onDataChanged();
        //del after dev
        notifyDataSetChanged();
    }
}
