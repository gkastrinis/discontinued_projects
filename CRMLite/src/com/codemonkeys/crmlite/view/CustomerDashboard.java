package com.codemonkeys.crmlite.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.codemonkeys.crmlite.model.Customer;

public class CustomerDashboard extends JPanel {

	private static final long serialVersionUID = 7796630258005124655L;
	
	Customer customer;

	public CustomerDashboard(int cID) {
		super(new GridBagLayout());
		customer = new Customer(cID);
		GridBagConstraints c = new GridBagConstraints();

		setBackground(Color.WHITE);

		JPanel p = infoPane();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0 / 4.0;
		c.weighty = 1;
		c.insets = new Insets(10, 10, 10, 10);
		add(p, c);
		
		JButton t2 = new JButton("Testing button2");
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 3.0 / 4.0;
		add(t2, c);
		
	}
	
	JPanel infoPane() {
		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
	
		p.setBackground( customer.sex.equals("M") ? new Color(112, 142, 222) : new Color(222, 142, 192) );
		Font font = new Font("Sans Serif", Font.PLAIN, 16);
		Font fontB = font.deriveFont(Font.BOLD);
		
		JLabel fullName = new JLabel(customer.firstName + " "
				+ (customer.middleName != null ? customer.middleName + " " : "")
				+ customer.lastName);
		fullName.setFont(fontB);
		

		c.fill = GridBagConstraints.HORIZONTAL;
//		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.ipadx = 0;
		c.insets = new Insets(0,0,0,0);
		
		//c.gridwidth = 2;
		JPanel t = new JPanel();
		t.setBackground(Color.RED);
		p.add(t, c);

		c.gridy = 1;
		p.add(fullName, c);
		//c.gridwidth = 2;
/*
		c.gridy++;

		JLabel ageL = new JLabel("age");
		ageL.setFont(font);
		ageL.setHorizontalAlignment(JLabel.RIGHT);
		c.gridx = 0;
		//p.add(ageL, c);

		JLabel age = new JLabel("" + customer.age);
		age.setFont(fontB);
		age.setHorizontalAlignment(JLabel.LEFT);
		c.gridx = 1;
		//p.add(age, c);
		
		c.gridy++;
		
		JLabel addressL = new JLabel("address");
		addressL.setFont(font);
		addressL.setHorizontalAlignment(JLabel.RIGHT);
		c.gridx = 0;
		//p.add(addressL, c);

		JTextArea address = new JTextArea("" + customer.address);
		address.setFont(fontB);
		address.setColumns(15);
		address.setLineWrap(true);
		c.gridx = 1;
		//p.add(address, c);
		
		c.gridy++;
		
		JLabel phoneL = new JLabel("phone");
		phoneL.setFont(font);
		phoneL.setHorizontalAlignment(JLabel.RIGHT);
		c.gridx = 0;
		//p.add(phoneL, c);
		
		JLabel phone = new JLabel("" + customer.phone);
		phone.setFont(fontB);
		c.gridx = 1;
		//p.add(phone, c);
	*/	
		return p;
	}
}
