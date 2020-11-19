/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eaustria.webcrawler;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author bmayr
 */
// Recursive Action for forkJoinFramework from Java7
public class LinkFinderAction extends RecursiveAction {

    private String url;
    private ILinkHandler cr;
    /**
     * Used for statistics
     */
    private static final long t0 = System.nanoTime();

    private static boolean reached1500 = false;
    private static boolean reached3000 = false;

    public LinkFinderAction(String url, ILinkHandler cr) {
        this.url = url;
        this.cr = cr;
    }

    @Override
    public void compute() {

        if (cr.size() > 1500 && !reached1500) {
            reached1500 = true;
            System.out.println(((System.nanoTime() - t0) / 1000000000) + " s");
        }

        if (cr.size() > 3000 && !reached3000) {
            reached3000 = true;
            System.out.println(((System.nanoTime() - t0) / 1000000000) + " s");
        }

        if (!cr.visited(url)) {
            try {
                Document doc = Jsoup.connect(url).ignoreContentType(true).get();
                Elements links = doc.select("a[href]");
                cr.addVisited(url);

                List<LinkFinderAction> subfinders = links.stream()
                        .map(link -> {
                            if (link.attr("href").startsWith("http")) {
                                return new LinkFinderAction(link.attr("href"), cr);
                            }
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                invokeAll(subfinders);

            } catch (IOException e) {

            }
        }
    }
}
