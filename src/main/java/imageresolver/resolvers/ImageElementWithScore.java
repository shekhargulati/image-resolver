package imageresolver.resolvers;

import org.jsoup.nodes.Element;

import java.util.StringJoiner;

public class ImageElementWithScore {
    final Element image;
    final int score;
    final int surface;
    final int parents;

    public ImageElementWithScore(Element image, int score, int surface, int parents) {
        this.image = image;
        this.score = score;
        this.surface = surface;
        this.parents = parents;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(" ,");
        joiner.add("src: " + image);
        joiner.add("score: " + score);
        joiner.add("parents: " + parents);
        return joiner.toString();
    }
}
