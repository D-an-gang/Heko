package project.heko.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import project.heko.databinding.LightnovelDetailBinding;
import project.heko.models.Book;
import project.heko.models.Chapter;
import project.heko.models.Volume;

public class LnFragment extends Fragment {

    private LightnovelDetailBinding binding;
    private LnViewModel lnViewModel;
    private ExpandListViewAdapter expandListViewAdapter;
    private ArrayList<Volume> mListGroup;
    private Map<Volume,ArrayList<Chapter>> mListItem;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        lnViewModel = new ViewModelProvider(this).get(LnViewModel.class);
        binding = LightnovelDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBook();
        lnViewModel.getBook().observe(getViewLifecycleOwner(), book -> {
            binding.lnTitle.setText(book.getTitle());
            binding.txtLastUpdate.setText(formatDate(book.getLast_update().toDate()));
            binding.txtAuthor.setText(String.format("Tác giả: %s", book.getAuthor()));
            if (!book.getBook_cover().equals("")){
                Picasso.get().load(book.getBook_cover()).into(binding.lnCover);
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void getBook(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("books")
                .document("O5GqxRrBXPmNw15lyfQG").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("B", "DocumentSnapshot data: " + document.getData());
                            DocumentSnapshot snap = task.getResult();
                            Book book = snap.toObject(Book.class);
                            lnViewModel.getBook().setValue(book);
                            assert book != null;
                            String[] genres = book.getGenre().split(";");
                            for (String i : genres) {
                                Chip chip = new Chip(this.getContext());
                                chip.setText(i);
                                binding.groupGenre.addView(chip);
                            }
                        } else {
                            Log.d("B", "No such document");
                        }
                    task.getResult().getReference().collection("volume").orderBy("title", Query.Direction.ASCENDING)
                            .get().addOnCompleteListener(y ->{
                                if (y.isSuccessful()) {
                                    Map<Volume,ArrayList<Chapter>> listMap = new HashMap<>();
                                    for (DocumentSnapshot v : y.getResult()) {
                                        Volume volume ;
                                        volume = v.toObject(Volume.class);
                                        assert volume != null;
                                        Log.i("Volume", volume.getTitle());
                                        ArrayList<Chapter> chapters = new ArrayList<>();
                                        v.getReference().collection("chapters").orderBy("create_at",Query.Direction.ASCENDING).get().addOnCompleteListener(i -> {
                                            if (i.isSuccessful()){
                                                for (DocumentSnapshot z: i.getResult()) {
                                                    Chapter chapter;
                                                    chapter = z.toObject(Chapter.class);
                                                    assert chapter != null;
                                                    Log.i("Chapter", chapter.getTitle());
                                                    chapters.add(chapter);
                                                }
                                            }
                                        });
                                        listMap.put(volume, chapters);
                                    }
                                    mListItem = listMap;
                                    mListGroup = new ArrayList<>(mListItem.keySet());
                                    expandListViewAdapter = new ExpandListViewAdapter(mListGroup,mListItem);
                                    binding.listVolume.setAdapter(expandListViewAdapter);
                                    binding.listVolume.setGroupIndicator(null);
                                }
                            });
                    }
                });
    }
    @NonNull
    private String formatDate(Date date){
        Date currentDate = new Date();
        long difference = currentDate.getTime() - date.getTime();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(difference);
        int differenceYears = c.get(Calendar.YEAR)-1970;
        int differenceHours = c.get(Calendar.HOUR);
        int differenceMonth = c.get(Calendar.MONTH);
        int differenceDays = c.get(Calendar.DAY_OF_MONTH)-1;
        int differenceWeek = (c.get(Calendar.DAY_OF_MONTH)-1)/7;
        int differenceMinutes = c.get(Calendar.MINUTE);// ** if you use this, change the mDay to (c.get(Calendar.DAY_OF_MONTH)-1)%7
        String dif = differenceYears + "năm";
        if(differenceYears == 0){
            dif = differenceMonth + " tháng";
            if(differenceMonth == 0){
                dif = differenceWeek + " tuần";
                if(differenceWeek == 0){
                    dif = differenceDays +" ngày";
                    if (differenceDays == 0){
                        dif = differenceHours + " giờ";
                        if(differenceHours == 0){
                            dif = differenceMinutes +" phút";
                        }
                    }
                }
            }
        }
        return "Lần cuối "+dif;
    }
}