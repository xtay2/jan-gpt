package app.managers.backend;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author A.Mukhamedov
 */


public class WebScraper {
    private static final HttpClient httpClient = HttpClient.newBuilder().build();

    public static void main(String[] args) {
        returnScrapedContent();
    }

    private static void returnScrapedContent() {
        List<String> links = GoogleAPIHelper.tryGetLinksFor("welche kriege gibt es aktuell und warum?");
        List<String> htmlResponses = scrapeWebsites(links);
        System.out.println(links.get(0));
        System.out.println(htmlResponses.get(0));
        System.out.println(links.get(1));
        System.out.println(htmlResponses.get(1));
        System.out.println(links.get(2));
        System.out.println(htmlResponses.get(2));

    }

    public static List<String> scrapeWebsites(List<String> urls) {
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

                textResponses.add(relevantText.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return textResponses;
    }
}
