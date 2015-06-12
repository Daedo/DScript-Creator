package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.JSVGScrollPane;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.w3c.dom.Document;
import org.w3c.dom.svg.SVGDocument;

import textparser.Glyph;
import files.DocumentBuilder;

public class DisplayGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Create the frame.
	 * @param glyph 
	 */
	public DisplayGUI(String input, Vector<Glyph> words) {
		if(input==null || words==null) {
			this.dispose();
			return;
		}

		System.out.println(input);

		String title = "";
		if(input.length()<=10) {
			title = input;
		} else {
			title = input.substring(0,10)+"...";
		}

		this.setTitle(title);
		this.setVisible(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		addWords(words);
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmSaveAsPng = new JMenuItem("Save as PNG");
		mntmSaveAsPng.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String URI = getSaveURI(new FileNameExtensionFilter("Picture", "png"));
				saveAsPNG(URI);
			}
		});
		mnFile.add(mntmSaveAsPng);
		
	}

	void addWords(Vector<Glyph> words) {
		SVGDocument doc = DocumentBuilder.buildWords(words);
		/*
		 * TODO Generate InfoGUI for words 
		 */
		
		JSVGCanvas canvas = new JSVGCanvas();
		canvas.setDocument(doc);
		Dimension docDim = DocumentBuilder.getSVGSize(doc);
		
		canvas.setBounds(10,10, docDim.width, docDim.height);
		canvas.setBackground(new Color(255, 255, 255, 0));
		JSVGScrollPane scroll = new JSVGScrollPane(canvas);
		setContentPane(scroll);
		//saveAsPNG("test.png");
	}

	public void saveAsPNG(String URI) {
		try (OutputStream png_ostream = new FileOutputStream(URI)){
			JSVGScrollPane scroll = (JSVGScrollPane) getContentPane();
			JSVGCanvas canvas = scroll.getCanvas();
			Document currentDoc = canvas.getSVGDocument();
			TranscoderInput in = new TranscoderInput(currentDoc);
			TranscoderOutput output_png_image = new TranscoderOutput(png_ostream);              
			// Step-3: Create PNGTranscoder and define hints if required
			PNGTranscoder my_converter = new PNGTranscoder();        
			// Step-4: Convert and Write output
			my_converter.transcode(in, output_png_image);
			// Step 5- close / flush Output Stream
			png_ostream.flush();
			png_ostream.close();
		} catch (TranscoderException | IOException e) {
			System.err.println("Couldn't save picture because an error occured");
		} 
	}

	public static String getSaveURI(FileFilter filter) {
		JFileChooser fileChooser = new JFileChooser();

		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setFileFilter(filter);
		fileChooser.setFileHidingEnabled(true);
		fileChooser.setAcceptAllFileFilterUsed(false);

		int button = fileChooser.showSaveDialog(null);
		if(button == JFileChooser.APPROVE_OPTION) {
			String path = fileChooser.getSelectedFile().toString();

			//IF the User hasn't selected a File, create a default filename
			if(fileChooser.getSelectedFile().isDirectory()) {
				DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy HH_mm_ss");
				path+="/savefile "+dateFormat.format(new Date())+".save";
			}

			return path;
		}
		return null;
	}
}
