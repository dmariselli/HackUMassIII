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

public class Sample
{
    public static void main(String[] args)
    {
        HttpClient httpclient = HttpClients.createDefault();

        try
        {
            URIBuilder builder = new URIBuilder("https://api.projectoxford.ai/face/v0/detections");
            builder.setParameter("analyzesFaceLandmarks", "false");
            builder.setParameter("analyzesAge", "true");
            builder.setParameter("analyzesGender", "true");
            builder.setParameter("analyzesHeadPose", "false");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.addHeader("Content-Type" , "application/json");
            request.addHeader("Ocp-Apim-Subscription-Key" , "***");

            // Request body
            StringEntity reqEntity = new StringEntity("{ \"url\":\"http://www.richelehenry.com/wp-content/uploads/2012/12/istock_man-tired-man.jpg\" }");
            request.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                String raw = EntityUtils.toString(entity);
                Gson gson = new Gson();
                JSON[] obj = gson.fromJson(raw, JSON[].class);
                JSON result = obj[0];
                System.out.println(result.toString());
                System.out.println(raw);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}