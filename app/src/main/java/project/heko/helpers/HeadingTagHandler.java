package project.heko.helpers;

import android.graphics.Typeface;
import android.text.style.StyleSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.Collection;

import io.noties.markwon.MarkwonConfiguration;
import io.noties.markwon.RenderProps;
import io.noties.markwon.html.CssInlineStyleParser;
import io.noties.markwon.html.CssProperty;
import io.noties.markwon.html.HtmlTag;
import io.noties.markwon.html.tag.SimpleTagHandler;

public class HeadingTagHandler extends SimpleTagHandler {
    private final CssInlineStyleParser styleParser = CssInlineStyleParser.create();

    @Nullable
    @Override
    public Object getSpans(@NonNull MarkwonConfiguration configuration, @NonNull RenderProps renderProps, @NonNull HtmlTag tag) {
        final String style = tag.attributes().get("style");
        if (style == null) {
            return null;
        }
        String border = null;
        for (CssProperty property : styleParser.parse(style)) {
            if ("border".equals(property.key())) {
                border = property.value();
                break;
            }
        }
        if (border == null) {
            return null;
        }
        return new StyleSpan(Typeface.BOLD);
    }

    @NonNull
    @Override
    public Collection<String> supportedTags() {
        return Arrays.asList("h1", "h2", "h3", "h4", "h5", "h6");
    }
}
