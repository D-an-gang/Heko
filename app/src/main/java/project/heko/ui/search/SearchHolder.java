package project.heko.ui.search;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import project.heko.R;

public class SearchHolder extends RecyclerView.ViewHolder {
    public final TextView title;
    public final TextView author;

    public SearchHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.search_title);
        author = itemView.findViewById(R.id.search_author);
    }
}
