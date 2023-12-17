package project.heko.ui.bookShelf;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import project.heko.R;
import project.heko.dto.BookShelfDto;

public class BookShelfAdapter extends FirestoreRecyclerAdapter<BookShelfDto, BookShelfAdapter.BookShelfHolder> {
    private final NavController controller;
    public BookShelfAdapter(@NonNull FirestoreRecyclerOptions<BookShelfDto> options,NavController controller) {
        super(options);
        this.controller = controller;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onBindViewHolder(@NonNull BookShelfHolder holder, int position, @NonNull BookShelfDto model) {
        String imgurl = "https://firebasestorage.googleapis.com/v0/b/heko-a6b88.appspot.com/o/banners%2Fnocover.jpg?alt=media&token=2af4d3a7-1729-45e3-8291-bd1f7cd04684";
        if(model.getCover() != null && !model.getCover().equals("")){
            imgurl = model.getCover();
        }
        Picasso.get().load(imgurl)
                .error(R.drawable.nocover)
                .into(holder.cover);
        holder.txtTitle.setText(model.getTitle());
        if(model.getUnread()!=0){
            holder.unread.setText(String.valueOf(model.getUnread()));
        }
        else {
            holder.unread.setVisibility(View.GONE);
            holder.btn.setEnabled(false);
            holder.btn.setText("Đã đọc");
            holder.btn.setBackgroundColor(R.color.secondary);
        }

        holder.itemView.setOnClickListener(v -> {
            Log.d("POSITION" , String.valueOf(position));
            Log.i("Id", model.getId());
            Bundle payload = new Bundle();
            payload.putString("id", model.getId());
            controller.navigate(R.id.book_shelf_to_book, payload);
        });
        holder.btn.setOnClickListener(t -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("bookShelf").document(model.getDocId()).update("unread",0)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            holder.unread.setVisibility(View.GONE);
                            holder.btn.setEnabled(false);
                            holder.btn.setText("Đã đọc");
                            holder.btn.setBackgroundColor(R.color.secondary);
                        }
                    });
        });
    }

    @NonNull
    @Override
    public BookShelfHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookshelf, parent,false);
        return new BookShelfHolder(view);
    }


    /** @noinspection FieldMayBeFinal*/
    public class BookShelfHolder extends RecyclerView.ViewHolder{
        private ImageView cover;
        private TextView txtTitle;
        private Chip unread;
        private MaterialButton btn;
        public BookShelfHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.img_book);
            txtTitle = itemView.findViewById(R.id.title_book);
            unread = itemView.findViewById(R.id.unread);
            btn = itemView.findViewById(R.id.read);
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDataChanged() {
        super.onDataChanged();
        //del after dev
        notifyDataSetChanged();
    }

}
