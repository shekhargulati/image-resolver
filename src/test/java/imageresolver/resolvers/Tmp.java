package imageresolver.resolvers;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class Tmp {

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(new Request.Builder().head().url("https://upload.wikimedia.org/wikipedia/commons/3/3f/JPEG_example_flower.jpg").build());
        Response response = call.execute();
        long end = System.currentTimeMillis();
        System.out.println(String.format("Total time: %d sec(s)", (end - start) / 1000));
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(response.headers());
    }
}
