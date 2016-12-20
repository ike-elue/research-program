/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.research.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author Jonathan Elue
 */
public class TextAreaWindow extends JFrame implements ActionListener{

    private final JPanel mainPanel;
    
    private final JTextArea tArea;
    private final JButton done;
    
    private final Window window;
    private final int key;
    
    public TextAreaWindow(Window window, int key) {
        super("Enter Text Here");
        
        this.window = window;
        this.key = key;
        
        setResizable(false);
        setSize(320, 240);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        mainPanel = new JPanel();
        tArea = new JTextArea(20,16);
        done = new JButton("Done");
        
        initComponents();
        this.add(mainPanel);
        setVisible(true);
    }
    
    private void initComponents() {
        mainPanel.setLayout(new BorderLayout());
        
        JScrollPane scroll = new JScrollPane(tArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        done.addActionListener(this);
        
        mainPanel.add(scroll, BorderLayout.CENTER);
        mainPanel.add(done, BorderLayout.SOUTH);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if(obj.equals(done)) {
            if(key == Window.KEYWORD)
                window.setKeywords(tArea.getText().split(","));
            else if(key == Window.OMIT)
                window.setOmissions(tArea.getText().split(","));
            this.dispose();
        }
    }
    
}
