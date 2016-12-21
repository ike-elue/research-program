/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.research.main;

import java.util.ArrayList;

/**
 *
 * @author Jonathan Elue
 */
public class StringUtil {
   
    /**
     * Returns the sentences in the website that fit the criteria
     * @param identifier unique set of characters that don't clash with patterns 
     * @param omissions strings that you don't want in the result 
     * @param ignoreAbbreviations Abbreviations to ignore (because they have periods) 
     * @param keywords words to look for in webpage
     * @param webpage all the sentences of a webpage
     * @param sentencesLeft amount of past sentences to grab
     * @param sentencesRight amount of future sentences to grab 
     * @return the sentences in the website that fit the criteria 
     */
    public static String getWebsiteStrings(String identifier, String[] omissions, String[] ignoreAbbreviations,  String[] keywords, String webpage, int sentencesLeft, int sentencesRight) {
        String doc = webpage.replaceAll("\\.", identifier).replaceAll("\"", identifier).replaceAll("!", identifier).replaceAll("\\?", identifier);
        String[] sentences = format(doc, ignoreAbbreviations, identifier).split(identifier);
        int currentIndex = -1;
        String str = "";
        boolean finding = true;
        int left;
        int right = 0;
        int counter = 1;
        while(finding) {
            finding = false;
            for(int i = right; i < sentences.length; i++) {
                if(isKeyword(sentences[i], keywords)) {
                    currentIndex = i;
                    finding = true;
                    break;
                }
            }
            if(finding) {
                left = currentIndex - sentencesLeft;
                if(left < 0)
                    left = 0;
                right = currentIndex + sentencesRight + 1;
                if(right > sentences.length)
                    right = sentences.length;
                
                if(!containsOmission(omissions, sentences, left, right)) {
                    str += counter + ": ";
                    for(int i = left; i < right; i++) {
                        str += sentences[i].trim() + ". ";
                    }
                    counter++;  
                }             
            }
        }
        if(str.length() == 0)
            return "";
        return str.replaceAll(identifier, "\\.").trim().substring(0, str.length() - (Integer.toString(counter - 1).length() + 2));
    }
    
    /**
     * Returns the str that has the identifier replaced with a period
     * @param str string to look at
     * @param comparator items to look for in str
     * @param identifier unique set of characters to be replaced
     * @return str that has the identifier replaced with a period
     */
    private static String format(String str, String[] comparator, String identifier) {
        String s = str;
        for (String comparator1 : comparator) {
            s = s.replaceAll(comparator1, comparator1.replaceAll(identifier, "\\."));
        }
        return s;
    }
    
    /**
     * Returns true if the given string matches any of the keywords (or phrases),
     * otherwise false
     * @param statement
     * @param keywords
     * @return true if the given string matches any of the keywords (or phrases),
     * otherwise false
     */
    private static boolean isKeyword(String statement, String[] keywords) {
        return contains(lowerCase(statement.split(" ")), lowerCase(keywords)) != -1;
    }
    
    /**
     * Return -1 if the elements of the comparator are not parallel with the 
     * elements presented in the array or else it will return the index in which
     * the array matches the given comparator
     * @param array set of words to check
     * @param comparator set of entries to compare to
     * @return index of when the array matches the comparator 
     */
    private static int contains(String[] array, String[] comparator) {
        ArrayList<String[]> parts = new ArrayList<>();
        for (String comparator1 : comparator) {
            parts.add(comparator1.split(" "));
        }
        boolean isEqual;
        for(int i = 0; i < array.length; i++) {
            for(int j = 0; j < parts.size(); j++) {
                isEqual = true;
                for(int k = 0; k < parts.get(j).length; k++) {
                    if(i + k < array.length) {
                        isEqual = isEqual 
                                && parts.get(j)[k]
                                        .replaceAll("[(){},.;!?<>%]", "")
                                        .trim()
                                        .equals(array[i+k]
                                                .replaceAll("[(){},.;!?<>%]", ""));
                    }   
                }
                if(isEqual)
                    return i;
            }
        }
        return -1;
    }
    
    /**
     * Makes all elements of strs lowercase
     * @param strs elements to change
     * @return strs elements in lowercase format
     */
    private static String[] lowerCase(String[] strs) {
        String[] strings = new String[strs.length];
        for(int i = 0; i < strings.length; i++) {
            strings[i] = strs[i].toLowerCase();
        }
        return strings;
    }
    
    /**
     * Returns true if the sentences contain the omissions
     * @param omissions elements you don't want in final result
     * @param sentences sentences in doc
     * @param left start index 
     * @param right end index
     * @return true if the sentences contain the omissions
     */
    private static boolean containsOmission(String[] omissions, String[] sentences, int left, int right) {
        if(omissions == null)
            return false;
        
        String str = "";
        for(int i = left; i < right; i++) {
            str += sentences[i];
        }
        return isKeyword(str, omissions);
    }
}