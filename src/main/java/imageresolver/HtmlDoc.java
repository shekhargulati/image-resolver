package imageresolver;

import java.util.Optional;

public class HtmlDoc {

    public final Optional<String> html;
    public final String url;

    public HtmlDoc(String url, String html) {
        this.html = Optional.ofNullable(html);
        this.url = url;
    }

    public HtmlDoc(String url) {
        this.html = Optional.empty();
        this.url = url;
    }
}
