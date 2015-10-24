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
import java.util.HashMap;
import java.util.Map;

public class FacialAnalysis
{
    final static String AUTH_KEY = "xxx";
    final static HttpClient httpclient = HttpClients.createDefault();

    public static void main(String[] args) throws InterruptedException {
        try {
            Map<String, String> heroes = new HashMap<String, String>();
            String input =  "\"url\":\"http://orig15.deviantart.net/5470/f/2010/105/4/5/random_person_by_vurtov.jpg\"";
            JSON result = faceDetection(input);
            if (result.getAttributes().getGender().equalsIgnoreCase("Female")) {
                heroes = initForW(heroes);
            }
            heroes = initForM(heroes);

            String[] toCompareWith = new String[heroes.size()];
            int count = 0;
            for (String ids : heroes.keySet()) {
                toCompareWith[count++] = ids;
            }
            String similarHero = heroes.get(findSimilar(result, toCompareWith));
            System.out.println(similarHero);
        }
        catch (Exception e) {
            System.out.println("Time leak");
            Thread.sleep(50000);
            main(null);
        }
    }

    public static Map<String, String> initForW(Map<String, String> heroes) {
        // Wonder Woman
        System.out.println("Wonder Woman");
        heroes.put(faceDetection("\"url\":\"http://oyster.ignimgs.com/wordpress/stg.ign.com/2010/01/lynda-carter-wonder-woman.jpg\"").getFaceId(), "Wonder Woman");
        // Storm
        System.out.println("Storm");
        heroes.put(faceDetection("\"url\":\"http://cdn.movieweb.com/img.news/NE3IBkFlPbuF6b_1_2.jpg\"").getFaceId(), "Storm");
        // Jean Grey/Phoenix
        System.out.println("Jean Grey/Phoenix");
        heroes.put(faceDetection("\"url\":\"http://vignette1.wikia.nocookie.net/xmenmovies/images/7/70/Phoenix_13.jpg/revision/latest?cb=20110626022356\"").getFaceId(), "Jean Grey/Phoenix");
        // Mystique
        System.out.println("Mystique");
        heroes.put(faceDetection("\"url\":\"http://cdn.wegotthiscovered.com/wp-content/uploads/683013-540x360.jpg\"").getFaceId(), "Mystique");
        return heroes;
    }

    public static Map<String, String> initForM(Map<String, String> heroes) {
        // Joker
        System.out.println("Joker");
        heroes.put(faceDetection("\"url\":\"http://blogs-images.forbes.com/markhughes/files/2015/04/Heath-Ledger-Joker-The-Dark-Knight.png\"").getFaceId(), "Joker");
        // Iron Man
        System.out.println("Iron Man");
        heroes.put(faceDetection("\"url\":\"http://cdn.idigitaltimes.com/sites/idigitaltimes.com/files/2014/09/08/iron-man-4.jpg\"").getFaceId(), "Iron Man");
        // Superman
        System.out.println("Superman");
        heroes.put(faceDetection("\"url\":\"http://www.coupdemainmagazine.com/sites/default/files/3/Henry%20Cavill.jpg\"").getFaceId(), "Superman");
        // Xavier
        System.out.println("Bing Bing Fan");
        heroes.put(faceDetection("\"url\":\"http://screenrant.com/wp-content/uploads/Blink-in-X-Men-Days-of-Future-Past1.jpg\"").getFaceId(), "Bing Bing Fan");
        return heroes;
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
            System.out.println("Current: " + current);
            System.out.println("Max: " + max);
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
                    "\"faceId2\":\"" + comparison + "\"}");

            System.out.println("About to compare " + user.getFaceId() + " with " + comparison);

            request.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            Double real = 0.0;
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                String[] split = result.split(":");
                String forrealz = split[2].substring(0, split[2].length()-1);
                real = Double.parseDouble(forrealz);
            }
            System.out.println("Double forming: " + real);
            return real;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return 0.0;
    }

}