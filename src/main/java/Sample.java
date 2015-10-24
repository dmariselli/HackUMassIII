// // This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sample
{
    final static String AUTH_KEY = "xxx";
    final static HttpClient httpclient = HttpClients.createDefault();

    public static void main(String[] args) {

        String input =  "\"url\":\"http://www.richelehenry.com/wp-content/uploads/2012/12/istock_man-tired-man.jpg\"";
        JSON result = faceDetection(input);
        // ^ faceID = a3ee2200-094c-4bb5-abda-693a408ea814
        Map<String, String> heroes = new HashMap<String, String>();
        // Wonder Woman
        System.out.println("Wonder Woman");
        heroes.put("Wonder Woman", faceDetection("\"url\":\"http://oyster.ignimgs.com/wordpress/stg.ign.com/2010/01/lynda-carter-wonder-woman.jpg\"").getFaceId());
        // Joker
        System.out.println("Joker");
        heroes.put("Joker", faceDetection("\"url\":\"http://blogs-images.forbes.com/markhughes/files/2015/04/Heath-Ledger-Joker-The-Dark-Knight.png\"").getFaceId());
        // Storm
        System.out.println("Storm");
        heroes.put("Storm", faceDetection("\"url\":\"http://d1oi7t5trwfj5d.cloudfront.net/d9/3738f0754e11e2922e22000a1d0930/file/x-men-storm.jpg\"").getFaceId());
        // Jean Grey/Phoenix
        System.out.println("Jean Grey/Phoenix");
        heroes.put("Jean Grey/Phoenix", faceDetection("\"url\":\"http://vignette1.wikia.nocookie.net/xmenmovies/images/7/70/Phoenix_13.jpg/revision/latest?cb=20110626022356\"").getFaceId());
        // Superman
        System.out.println("Superman");
        heroes.put("Superman", faceDetection("\"url\":\"http://static.comicvine.com/uploads/original/0/40/3114457-man-of-steel.jpg\"").getFaceId());
        // Mystique
        System.out.println("Mystique");
        heroes.put("Mystique", faceDetection("\"url\":\"http://cdn.wegotthiscovered.com/wp-content/uploads/683013-540x360.jpg\"").getFaceId());
        // Xavier
        System.out.println("Xavier");
        heroes.put("Xavier", faceDetection("\"url\":\"http://studentsites.woodlandschools.org/2015/thomast8/Web%20Design%201/Projects/X-men/professor-x.jpg\"").getFaceId());

        String[] toCompareWith = new String[heroes.size()];
        int count = 0;
        for (String names : heroes.keySet()) {
            toCompareWith[count++] = heroes.get(names);
        }
        String similarHero = findSimilar(result, toCompareWith);
        System.out.println(similarHero);
    }

    public static JSON faceDetection(String url) {
        try {
            JSON result = new JSON();
            URIBuilder builder = new URIBuilder("https://api.projectoxford.ai/face/v0/detections");
            builder.setParameter("analyzesFaceLandmarks", "false");
            builder.setParameter("analyzesAge", "true");
            builder.setParameter("analyzesGender", "true");
            builder.setParameter("analyzesHeadPose", "false");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.addHeader("Content-Type", "application/json");
            request.addHeader("Ocp-Apim-Subscription-Key", AUTH_KEY);

            // Request body
            StringEntity reqEntity = new StringEntity("{" + url + "}");
            request.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String raw = EntityUtils.toString(entity);
                Gson gson = new Gson();
                JSON[] obj = gson.fromJson(raw, JSON[].class);
                result = obj[0];
                System.out.println(result.toString());
                System.out.println(raw);
            }
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static String findSimilar(JSON user, String[] toCompareWith) {
        double current = 0.0;
        double max = 0.0;
        String similar = null;
        String def = "hulk";
        for (int i = 0; i < toCompareWith.length-1; i++) {
            current = compare(user, toCompareWith[i]);
            System.out.println("Look: " + current);
            if (current > max) {
                max = current;
                similar = toCompareWith[i];
            }
        }
        if (similar != null) {
            return similar;
        }
        return def;
    }

    public static double compare(JSON user, String comparison) {
        try {
            URIBuilder builder = new URIBuilder("https://api.projectoxford.ai/face/v0/verifications");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.addHeader("Content-Type", "application/json");
            request.addHeader("Ocp-Apim-Subscription-Key", AUTH_KEY);

            // Request body
            StringEntity reqEntity = new StringEntity("{\"faceId1\":\"" + user.getFaceId() + "\",\n" +
                    "\"faceId2\":\"" + comparison + "}");

            System.out.println("About to compare with: " + user.getFaceId() + " with " + comparison);

            request.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            Double real = 0.0;
            if (entity != null) {
                System.out.println("Done!");
                String result = EntityUtils.toString(entity);
                real = Double.parseDouble(result);
                System.out.println(EntityUtils.toString(entity));
            }
            return real;
        }
        catch (Exception e)
        {
            System.out.println("Error");
            System.out.println(e.getMessage());
        }
        return 0.0;
    }

}