package app.managers.backend;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.customsearch.v1.Customsearch;
import com.google.api.services.customsearch.v1.model.Result;
import com.google.api.services.customsearch.v1.model.Search;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author A.Mukhamedov
 */


public class WebScraper {
    private static final HttpClient httpClient = HttpClient.newBuilder().build();

    public static List<String> tryScrapeWebsites(List<String> urls) {
        List<String> textResponses = new ArrayList<>();

        for (String url : urls) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                Document doc = Jsoup.parse(response.body());
                doc.select("input, a, button, img, script, style, link, header, footer, nav, .sidebar, ul, ol").remove();

                Elements relevantElements = doc.select("p");

                StringBuilder relevantText = new StringBuilder();
                for (Element element : relevantElements)
                    relevantText.append(element.text().replaceAll("[|\\[\\](){}]", "")).append("\n");
                textResponses.add(String.valueOf(relevantText));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return textResponses;
    }

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
            list.setNum(2);

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
