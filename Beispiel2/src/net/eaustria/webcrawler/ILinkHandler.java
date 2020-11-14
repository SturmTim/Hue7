/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// This is the Interface which LinkFinder (for Java6) and LinkFinderAction (for
// Java7) will implement!

package net.eaustria.webcrawler;

/**
 *
 * @author bmayr
 */
public interface ILinkHandler {

    /**
     * Places the link in the queue
     * @param link
     * @throws Exception 
     */
    void queueLink(String link) throws Exception;

    /**
     * Returns the number of visited links
     * @return 
     */
    int size();

    /**
     * Checks if the link was already visited
     * @param link
     * @return 
     */
    boolean visited(String link);

    /**
     * Marks this link as visited
     * @param link 
     */
    void addVisited(String link);
}
