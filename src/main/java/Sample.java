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
        Map<String, String> heroes = new HashMap<String, String>();
        heroes.put("Wonder Woman", "404fbc35-10d0-4113-a8db-34474aa4097d");
        heroes.put("Joker", "95ad53d0-f32a-4e0f-97fa-784ed1be06b8");
        heroes.put("Storm", "98d34aec-9b84-4d10-a6ef-3d9485ac8956");
        heroes.put("Jean Grey/Phoenix", "9d7ccb17-0fb4-4e8d-a789-5f21f5c28fcf");
        heroes.put("Superman", "9ff9f9b6-5cb4-4bec-87ea-8dab0c1d5a2d");
        heroes.put("Mystique", "f37bd5b1-4591-404b-a68a-204d3838bc4c");
        heroes.put("Xavier", "5fd517b0-c6b9-4fb8-8795-c9eb20e3d76a");
        String[] toCompareWith = new String[heroes.size()];
        int count = 0;
        for (String names : heroes.keySet()) {
            toCompareWith[count++] = heroes.get(names);
        }
        findSimilar(result, toCompareWith);
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

    public static JSON findSimilar(JSON user, String[] toCompareWith) {
        try {
            URIBuilder builder = new URIBuilder("https://api.projectoxford.ai/face/v0/findsimilars");


            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.addHeader("Content-Type", "application/json");
            request.addHeader("Ocp-Apim-Subscription-Key", AUTH_KEY);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < toCompareWith.length-2; i++) {
                sb.append("\"" + toCompareWith[i] + "\",\n");
            }
            sb.append("\"" + toCompareWith[toCompareWith.length-1] + "\"\n");

            // Request body
            StringEntity reqEntity = new StringEntity("{\"faceId\":\"" + user.getFaceId() + "\",\n" +
                    "\"faceIds\":[\n" + sb.toString() + "]}");

            request.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                System.out.println("Done!");
                System.out.println(EntityUtils.toString(entity));
            }
            return null;
        }
        catch (Exception e)
        {
            System.out.println("Error");
            System.out.println(e.getMessage());
        }
        return null;
    }

}