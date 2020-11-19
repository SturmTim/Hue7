/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eaustria.webcrawler;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author bmayr
 */
public class LinkFinder implements Runnable {

    private String url;
    private ILinkHandler linkHandler;
    /**
     * Used fot statistics
     */
    private static final long t0 = System.nanoTime();

    private static boolean reached1500 = false;
    private static boolean reached3000 = false;

    public LinkFinder(String url, ILinkHandler handler) {
        this.url = url;
        this.linkHandler = handler;
    }

    @Override
    public void run() {
        getSimpleLinks(url);
    }

    private void getSimpleLinks(String url) {
        // ToDo: Implement
        // 1. if url not already visited, visit url with linkHandler
        // 2. get url and Parse Website
        // 3. extract all URLs and add url to list of urls which should be visited
        //    only if link is not empty and url has not been visited before
        // 4. If size of link handler equals 500 -> print time elapsed for statistics
        if (linkHandler.size() > 1500 && !reached1500) {
            reached1500 = true;
            System.out.println(((System.nanoTime() - t0) / 1000000000) + " s");
        }

        if (linkHandler.size() > 3000 && !reached3000) {
            reached3000 = true;
            System.out.println(((System.nanoTime() - t0) / 1000000000) + " s");
        }

        if (!linkHandler.visited(url)) {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements links = doc.select("a[href]");
                linkHandler.addVisited(url);

                links.stream()
                        .forEach(link -> {
                            try {
                                if (link.attr("href").startsWith("http")) {
                                    linkHandler.queueLink(link.attr("href"));
                                }
                            } catch (Exception e) {
                                System.out.println("getSimpleLinks Exception");
                            }
                        });

            } catch (IOException e) {

            }
        }
    }
}
