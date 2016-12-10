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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Jonathan Elue
 */
public class Window extends JFrame implements ActionListener{
    private final Scraper scraper;
    private String[] keywords;
    
    private final JPanel mainPanel;
    
    private final JPanel topPanel;
    private final JPanel midTop, midmid, midBottom;
    
    private final JTextField searchField;
    private final JButton search;
    
    private final JTextField amount;
    // Green when succesful, red when not
    private final JButton loadKeywords;
    
    private final JButton[] arrows;
    private final JLabel sentenceLeft, sentenceRight;
    private int right, left;
    
    private final JButton save;
    
    public Window(String identifier) {
        super("Research");
        
        scraper = new Scraper(identifier);
        
        setSize(320, 240);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        
        right = 0;
        left = 0;
        
        mainPanel = new JPanel();
        topPanel = new JPanel();
        midTop = new JPanel();
        midmid = new JPanel();
        midBottom = new JPanel();
        searchField = new JTextField("Enter Search Word Here");
        search = new JButton("Search");
        amount = new JTextField("0");
        loadKeywords = new JButton("Load Keywords");
        arrows = new JButton[6];
        sentenceLeft = new JLabel("Sentences Left: " + left);
        sentenceRight = new JLabel("Sentences Right: " + right);
        save = new JButton("Save");
        
        init();
       
        setVisible(true);
    }
    
    private void init() {
        mainPanel.setLayout(new GridLayout(6, 1));
        
        search.addActionListener(this);
        
        topPanel.add(searchField);
        topPanel.add(search);
        
        loadKeywords.setBackground(Color.RED);
        loadKeywords.addActionListener(this);
        
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
        midBottom.add(sentenceRight);
        
        save.setEnabled(false);
        save.addActionListener(this);
                
        mainPanel.add(topPanel);
        mainPanel.add(loadKeywords);
        mainPanel.add(midTop);
        mainPanel.add(midmid);
        mainPanel.add(midBottom);
        mainPanel.add(save);
        
        this.add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if(obj.equals(search)) {
            // Check amount if valid
            scraper.search(searchField.getText(), keywords, Integer.valueOf(amount.getText()), left, right);
        }
        else if(obj.equals(save)) {
            scraper.save();
        }
    }
}
