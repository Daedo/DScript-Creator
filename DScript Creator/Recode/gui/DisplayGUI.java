package gui;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

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
	private JPanel contentPane;

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
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(null);
		addWords(words);
	}

	void addWords(Vector<Glyph> words) {
		SVGDocument doc = DocumentBuilder.buildWords(words);
		JSVGCanvas canvas = new JSVGCanvas();
		canvas.setDocument(doc);
		canvas.setBounds(10,10, 2000, 2000);
		canvas.setBackground(new Color(255, 255, 255, 0));
		JSVGScrollPane scroll = new JSVGScrollPane(canvas);
		setContentPane(scroll);
		saveAsPNG("test.png");
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
}
