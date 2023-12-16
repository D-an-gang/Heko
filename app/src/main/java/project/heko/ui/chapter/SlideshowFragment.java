package project.heko.ui.chapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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

import com.google.firebase.auth.FirebaseAuth;
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
    private SettingViewModel setFont;
    private boolean autoScroll;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initMarkwon();
        return root;
    }
    /**
     * @noinspection DataFlowIssue
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
        initFont();
        boolean confirm = getArguments().getString("id").equals(requireActivity().getPreferences(Context.MODE_PRIVATE).getString(getString(R.string.lr_chapter_id), ""));
        if (confirm) {
            autoScroll = true;
        }
    }

    /**
     * @noinspection DataFlowIssue
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        boolean stat = getArguments() == null || getArguments().getString("id") == null || getArguments().getString("book") == null || getArguments().getString("vol") == null;
        boolean confirm = getArguments().getString("id").equals(requireActivity().getPreferences(Context.MODE_PRIVATE).getString(getString(R.string.lr_chapter_id), ""));
        if (FirebaseAuth.getInstance().getCurrentUser() != null && !stat && confirm) {
            SharedPreferences.Editor editor = requireActivity().getPreferences(Context.MODE_PRIVATE).edit();
            editor.putInt(getString(R.string.lr_postion), binding.chapterSv.getScrollY());
            editor.apply();
            setFont.getReset().setValue(true);
        }
    }

    private void initMarkwon() {
        markwon = Markwon.builder(requireContext()).usePlugin(SoftBreakAddsNewLinePlugin.create()).usePlugin(ImagesPlugin.create()).usePlugin(HtmlPlugin.create(plugin -> plugin.addHandler(new HeadingTagHandler()))).usePlugin(new AbstractMarkwonPlugin() {
                    @Override
                    public void configureSpansFactory(@NonNull MarkwonSpansFactory.Builder builder) {
                        builder.prependFactory(Image.class, (configuration, props) -> new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER));
                    }
                })
                .build();
        slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), s -> {
            markwon.setMarkdown(binding.textSlideshow, s);
            if (autoScroll) {
                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    int saved_pos = requireActivity().getPreferences(Context.MODE_PRIVATE).getInt(getString(R.string.lr_postion), 0);
                    binding.chapterSv.post(() -> binding.chapterSv.smoothScrollTo(0, saved_pos));
                }, 500);
            }
        });
    }

    /**
     * @noinspection DataFlowIssue
     */
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
            db.collection("books").document(book_id).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String cover = documentSnapshot.getString("book_cover");
                    String title = documentSnapshot.getString("title");
                    documentSnapshot.getReference().collection("volume").document(vol_id).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            String vol_title = task.getResult().getString("title");
                            task.getResult().getReference().collection("chapters").document(id).get().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful() && task1.getResult().exists() && task1.getResult().getString("content") != null && task1.getResult().getString("title") != null) {
                                    try {
                                        String chap_title = task1.getResult().getString("title");
                                        ((MainActivity) getActivity()).getSupportActionBar().setTitle(task1.getResult().getString("title"));
                                        SharedPreferences.Editor editor = requireActivity().getPreferences(Context.MODE_PRIVATE).edit();
                                        editor.putString(getString(R.string.lr_book_id), book_id);
                                        editor.putString(getString(R.string.lr_volume_id), vol_id);
                                        editor.putString(getString(R.string.lr_chapter_id), id);
                                        editor.putString(getString(R.string.lr_d_book_title), title);
                                        editor.putString(getString(R.string.lr_d_book_cover), cover);
                                        editor.putString(getString(R.string.lr_d_chapter_title), chap_title);
                                        editor.putString(getString(R.string.lr_d_vol_title), vol_title);
                                        editor.apply();
                                    } catch (NullPointerException ex) {
                                        UItools.toast(requireActivity(), getResources().getString(R.string.error_norm));
                                    }
                                    slideshowViewModel.getText().setValue(task1.getResult().getString("content"));
                                } else {
                                    cancel();
                                }
                            });
                        } else {
                            binding = null;
                        }
                    });
                }
            });
        }

    }

    private void cancel() {
        UItools.toast(requireActivity(), getString(R.string.error_load_book));
        Navigation.findNavController(requireView()).popBackStack(R.id.nav_home, false);
    }

    private void initFont() {
        setFont = new ViewModelProvider(requireActivity()).get(SettingViewModel.class);
        setFont.getFont().observe(getViewLifecycleOwner(), fontSetting -> {
            binding.textSlideshow.setTypeface(fontSetting.font);
            binding.textSlideshow.setTextSize(fontSetting.size);
        });
    }
}