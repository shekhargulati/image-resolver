package imageresolver.resolvers;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.stream.IntStream;

public class Tmp {

    public static void main(String[] args) throws IOException {
        IntStream.range(0, 9).forEach(i -> {
            try {
                makeCall();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }

    public static void makeCall() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().get().url("https://www.forbes.com/sites/startswithabang/2017/01/24/nobody-knows-where-a-black-holes-information-goes/").build();
        long start = System.currentTimeMillis();
        Call call = client.newCall(request);
        call.execute();
        long end = System.currentTimeMillis();
        System.out.println(String.format("Total time: %d sec(s)", (end - start) / 1000));
    }
}
