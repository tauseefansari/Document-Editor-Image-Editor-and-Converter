package com.stark;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/***
 * 
 * @author Noman Naeem version 1.0
 */
public class MainFrame {

	private static int count = 1;

	public static void main(String[] args) {
		final JFrame frame = new JFrame("Closable Tabbedpane demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final CloseableTabbedPane tabbedPane = new CloseableTabbedPane();

		JButton button = new JButton("Add new tab!");
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		frame.getContentPane().add(button, BorderLayout.SOUTH);
		tabbedPane.addTab("New tab" + count, new JButton("New Tab " + count));

		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				++count;
				tabbedPane.addTab("New tab" + count, new JButton("New Tab "
						+ count));
			}
		});
		frame.setSize(300, 200);
		frame.setVisible(true);
	}
}
