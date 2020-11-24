/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eaustria.webcrawler;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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

                for (Element link : links) {
                    if (link.attr("href").startsWith("http")) {
                        try {
                            linkHandler.queueLink(link.attr("href"));
                        } catch (Exception ex) {
                            System.out.println("Exception");
                        }
                    }
                }

            } catch (IOException e) {

            }
        }
    }
}
