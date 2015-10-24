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
import java.util.List;

public class Sample
{
    final static String AUTH_KEY = "";
    final static HttpClient httpclient = HttpClients.createDefault();

    public static void main(String[] args)
    {

        // not consistently outputting values

        List<JSON> toCompareWithList = new ArrayList<JSON>();
        String input =  "\"url\":\"http://www.richelehenry.com/wp-content/uploads/2012/12/istock_man-tired-man.jpg\"";
        JSON result = faceDetection(input);
        List<String> comparisons = new ArrayList<String>();
//        // Hulk - Detects it is not a real face
//        System.out.println("Hulk");
//        comparisons.add("\"url\":\"http://media.ignimgs.com/media/ign/imgs/minisites/topN/comic-book-heroes/9_Hulk.jpg\"");
//        faceDetection(comparisons.get(comparisons.size()-1));
        // Wonder Woman
        System.out.println("Wonder Woman");
        comparisons.add("\"url\":\"http://oyster.ignimgs.com/wordpress/stg.ign.com/2010/01/lynda-carter-wonder-woman.jpg\"");
        faceDetection(comparisons.get(comparisons.size() - 1));
        // Joker
        System.out.println("Joker");
        comparisons.add("\"url\":\"http://blogs-images.forbes.com/markhughes/files/2015/04/Heath-Ledger-Joker-The-Dark-Knight.png\"");
        faceDetection(comparisons.get(comparisons.size()-1));
//        // Magneto - not visible enough
//        System.out.println("Magneto");
//        comparisons.add("\"url\":\"https://s-media-cache-ak0.pinimg.com/736x/b1/0c/16/b10c166cc0bc63079d2750d67a3e72bb.jpg\"");
//        faceDetection(comparisons.get(comparisons.size()-1));
        // Storm
        System.out.println("Storm");
        comparisons.add("\"url\":\"http://d1oi7t5trwfj5d.cloudfront.net/d9/3738f0754e11e2922e22000a1d0930/file/x-men-storm.jpg\"");
        faceDetection(comparisons.get(comparisons.size()-1));
        // Jean Grey/Phoenix
        System.out.println("Jean Grey/Phoenix");
        comparisons.add("\"url\":\"http://vignette1.wikia.nocookie.net/xmenmovies/images/7/70/Phoenix_13.jpg/revision/latest?cb=20110626022356\"");
        faceDetection(comparisons.get(comparisons.size()-1));
//        // The Flash - mask
//        System.out.println("The Flash");
//        comparisons.add("\"url\":\"http://screenrant.com/wp-content/uploads/The-Flash-Grant-Gustin.png\"");
//        faceDetection(comparisons.get(comparisons.size()-1));
        // Superman
        System.out.println("Superman");
        comparisons.add("\"url\":\"http://static.comicvine.com/uploads/original/0/40/3114457-man-of-steel.jpg\"");
        faceDetection(comparisons.get(comparisons.size()-1));
        // Mystique
        System.out.println("Mystique");
        comparisons.add("\"url\":\"http://cdn.wegotthiscovered.com/wp-content/uploads/683013-540x360.jpg\"");
        faceDetection(comparisons.get(comparisons.size()-1));
//        // Catwoman (Eartha Kitt) - low quality
//        System.out.println("Catwoman (Eartha Kitt)");
//        comparisons.add("\"url\":\"http://vignette1.wikia.nocookie.net/batman/images/f/fc/KittCat.jpg/revision/latest?cb=20081127211019\"");
//        faceDetection(comparisons.get(comparisons.size()-1));
//        // Bane - not visible enough
//        System.out.println("Bane");
//        comparisons.add("\"url\":\"http://images.akamai.steamusercontent.com/ugc/452908955136883995/5D5331E4FCC6DD9034C1D4EFFDB9E7D483C560D7/\"");
//        faceDetection(comparisons.get(comparisons.size()-1));
        // Xavier
        System.out.println("Xavier");
        comparisons.add("\"url\":\"http://studentsites.woodlandschools.org/2015/thomast8/Web%20Design%201/Projects/X-men/professor-x.jpg/\"");
        faceDetection(comparisons.get(comparisons.size()-1));
        // Bing Bing Fan -
        System.out.println("Bing Bing Fan");
        comparisons.add("\"url\":\"http://screenrant.com/wp-content/uploads/Blink-in-X-Men-Days-of-Future-Past1.jpg/\"");
        faceDetection(comparisons.get(comparisons.size()-1));
//        for (String comp : comparisons) {
//            toCompareWithList.add(faceDetection(comp));
//        }
        JSON[] toCompareWith = new JSON[toCompareWithList.size()];
     //   findSimilar(result, toCompareWithList.toArray(toCompareWith));
    }

    public static JSON faceDetection(String url) {
        try
        {
            JSON result = new JSON();
            URIBuilder builder = new URIBuilder("https://api.projectoxford.ai/face/v0/detections");
            builder.setParameter("analyzesFaceLandmarks", "false");
            builder.setParameter("analyzesAge", "true");
            builder.setParameter("analyzesGender", "true");
            builder.setParameter("analyzesHeadPose", "false");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.addHeader("Content-Type" , "application/json");
            request.addHeader("Ocp-Apim-Subscription-Key" , AUTH_KEY);

            // Request body
            StringEntity reqEntity = new StringEntity("{" + url + "}");
            request.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                String raw = EntityUtils.toString(entity);
                Gson gson = new Gson();
                JSON[] obj = gson.fromJson(raw, JSON[].class);
                result = obj[0];
                System.out.println(result.toString());
                System.out.println(raw);
            }
            return result;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static JSON findSimilar(JSON user, JSON[] toCompareWith) {
        try {
            URIBuilder builder = new URIBuilder("https://api.projectoxford.ai/face/v0/findsimilars");


            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.addHeader("Content-Type", "application/json");
            request.addHeader("Ocp-Apim-Subscription-Key", "824a50cdbc2c47ae84fdab4e42882835");

            // Request body
            StringEntity reqEntity = new StringEntity("{\"faceId\":\"" + user.getFaceId() + "\",\n" +
                    "\"faceIds\":[\n" +
                    "\"" + toCompareWith[0] + "\",\n" +
                    "\"" + toCompareWith[1] + "\",\n" +
                    "\"" + toCompareWith[2] + "\"\n" +
                    "]}");
            request.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                System.out.println(EntityUtils.toString(entity));
            }
            return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

}