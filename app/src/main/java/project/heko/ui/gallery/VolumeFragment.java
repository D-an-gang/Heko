package project.heko.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.TreeMap;

import project.heko.R;
import project.heko.databinding.FragmentVolumeBinding;
import project.heko.models.Chapter;
import project.heko.models.Volume;

public class VolumeFragment extends Fragment {
    private ExpandListViewAdapter expandListViewAdapter;
    private ArrayList<Volume> mListGroup;
    private TreeMap<Volume,ArrayList<Chapter>> mListItem;
    private FragmentVolumeBinding binding;
    private String id;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVolumeBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments() != null) {
            id = String.valueOf(getArguments().getString("id"));
            Log.i("dona", id);
        }
        showVolume();
    }

    private void showVolume(){
    FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("books")
                .document(id).get().addOnCompleteListener(task -> {
        if (task.isSuccessful()){
            task.getResult().getReference().collection("volume").orderBy("create_at", Query.Direction.ASCENDING)
                    .get().addOnCompleteListener(y ->{
                        if (y.isSuccessful()) {
                            TreeMap<Volume,ArrayList<Chapter>> listMap = new TreeMap<>(new AccordingMarks());
                            for (DocumentSnapshot v : y.getResult()) {
                                Volume volume ;
                                volume = v.toObject(Volume.class);
                                assert volume != null;
                                Log.i("Volume", volume.getId());
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
                            binding.listVolume.setOnChildClickListener((parent, v, groupPosition, childPosition, idc) -> {
                                Bundle payload = new Bundle();
                                payload.putString("book", id);
                                payload.putString("id", Objects.requireNonNull(mListItem.get(mListGroup.get(groupPosition))).get(childPosition).getId());
                                payload.putString("vol", mListGroup.get(groupPosition).getId());
                                Log.i("Data", id +" "+ Objects.requireNonNull(mListItem.get(mListGroup.get(groupPosition))).get(childPosition).getId() +" "+ mListGroup.get(groupPosition).getId());
                                Navigation.findNavController(requireView()).navigate(R.id.action_nav_gallery_to_nav_slideshow,payload);
                                return true;
                            });
                        }
                    });
        }
    });
}
    static class AccordingMarks implements Comparator<Volume> {
        public int compare(Volume s1, Volume s2) {
            return s1.getTitle().compareTo(s2.getTitle());
        }
    }

}