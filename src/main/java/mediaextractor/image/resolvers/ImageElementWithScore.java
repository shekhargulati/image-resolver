package mediaextractor.image.resolvers;

import org.jsoup.nodes.Element;

public class ImageElementWithScore {
    final Element image;
    final int score;
    final int surface;

    public ImageElementWithScore(Element image, int score, int surface) {
        this.image = image;
        this.score = score;
        this.surface = surface;
    }
}
