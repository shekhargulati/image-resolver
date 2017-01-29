package imageresolver;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HtmlDoc {

    private final String url;
    private final String html;
    private final Map<String, List<String>> responseHeaders;

    public HtmlDoc(String url, String html, Map<String, List<String>> responseHeaders) {
        this.html = html;
        this.url = url;
        this.responseHeaders = responseHeaders;
    }

    public HtmlDoc(String url) {
        this(url, null, Collections.emptyMap());
    }

    public String url() {
        return url;
    }

    public Optional<String> html() {
        return Optional.ofNullable(html);
    }

    public Map<String, List<String>> responseHeaders() {
        return Collections.unmodifiableMap(responseHeaders);
    }
}
