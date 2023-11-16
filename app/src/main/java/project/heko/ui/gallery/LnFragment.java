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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import project.heko.databinding.LightnovelDetailBinding;
import project.heko.models.Book;
import project.heko.models.Chapter;
import project.heko.models.Volume;

public class LnFragment extends Fragment {

    private LightnovelDetailBinding binding;
    private LnViewModel lnViewModel;
    private ExpandListViewAdapter expandListViewAdapter;
    private List<Volume> mListV;
    private Map<Volume,List<Chapter>> mListI;
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
            if(!book.getGenre().equals("")){
                String[] genres = book.getGenre().split(";");
                for (String i : genres) {
                    Chip chip = new Chip(this.getContext());
                    chip.setText(i);
                    Log.i("array", i);
                    binding.groupGenre.addView(chip);
                }
            }

            binding.txtLastUpdate.setText(formatDate(book.getLast_update().toDate()));
            binding.txtAuthor.setText(String.format("Tác giả: %s", book.getAuthor()));
        });
        Volume volume = lnViewModel.getVolume().getValue();
        expandListViewAdapter = new ExpandListViewAdapter(mListV,mListI);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void getBook(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("books").document("O5GqxRrBXPmNw15lyfQG");
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d("B", "DocumentSnapshot data: " + document.getData());
                    DocumentSnapshot snap = task.getResult();
                    Book book = snap.toObject(Book.class);
                    lnViewModel.getBook().setValue(book);
                    assert book != null;
                    String[] genres = book.getGenre().split(";");
                    Log.i("Test",genres[0]);
                } else {
                    Log.d("B", "No such document");
                }
            } else {
                Log.d("B", "get failed with ", task.getException());
            }
        });
        DocumentReference docVol = db.collection("volume").document("isPZprKfDXBohKi6Lnw0");
        docVol.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d("B", "DocumentSnapshot data: " + document.getData());
                    DocumentSnapshot snap = task.getResult();
                    Volume volume = snap.toObject(Volume.class);
                    lnViewModel.getVolume().setValue(volume);
                } else {
                    Log.d("B", "No such document");
                }
            } else {
                Log.d("B", "get failed with ", task.getException());
            }
        });
        DocumentReference docChap = db.collection("chapter").document("S3Hg2N5ZwN5aj01");
        docChap.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d("B", "DocumentSnapshot data: " + document.getData());
                    DocumentSnapshot snap = task.getResult();
                    Chapter chap = snap.toObject(Chapter.class);
                    lnViewModel.getChapter().setValue(chap);
                } else {
                    Log.d("B", "No such document");
                }
            } else {
                Log.d("B", "get failed with ", task.getException());
            }
        });
    }
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
            if(differenceMonth == 0){
                dif = differenceWeek + "tuần";
                if(differenceWeek == 0){
                    dif = differenceDays +"ngày";
                    if (differenceDays == 0){
                        dif = differenceHours + "giờ";
                        if(differenceHours == 0){
                            dif = differenceMinutes +"phút";
                        }
                    }
                }
            }
        }
        return "Cập nhật lần cuối:"+dif;
    }
}