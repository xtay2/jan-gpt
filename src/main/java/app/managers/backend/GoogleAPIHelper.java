package app.managers.backend;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.customsearch.v1.Customsearch;
import com.google.api.services.customsearch.v1.model.Result;
import com.google.api.services.customsearch.v1.model.Search;

import java.util.ArrayList;
import java.util.List;


/**
 * @author A.Mukhamedov
 */
public class GoogleAPIHelper {
    public static List<String> tryGetLinksFor(String query) {

        String apiKey = "AIzaSyAY_Pub4sFkdQbybFPdgMOPIPKpMOpMQKc";
        String searchEngineId = "f296827fce4f6406f";

        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();

        Customsearch customsearch = new Customsearch
                .Builder(httpTransport, jsonFactory, null)
                .setApplicationName("Google Search API")
                .build();

        try {
            Customsearch.Cse.List list = customsearch.cse().list();
            list.setQ(query);
            list.setKey(apiKey);
            list.setCx(searchEngineId);
            list.setNum(3);

            Search results = list.execute();

            List<String> links = new ArrayList<>();
            if (results.getItems() == null)
                return List.of();
            for (Result result : results.getItems())
                links.add(result.getLink());
            return links;

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
