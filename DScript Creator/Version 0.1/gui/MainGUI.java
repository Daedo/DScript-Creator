package gui;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JButton;

import build.OldDScriptBuilder;
import build.DScriptCreator;
import net.miginfocom.swing.MigLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import scriptparser.DScriptLine;
import scriptparser.DScriptText;

public class MainGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JTextArea textArea;
	String documentPath;	//Path to the currently opened file
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		//Debug Code
		/*Database DB = new Database("LigatureDatabase.db");
		DB.connect();
		DB.getText(1);
		DB.getFileURI(1);
		DB.getTextID(1);
		DB.getLigatureID(1, "B");
		DB.disconnect();*/
		/*
		try {
			DSVGParser.parseDSVGFile("C:/Users/Dominik/Desktop/Programmieren/Java/DScript/DSVG DEMO FILE.dsvg",new DScriptFlag());
			//DSVGParser.parseDSVGFile("C:/Users/Dominik/Desktop/Programmieren/Java/git/DScript Creator/DScript Creator/Ligatures/Basic/a1.dsvg",0);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		
		//Main Code
		EventQueue.invokeLater(new Runnable() {
			@Override
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
		
		this.documentPath = "";
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 480);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				//TODO check for unsafed changes
				
				System.out.println("New");
				
				MainGUI.this.textArea.setText("");
				
			}
		});
		mnFile.add(mntmNew);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//TODO check for unsafed changes
				
				System.out.println("Open");
				
				String path = getOpenFilename();
				String contend = loadFile(path);
				MainGUI.this.textArea.setText(contend);
				MainGUI.this.documentPath = path;
			}
		});
		mnFile.add(mntmOpen);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				System.out.println("Save");
				if(MainGUI.this.documentPath!="") {
					saveFile(MainGUI.this.documentPath);
				} else {
					String path = getSaveFilename();
					saveFile(path);
					MainGUI.this.documentPath = path;
				}
			}
		});
		mnFile.add(mntmSave);
		
		JMenuItem mntmSaveAs = new JMenuItem("Save As...");
		mntmSaveAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				System.out.println("Save As...");
				String path = getSaveFilename();
				saveFile(path);
				MainGUI.this.documentPath = path;
			}
		});
		mnFile.add(mntmSaveAs);
		
		
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(1, 1, 1, 1));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(new MigLayout("", "[100%]", "[75%][:25%:25%,bottom]"));
		
		JPanel ScriptPanel = new JPanel();
		this.contentPane.add(ScriptPanel, "cell 0 0,grow");
		
		JPanel InputPanel = new JPanel();
		this.contentPane.add(InputPanel, "cell 0 1,growx,aligny bottom");
		InputPanel.setLayout(new MigLayout("", "[100%][80px]", "[100%]"));
		
		this.textArea = new JTextArea();
		InputPanel.add(this.textArea, "cell 0 0,grow");
		
		JButton btnTranslate = new JButton("Translate");
		InputPanel.add(btnTranslate, "cell 1 0,alignx right,aligny center");
		btnTranslate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				translateText();
			}
		});
		
		btnTranslate.setPreferredSize( new Dimension(60, 20));
	}

	void translateText() {
		String areaText = this.textArea.getText();
		//System.out.println(areaText);
		DScriptText parsedText = parseText(areaText);
		//parsedText.debugText();
		
		int lines = parsedText.getLinecount();
		for(int i=0;i<lines;i++) {
			DScriptLine currentLine = parsedText.getLine(i);
			
			int wordcount = currentLine.getWordcount();
			for(int j=0;j<wordcount;j++) {
				OldDScriptBuilder.buildWord(currentLine.getWord(j));
			}
		}
		
	}
	
	/**
	 * Parses a given String and returns a {@link DScriptText} instance
	 * 
	 * @param text The String, that is to parse
	 * @return Returns a {@link DScriptText} instance
	 */
	public static DScriptText parseText(String text) {
		
		String[] lines = text.split("\\n");
		DScriptText parsedText = new DScriptText();
		
		for(String line:lines) {
			DScriptLine parsedLine = DScriptCreator.runParser(line);
			parsedText.addLine(parsedLine);
		}
		
		return parsedText;
	}
	
	String getOpenFilename() {
		JFileChooser fileDialog = new JFileChooser("Select File to open");
		int choice = fileDialog.showOpenDialog(this);
		
		if(choice == JFileChooser.APPROVE_OPTION) {
			File openFile= fileDialog.getSelectedFile();
			return openFile.getAbsolutePath();
		}
		return "";
	}
	
	String getSaveFilename() {
		JFileChooser fileDialog = new JFileChooser("Select File to open");
		int choice = fileDialog.showSaveDialog(this);
		
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
	static String loadFile(String filePath) {
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
	void saveFile(String filePath) {
		try {
			File file = new File(filePath);
			if(!file.exists()) {
				file.createNewFile();
			} else {
				int choice = JOptionPane.showConfirmDialog(this, "File already exists. Overwrite it?");
				if(choice != JOptionPane.OK_OPTION) {
					return;
				}
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(this.textArea.getText());
			bw.close();
		}
		catch(Exception e) {
			System.out.println("Error while writing: "+e.getMessage());
		}
	}
}
