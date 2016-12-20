/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.research.main;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Jonathan Elue
 */
public class Window extends JFrame implements ActionListener{
    
    public static final int KEYWORD = 0, OMIT = 1;
    
    private final Scraper scraper;
    private String[] keywords;
    private String[] omissions;
    
    private final JPanel mainPanel;
    
    private final JPanel keywordPanel;
    private final JPanel omissionPanel;
    private final JPanel topPanel;
    private final JPanel bellowTopPanel;
    private final JPanel midTop, midmid, midBottom;
    
    private final JTextField searchField;
    private final JButton search;
    
    private final JLabel searchAmount;
    private final JTextField amount;
    private final JButton loadKeywordsTextArea;
    private final JButton loadKeywords;
    private final JButton loadOmissionsTextArea;
    private final JButton loadOmissions;
    private final JButton[] arrows;
    private final JLabel sentenceLeft, sentenceRight, right, left;
    private int rightNum, leftNum;
    
    private final JButton save;
    
    public Window(String identifier) {
        super("Research");
        
        scraper = new Scraper(identifier);
        
        setSize(320, 240);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        rightNum = 0;
        leftNum = 0;
        
        mainPanel = new JPanel();
        keywordPanel = new JPanel();
        omissionPanel = new JPanel();
        topPanel = new JPanel();
        bellowTopPanel = new JPanel();
        midTop = new JPanel();
        midmid = new JPanel();
        midBottom = new JPanel();
        searchField = new JTextField(20);
        search = new JButton("Search");
        searchAmount = new JLabel("Search Amount: ");
        amount = new JTextField(2);
        loadKeywordsTextArea = new JButton("Load Keyword Text");
        loadKeywords = new JButton("Load Keyword File");
        loadOmissionsTextArea = new JButton("Load Omission Text");
        loadOmissions = new JButton("Load Omission File");
        arrows = new JButton[6];
        sentenceLeft = new JLabel("Sentences Left: ");
        left = new JLabel("0");
        sentenceRight = new JLabel("Sentences Right: ");
        right = new JLabel("0");
        save = new JButton("Save");
        
        init();
       
        setVisible(true);
        //pack();
    }
    
    private void init() {
        mainPanel.setLayout(new GridLayout(8, 1));
        
        search.addActionListener(this);
        
        searchAmount.setMinimumSize(searchAmount.getPreferredSize());
        searchAmount.setMaximumSize(searchAmount.getPreferredSize());
        
        topPanel.add(searchField);
        topPanel.add(search);
        
        bellowTopPanel.add(searchAmount);
        bellowTopPanel.add(amount);
        
        loadKeywordsTextArea.setBackground(Color.RED);
        loadKeywordsTextArea.addActionListener(this);
        
        loadOmissionsTextArea.setEnabled(false);
        loadOmissionsTextArea.setBackground(Color.RED);
        loadOmissionsTextArea.addActionListener(this);
                
        loadKeywords.setBackground(Color.RED);
        loadKeywords.addActionListener(this);
        
        loadOmissions.setEnabled(false);
        loadOmissions.setBackground(Color.RED);
        loadOmissions.addActionListener(this);
        
        arrows[0] = new JButton("<");
        arrows[0].addActionListener(this);
        arrows[1] = new JButton("<<>>");
        arrows[1].addActionListener(this);
        arrows[2] = new JButton(">");
        arrows[2].addActionListener(this);
        arrows[3] = new JButton(">");
        arrows[3].addActionListener(this);
        arrows[4] = new JButton(">><<");
        arrows[4].addActionListener(this);
        arrows[5] = new JButton("<");
        arrows[5].addActionListener(this);
        
        midTop.add(arrows[0]);
        midTop.add(arrows[1]);
        midTop.add(arrows[2]);
        
        midmid.add(arrows[3]);
        midmid.add(arrows[4]);
        midmid.add(arrows[5]);
        
        midBottom.add(sentenceLeft);
        midBottom.add(left);
        midBottom.add(sentenceRight);
        midBottom.add(right);
        
        save.setEnabled(false);
        save.addActionListener(this);
                
        keywordPanel.add(loadKeywords);
        keywordPanel.add(loadKeywordsTextArea);
        
        omissionPanel.add(loadOmissions);
        omissionPanel.add(loadOmissionsTextArea);
        
        mainPanel.add(topPanel);
        mainPanel.add(bellowTopPanel);
        mainPanel.add(keywordPanel);
        mainPanel.add(omissionPanel);
        mainPanel.add(midTop);
        mainPanel.add(midmid);
        mainPanel.add(midBottom);
        mainPanel.add(save);
        
        this.add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        
        if(keywords == null) {
            loadKeywords.setBackground(Color.RED);
            loadKeywordsTextArea.setBackground(Color.RED);
            loadOmissions.setEnabled(false);
            loadOmissionsTextArea.setEnabled(false);
            loadOmissions.setBackground(Color.RED);
            loadOmissionsTextArea.setBackground(Color.RED);
        }
        
        if(omissions == null) {
            loadOmissions.setBackground(Color.RED);
            loadOmissionsTextArea.setBackground(Color.RED);
        }
        
        if(!isInt(amount.getText())) {
            JOptionPane.showMessageDialog(null, "Amount is not valid", "Error", JOptionPane.PLAIN_MESSAGE); 
            return;
        }
        
        if(obj.equals(arrows[0])) {
            leftNum++;
        }
        
        if(obj.equals(arrows[1])) {
            leftNum++;
            rightNum++;
        }
        
        if(obj.equals(arrows[2])) {
            rightNum++;
        }
        
        if(obj.equals(arrows[3])) {
            if(leftNum > 0)
                leftNum--;
        }
        
        if(obj.equals(arrows[4])) {
            if(leftNum > 0)
                leftNum--;
            if(rightNum > 0)
                rightNum--;
        }
        
        if(obj.equals(arrows[5])) {
            if(leftNum > 0)
                rightNum--;
        }
        
        if(obj.equals(loadKeywordsTextArea)) {
            keywords = null;
            omissions = null;
            
            TextAreaWindow tWindow = new TextAreaWindow(this, Window.KEYWORD);
            
            loadKeywords.setBackground(Color.GREEN);
            loadKeywordsTextArea.setBackground(Color.GREEN);
            loadOmissions.setEnabled(true);
            loadOmissionsTextArea.setEnabled(true);
            loadOmissions.setBackground(Color.RED);
            loadOmissionsTextArea.setBackground(Color.RED);
            
            return;
        }
        
        if(obj.equals(loadKeywords)) {
            keywords = null;
            omissions = null;
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Text Files (*.txt)", "txt");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(null);
            String file = "";
            if(returnVal == JFileChooser.APPROVE_OPTION) {
              file = chooser.getSelectedFile().getAbsolutePath();
            }

            if(file.equals("") || !file.endsWith(".txt")) {
                JOptionPane.showMessageDialog(null, "Error in File Chosen", "Error", JOptionPane.PLAIN_MESSAGE); 
                loadKeywords.setBackground(Color.RED);
                loadOmissions.setBackground(Color.RED);
                loadOmissions.setEnabled(false);    
            }
            else {
                try(Scanner scan = new Scanner(new File(file))) {
                    keywords = scan.nextLine().split(",");
                    System.out.println(Arrays.toString(keywords));
                    scan.close();
                } catch(FileNotFoundException exc) {
                    exc.printStackTrace();
                }
                loadKeywords.setBackground(Color.GREEN);
                loadOmissions.setEnabled(true);
            }
        }
        
        if(obj.equals(loadOmissionsTextArea)) {
            omissions = null;
            
            TextAreaWindow tWindow = new TextAreaWindow(this, Window.OMIT);
            
            loadOmissions.setBackground(Color.GREEN);
            loadOmissionsTextArea.setBackground(Color.GREEN);
            
            return;
        }
        
        if(obj.equals(loadOmissions)) {
            omissions = null;
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Text Files (*.txt)", "txt");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(null);
            String file = "";
            if(returnVal == JFileChooser.APPROVE_OPTION) {
              file = chooser.getSelectedFile().getAbsolutePath();
            }

            if(file.equals("") || !file.endsWith(".txt")) {
                JOptionPane.showMessageDialog(null, "Error in File Chosen", "Error", JOptionPane.PLAIN_MESSAGE); 
                loadOmissions.setBackground(Color.RED);
            }
            else {
                try(Scanner scan = new Scanner(new File(file))) {
                    omissions = scan.nextLine().split(",");
                    System.out.println(Arrays.toString(omissions));
                    scan.close();
                } catch(FileNotFoundException exc) {
                    exc.printStackTrace();
                }
                loadOmissions.setBackground(Color.GREEN);
            }
        }
        
        if(obj.equals(search) && searchField.getText().length() > 0) {
            scraper.search(searchField.getText(), keywords, omissions, Integer.valueOf(amount.getText()), leftNum, rightNum);
            save.setEnabled(true);
            JOptionPane.showMessageDialog(null, "Search Query Completed", "Success", JOptionPane.PLAIN_MESSAGE);
        }
        else if(obj.equals(save)) {
            scraper.save();
            save.setEnabled(false);
        }
        
        left.setText(leftNum + "");
        right.setText(rightNum + "");
    }
    
    private boolean isInt(String s) {
        for(int i = 0; i < s.length(); i++) {
            if(!Character.isDigit(s.charAt(i)))
                return false;
        }
        return true;
    }
    
    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }
    
    public void setOmissions(String[] omissions) {
        this.omissions = omissions;
    }
}
