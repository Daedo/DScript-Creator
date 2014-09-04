package gui;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JButton;

import build.DScriptCreator;
import net.miginfocom.swing.MigLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainGUI extends JFrame {

	private JPanel contentPane;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI frame = new MainGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(1, 1, 1, 1));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[100%]", "[75%][:25%:25%,bottom]"));
		
		JPanel ScriptPanel = new JPanel();
		contentPane.add(ScriptPanel, "cell 0 0,grow");
		
		JPanel InputPanel = new JPanel();
		contentPane.add(InputPanel, "cell 0 1,growx,aligny bottom");
		InputPanel.setLayout(new MigLayout("", "[100%][80px]", "[100%]"));
		
		JButton btnTranslate = new JButton("Translate");
		InputPanel.add(btnTranslate, "cell 1 0,alignx right,aligny center");
		
		textArea = new JTextArea();
		InputPanel.add(textArea, "cell 0 0,grow");
		btnTranslate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String areaText = textArea.getText();
				//System.out.println(areaText);
				parseText(areaText);
			}
		});
		
		btnTranslate.setPreferredSize( new Dimension(60, 20));
	}

	public static void parseText(String text) {
		
		String[] lines = text.split("\\n");
		
		for(String line:lines) {
			System.out.println("Line: ");
			DScriptCreator.runParser(line);
			System.out.println("");
		}
	}
}
