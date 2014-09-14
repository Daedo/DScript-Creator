package gui;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JButton;

import build.DScriptCreator;
import net.miginfocom.swing.MigLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class MainGUI extends JFrame {
	private static final long serialVersionUID = 1L;
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
	
	private String getOpenFilename() {
		JFileChooser fileDialog = new JFileChooser("Select File to open");
		int choice = fileDialog.showOpenDialog(this);
		
		if(choice == JFileChooser.APPROVE_OPTION) {
			File openFile= fileDialog.getSelectedFile();
			return openFile.getAbsolutePath();
		}
		return "";
	}
	
	
	
	/**
	 * Opens a File and Loads it into the multi-line View
	 * 
	 * @param filePath Path to the File
	 */
	private String loadFile(String filePath) {
		try {
			FileReader openFile = new FileReader(filePath);
			BufferedReader in = new BufferedReader(openFile);
			
			String outtext = "";
			String line ="";
			
			boolean isEndOfFile = false;
			
			while(!isEndOfFile) {
				line = in.readLine();
				
				if(line != null) {
					outtext+= line+"\n";
				} else {
					isEndOfFile = true;
				}
			}
			
			in.close();
			return outtext;
			
		} catch (Exception e) {
			System.out.println("Reading Error: "+e.getMessage());
		}
		
		return null;
	}
	
	/**
	 * Saves the current text into a txt File
	 * 
	 * @param filePath Path to the File
	 */
	private static void saveFile(String filePath) {
		//TODO Save Method
	}
}
