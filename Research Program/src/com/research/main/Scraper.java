/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.research.main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author Jonathan Elue
 */
public class Scraper {
    private Document document;
    
    private String keyword;
    private int previousAmount;
    
    private final List<String> listOfLinks;
    private final List<String> texts;
    private final List<String> products;
    
    private final String charset;

    private final String indention;
    private final String identifier;
    private final String[] ignoreAbbreviations;
    private String[] omissions;
    
    /**
     * Constructor
     * @param identifier should be a unique set of characters that don't clash with patterns 
     */
    public Scraper(String identifier) {
        this.identifier = identifier;
        ignoreAbbreviations = format(new String[] {"Jr.",
                "Sr.",
                "Mr.",
                "Ms.",
                "Mrs.",
                "Dr.",
                "St.",
                "Mon.",
                "Tu.",
                "Tue.",
                "Tues.",
                "Wed.",
                "Th.",
                "Thu.",
                "Thur.",
                "Thurs.",
                "Fri.",
                "Sat.",
                "Sun.",
                "D.C.",
                "U.S.",
                "U.S.A."}, identifier);
        omissions = null;
        indention = "     ";
        document = null;
        keyword = "";
        previousAmount = 0;
        listOfLinks = new ArrayList<>();
        texts = new ArrayList<>();
        products = new ArrayList<>();
        charset = "UTF-8";
    }
   
    /**
     * Replaces each period in each array entry with the changer
     * @param array strings to change
     * @param changer what the period changes into
     * @return 
     */
    private String[] format(String[] array, String changer) {
        String[] strings = new String[array.length];
        for(int i = 0; i < strings.length; i++) {
            strings[i] = array[i].replaceAll("\\.", changer);
        }
        return strings;
    }

    /**
     * Searches the keyword and saves the links
     * @param keyword String you want to search
     * @param lookForInSearch String you want to search in the links
     * @param omissions strings to not include in result
     * @param amount Amount of links to get
     * @param sentenceAmountToLeft When getting the sentence that contains keyword this determines how far left
     * @param sentenceAmountToRight This determines how far right 
     */
    public void search(String keyword, String[] lookForInSearch, String[] omissions, int amount, int sentenceAmountToLeft, int sentenceAmountToRight) {
        if(!keyword.equals(this.keyword)) {
            previousAmount = 0;
            this.keyword = keyword;
        }
        
        if(omissions != null)
            this.omissions = omissions.clone();
        else 
            this.omissions = null;
        
        listOfLinks.clear();
        texts.clear();
        String search = "http://www.google.com/search?q=";
        try {
            document = org.jsoup.Jsoup.connect(search + URLEncoder.encode(keyword, charset) + "&num=" + (previousAmount + amount)).userAgent("Chrome").get();
            Elements links = document.select("h3.r > a");
            for(int i = previousAmount; i < links.size(); i++) {
                listOfLinks.add(links.get(i).absUrl("href"));
                texts.add(links.get(i).text());
            }
        } catch(IOException exc) {
            exc.printStackTrace();
        }
        this.previousAmount = amount;
        if(lookForInSearch != null)
            find(lookForInSearch, sentenceAmountToLeft, sentenceAmountToRight);
        else
            find(new String[] {keyword}, sentenceAmountToLeft, sentenceAmountToRight);
    }
    
    /**
     * Find and Saves the sentences in the links that contains any element of lookForIn Search
     * @param lookForInSearch strings to search for
     * @param sentenceAmountToLeft amount of previous sentences to grab
     * @param sentenceAmountToRight amount of future sentences to grab
     */
    public void find(String[] lookForInSearch, int sentenceAmountToLeft, int sentenceAmountToRight) {
        products.clear();
        if(listOfLinks.isEmpty() || texts.isEmpty()) {
            System.out.println("List Are Empty");
            return;
        }
        String str;
        try {
            for(int i = 0; i < listOfLinks.size(); i++) {
                document = org.jsoup.Jsoup.connect(listOfLinks.get(i)).userAgent("Chrome").get();
                str = document.select("body").text().trim();
                // str = sentences.stream().map((e) -> e.text()).reduce(str, String::concat);
                if(str.length() >0 )
                    products.add(StringUtil.getWebsiteStrings(identifier, omissions, ignoreAbbreviations, lookForInSearch, str, sentenceAmountToLeft, sentenceAmountToRight));
                str = "";
            }
        } catch(IOException exc) {
            exc.printStackTrace();
        }
    }
    
    /**
     * Prints the texts and products to console (Only for testing)
     * @param test doesn't matter
     */
    public void save(boolean test) {
        for(int i = 0; i < products.size(); i++) {
            System.out.println("Website: " + texts.get(i));
            System.out.println("Link: " + listOfLinks.get(i));
            int counter = 1;
            String str = products.get(i);
            int start = 0;
            int end;
            boolean stopLooping = false;
            while(str.contains(counter + ": ") || !stopLooping) {
                end = str.indexOf((counter+ 1) + ": ");
                if(end <= -1)
                    end = str.length();
                
                if(end - start > 0)
                    System.out.println(indention + str.substring(start, end));
                else    
                    stopLooping = true;
                start = end;
                counter++;
            }
            System.out.println();
            System.out.println();
        }
    }
    
    /**
     * Saves the texts and products to text document 
     */
    public void save() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Research Files (*.rs)", "rs");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        String file = "";
        if(returnVal == JFileChooser.APPROVE_OPTION) {
          file = chooser.getSelectedFile().getAbsolutePath();
        }

        if(file.equals(""))
            JOptionPane.showMessageDialog(null, "Error in File Chosen", "Error", JOptionPane.PLAIN_MESSAGE); 
        
        else {
            PrintWriter writer = null;
            
            if(!file.substring(file.length() - 3).equals(".rs"))
                file += ".rs";
            
            try {
                writer = new PrintWriter(new FileWriter(file, true));
            } catch (IOException ex) {
                Logger.getLogger(Scraper.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(writer != null) {
                for(int i = 0; i < texts.size(); i++) {
                    writer.println("Website: " + texts.get(i));
                    writer.println("Link: " + listOfLinks.get(i));
                    printProducts(writer, i); 
                    writer.println();
                    writer.println();
                }
                JOptionPane.showMessageDialog(null, "Search has been successfully saved", "Success!", JOptionPane.PLAIN_MESSAGE);
                writer.close();
                writer.flush();
            }
        }
    }
    
    /**
     * Prints the products correctly so any file can read it how it should be read
     * @param writer prints to file
     * @param i current iteration of the products
     */
    private void printProducts(PrintWriter writer, int i) {
        int counter = 1;
        String str = products.get(i);
        int start = 0;
        int end;
        boolean stopLooping = false;
        while(str.contains(counter + ": ") || !stopLooping) {
            end = str.indexOf((counter+ 1) + ": ");
            if(end <= -1)
                end = str.length();
            if(end - start > 0) {
                writer.println();
                writer.println(indention + str.substring(start, end));
            }
            else    
                stopLooping = true;
           
            start = end;
            counter++;
        }
    }
}