/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.research.main;

/**
 *
 * @author Jonathan Elue
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scraper scraper = new Scraper("-/-");
        scraper.search("Martin Luther King", new String[] {"Martin"}, 3, 1, 1);
        scraper.save(true);
    }
    
}
