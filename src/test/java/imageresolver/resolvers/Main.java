package imageresolver.resolvers;

import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        Pattern patter = Pattern.compile("[-_]ad", Pattern.CASE_INSENSITIVE);
        String str = "dfp_ad dfp_campaign_component";
        System.out.println(patter.matcher(str).find());
    }

}
