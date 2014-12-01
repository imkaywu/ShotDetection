package com.blakay;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;

public class MyMenu{
	private MainFrame _mainFrame;
	private JMenuBar menuBar;
	private JMenu menu1, menu2;
	private ButtonGroup group1, group2;
    private JRadioButtonMenuItem rbMenuItem1;
    private JRadioButtonMenuItem rbMenuItem2;
    private JRadioButtonMenuItem rbMenuItem3;
    private JRadioButtonMenuItem rbMenuItem4;
    private JRadioButtonMenuItem rbMenuItem5;
    private JRadioButtonMenuItem rbMenuItem6;
    private JRadioButtonMenuItem rbMenuItem7;
    private JRadioButtonMenuItem rbMenuItem8;
    private JRadioButtonMenuItem rbMenuItem9;
    private JRadioButtonMenuItem rbMenuItem10;
    private String _method = "Global Pixel2Pixel Difference";
    private String _colorModel = "YIQ";
    
    public MyMenu(MainFrame mainFrame) {
    	_mainFrame = mainFrame;
    	initMenu();
    	_mainFrame.setValue(1, _method);
    	_mainFrame.setValue(2, _colorModel);
    }
    
    private void initMenu() {
    	menuBar = new JMenuBar();
    	
        menu1 = new JMenu("Mothod");
        menu1.setMnemonic(KeyEvent.VK_M);
        menuBar.add(menu1);
        
        menu2 = new JMenu("Color Model");
        menu2.setMnemonic(KeyEvent.VK_C);
        menuBar.add(menu2);
        
        // Menu: Method
        group1 = new ButtonGroup();
        GroupButtonListener1 groupButtonListener1 = new GroupButtonListener1();
        
        rbMenuItem1 = new JRadioButtonMenuItem("Global Pixel2Pixel Difference");
        rbMenuItem1.addActionListener(groupButtonListener1);
        rbMenuItem1.setSelected(true);
        rbMenuItem1.setMnemonic(KeyEvent.VK_G);
        group1.add(rbMenuItem1);
        menu1.add(rbMenuItem1);
        
        rbMenuItem2 = new JRadioButtonMenuItem("Cumulative Pixel2Pixel Difference");
        rbMenuItem2.addActionListener(groupButtonListener1);
        rbMenuItem2.setMnemonic(KeyEvent.VK_C);
        group1.add(rbMenuItem2);
        menu1.add(rbMenuItem2);
        
        rbMenuItem3 = new JRadioButtonMenuItem("Simple Histogram Difference");
        rbMenuItem3.addActionListener(groupButtonListener1);
        rbMenuItem3.setMnemonic(KeyEvent.VK_S);
        group1.add(rbMenuItem3);
        menu1.add(rbMenuItem3);
        
        rbMenuItem4 = new JRadioButtonMenuItem("Max Color Histogram Difference");
        rbMenuItem4.addActionListener(groupButtonListener1);
        rbMenuItem4.setMnemonic(KeyEvent.VK_M);
        group1.add(rbMenuItem4);
        menu1.add(rbMenuItem4);
        
        rbMenuItem5 = new JRadioButtonMenuItem("Weighted Color Histogram Difference");
        rbMenuItem5.addActionListener(groupButtonListener1);
        rbMenuItem5.setMnemonic(KeyEvent.VK_W);
        group1.add(rbMenuItem5);
        menu1.add(rbMenuItem5);
        
        // Menu: Color Model
        group2 = new ButtonGroup();
        GroupButtonListener2 groupButtonListener2 = new GroupButtonListener2();
        
        rbMenuItem6 = new JRadioButtonMenuItem("RGB");
        rbMenuItem6.addActionListener(groupButtonListener2);
        rbMenuItem6.setMnemonic(KeyEvent.VK_R);
        group2.add(rbMenuItem6);
        menu2.add(rbMenuItem6);
        
        rbMenuItem7 = new JRadioButtonMenuItem("YIQ");
        rbMenuItem7.addActionListener(groupButtonListener2);
        rbMenuItem7.setMnemonic(KeyEvent.VK_Y);
        rbMenuItem7.setSelected(true);
        group2.add(rbMenuItem7);
        menu2.add(rbMenuItem7);
        
        rbMenuItem8 = new JRadioButtonMenuItem("HSV");
        rbMenuItem8.addActionListener(groupButtonListener2);
        rbMenuItem8.setMnemonic(KeyEvent.VK_H);
        group2.add(rbMenuItem8);
        menu2.add(rbMenuItem8);
        
        rbMenuItem9 = new JRadioButtonMenuItem("CMY");
        rbMenuItem9.addActionListener(groupButtonListener2);
        rbMenuItem9.setMnemonic(KeyEvent.VK_C);
        group2.add(rbMenuItem9);
        menu2.add(rbMenuItem9);
        
        rbMenuItem10 = new JRadioButtonMenuItem("YUV");
        rbMenuItem10.addActionListener(groupButtonListener2);
        rbMenuItem10.setMnemonic(KeyEvent.VK_Y);
        group2.add(rbMenuItem10);
        menu2.add(rbMenuItem10);
    }
    
    public JMenuBar getMenuBar() {
        return menuBar;
    }

    class GroupButtonListener1 implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			for(Enumeration<AbstractButton> buttons = group1.getElements(); buttons.hasMoreElements();) {
				AbstractButton button = buttons.nextElement();
				
				if(button.isSelected()) {
					_method = button.getText();
					System.out.println(_method);
					_mainFrame.setValue(1, _method);
			    	_mainFrame.setValue(2, _colorModel);
				}
			}
		}
    }
    
    class GroupButtonListener2 implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			for(Enumeration<AbstractButton> buttons = group2.getElements(); buttons.hasMoreElements();) {
				AbstractButton button = buttons.nextElement();
				
				if(button.isSelected()) {
					_colorModel = button.getText();
					System.out.println(_colorModel);
					_mainFrame.setValue(1, _method);
			    	_mainFrame.setValue(2, _colorModel);
				}
			}
		}
    }
}
