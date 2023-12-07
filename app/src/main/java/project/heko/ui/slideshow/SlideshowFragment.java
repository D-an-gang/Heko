package project.heko.ui.slideshow;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.commonmark.node.HtmlBlock;

import io.noties.markwon.AbstractMarkwonPlugin;
import io.noties.markwon.Markwon;
import io.noties.markwon.SoftBreakAddsNewLinePlugin;
import io.noties.markwon.core.MarkwonTheme;
import io.noties.markwon.html.HtmlPlugin;
import io.noties.markwon.image.ImagesPlugin;
import project.heko.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private Markwon markwon;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        testMarkwon();
        final TextView textView = binding.textSlideshow;

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void testMarkwon() {
        markwon = Markwon.builder(requireContext()).usePlugin(new AbstractMarkwonPlugin() {
                    @Override
                    public void configureTheme(@NonNull MarkwonTheme.Builder builder) {
//                        builder.codeTextColor(Color.BLACK).codeBackgroundColor(Color.GREEN);
                    }
                })
                .usePlugin(SoftBreakAddsNewLinePlugin.create())
                .usePlugin(ImagesPlugin.create())
                .usePlugin(HtmlPlugin.create())
                .build();
        SlideshowViewModel slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), s -> {
            markwon.setMarkdown(binding.textSlideshow, s);
        });
        slideshowViewModel.getText().setValue("_We also support some extra syntax for setting the width, height and alignment of images. You can provide pixels (`200`/`200px`), \n or a percentage (`50%`), for the width/height. The alignment can be either `left` or `right`, with images being centered by default. These settings are all optional._\n" +
//                    "\uFEFF\n" +
                "![](https://assets.digitalocean.com/public/mascot.png){ width=200 height=131 align=left }\n" +
//                    "\uFEFF\n" +
                "Use horizontal rules to break up long sections:");
    }
}