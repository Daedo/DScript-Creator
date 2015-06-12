package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.JScrollPane;

public class TextFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Create the frame.
	 */
	public TextFrame(String content) {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{434, 0};
		gridBagLayout.rowHeights = new int[]{262, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		getContentPane().add(scrollPane, gbc_scrollPane);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setText(content);
		
		/*JLabel lblText = new JLabel(content);
		lblText.setVerticalAlignment(SwingConstants.TOP);
		lblText.setHorizontalAlignment(SwingConstants.LEFT);

		// This text could be big so add a scroll pane
		JScrollPane scroller = new JScrollPane();
		scroller.getViewport().add(lblText);
		setContentPane(scroller);*/
	}
	
	public static TextFrame createFrameFromFile(String FileURI) {
		String text = "";
		try(BufferedReader br = new BufferedReader(new FileReader(FileURI))) {
			String line = br.readLine();

			while (line != null) {
				text+= line+"\n";
				line = br.readLine();
			}
			br.close();
			
		} catch (IOException e) {
			return null;
		}
		
		return new TextFrame(text);
	}
}

