package project.heko.ui.slideshow;

import android.os.Bundle;
import android.text.Layout;
import android.text.style.AlignmentSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.firebase.firestore.FirebaseFirestore;

import org.commonmark.node.Image;

import io.noties.markwon.AbstractMarkwonPlugin;
import io.noties.markwon.Markwon;
import io.noties.markwon.MarkwonSpansFactory;
import io.noties.markwon.SoftBreakAddsNewLinePlugin;
import io.noties.markwon.html.HtmlPlugin;
import io.noties.markwon.image.ImagesPlugin;
import project.heko.MainActivity;
import project.heko.R;
import project.heko.databinding.FragmentSlideshowBinding;
import project.heko.helpers.HeadingTagHandler;
import project.heko.helpers.UItools;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private Markwon markwon;
    private SlideshowViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initMarkwon();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO da link uncomment be duoi va xoa mockTest
//        getData();
        mockTesting();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initMarkwon() {
        markwon = Markwon.builder(requireContext()).usePlugin(SoftBreakAddsNewLinePlugin.create()).usePlugin(ImagesPlugin.create()).usePlugin(HtmlPlugin.create(plugin -> plugin.addHandler(new HeadingTagHandler())))
                .usePlugin(new AbstractMarkwonPlugin() {
                    @Override
                    public void configureSpansFactory(@NonNull MarkwonSpansFactory.Builder builder) {
                        builder.prependFactory(Image.class, (configuration, props) -> new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER));
                    }
                })
                .build();
        slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), s -> markwon.setMarkdown(binding.textSlideshow, s));
    }

    private void getData() {
        if (getArguments() == null || getArguments().getString("id") == null || getArguments().getString("book") == null || getArguments().getString("vol") == null) {
            cancel();
        } else {
            String id = getArguments().getString("id");
            String book_id = getArguments().getString("book");
            String vol_id = getArguments().getString("vol");
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            assert book_id != null;
            assert id != null;
            assert vol_id != null;
            db.collection("books").document(book_id).collection("volume").document(vol_id).collection("chapters").document(id).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists() && task.getResult().getString("content") != null && task.getResult().getString("title") != null) {
                    try {
                        //noinspection DataFlowIssue
                        ((MainActivity) getActivity()).getSupportActionBar().setTitle(task.getResult().getString("title"));
                    } catch (NullPointerException ex) {
                        UItools.toast(requireActivity(), getResources().getString(R.string.error_norm));
                    }
                    slideshowViewModel.getText().setValue(task.getResult().getString("content"));
                } else {
                    cancel();
                }
            });
        }

    }

    private void cancel() {
        UItools.toast(requireActivity(), getString(R.string.error_load_book));
        Navigation.findNavController(requireView()).popBackStack(R.id.nav_home, false);
    }

    private void mockTesting() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("books").document("O5GqxRrBXPmNw15lyfQG").collection("volume").document("QQs7Uzt204ECiUvqYIOz").collection("chapters").document("13GKHwMpa6NUGhzYYmdT").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists() && task.getResult().getString("content") != null && task.getResult().getTimestamp("create_at") != null && task.getResult().getString("title") != null) {
                try {
                    //noinspection DataFlowIssue
                    ((MainActivity) getActivity()).getSupportActionBar().setTitle(task.getResult().getString("title"));
                } catch (NullPointerException ex) {
                    UItools.toast(requireActivity(), getResources().getString(R.string.error_norm));
                }
                slideshowViewModel.getText().setValue(task.getResult().getString("content"));
            } else {
                cancel();
            }
        });
    }
}