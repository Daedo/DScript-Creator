package gui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class TextFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Create the frame.
	 */
	public TextFrame(String FileURI) {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		String text = "";

		try(BufferedReader br = new BufferedReader(new FileReader(FileURI))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				text+= line+"\n";
				line = br.readLine();
			}
			text = sb.toString();
			br.close();
			
		} catch (IOException e) {
			this.dispose();
		}

		JLabel lblText = new JLabel(text);
		lblText.setVerticalAlignment(SwingConstants.TOP);
		lblText.setHorizontalAlignment(SwingConstants.LEFT);

		// This text could be big so add a scroll pane
		JScrollPane scroller = new JScrollPane();
		scroller.getViewport().add(lblText);
		setContentPane(scroller);
	}
}

