package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTabbedPane;

import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import javax.swing.JTextField;

import builder.BuildingException;
import builder.DScriptBuilder;
import builder.SVGExporter;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.table.DefaultTableModel;
import javax.xml.transform.TransformerException;

import org.w3c.dom.svg.SVGDocument;

import scriptRepräsentation.DScriptText;
import scriptRepräsentation.Wordstart;
import scriptRepräsentation.ParseException;
import scriptRepräsentation.Parser;

import java.util.Vector;

public class InputGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField widthTextField;
	private JTextField heightTextField;
	private JTable table;
	JTextArea textArea;

	private DefaultTableModel  model;
	//private GUIData data;
	DScriptText dText;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InputGUI frame = new InputGUI();
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
	public InputGUI() {
		//this.data = new GUIData();
		this.dText = new DScriptText(0, 0);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		ChangeListener changeListener = new ChangeListener() {
			public void stateChanged(ChangeEvent changeEvent) {
				tabChange(changeEvent);
			}
		};
		tabbedPane.addChangeListener(changeListener);
		this.contentPane.add(tabbedPane);

		JPanel TextEditPanel = new JPanel();
		tabbedPane.addTab("Text", null, TextEditPanel, null);
		TextEditPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("22px"),}));

		JScrollPane scrollPane = new JScrollPane();
		TextEditPanel.add(scrollPane, "1, 2, fill, fill");

		this.textArea = new JTextArea();
		scrollPane.setViewportView(this.textArea);
		this.textArea.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				//System.out.println(arg0.getType());
				deleteUpdate(arg0);
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				//System.out.println(arg0.getType());
				addUpdate(arg0);
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				//No Need for this
				//System.out.println(arg0.getType());
			}
		});

		Button generateButton = new Button("Generate");
		generateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				parseDScript(InputGUI.this.textArea.getText());
				buildDScript(InputGUI.this.dText);
			}
		});
		TextEditPanel.add(generateButton, "1, 4, center, top");

		JPanel OptionsPanel = new JPanel();
		tabbedPane.addTab("Options", null, OptionsPanel, null);
		OptionsPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("default:grow"),}));

		JLabel lblWidth = new JLabel("Width");
		OptionsPanel.add(lblWidth, "1, 1, right, center");

		this.widthTextField = new JTextField();
		this.widthTextField.setText("640");
		OptionsPanel.add(this.widthTextField, "2, 1, center, top");
		this.widthTextField.setColumns(10);

		JLabel lblHeight = new JLabel("Height");
		OptionsPanel.add(lblHeight, "3, 1, right, center");

		this.heightTextField = new JTextField();
		this.heightTextField.setText("480");
		OptionsPanel.add(this.heightTextField, "4, 1, center, top");
		this.heightTextField.setColumns(10);

		JScrollPane scrollPane_1 = new JScrollPane();
		OptionsPanel.add(scrollPane_1, "1, 3, 4, 1, fill, fill");

		this.table = new JTable();
		this.model = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Word", "X Position", "Y Position", "Width", "Height"
				}

				) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			Class[] columnTypes = new Class[] {
					String.class, Double.class, Double.class, Double.class, Double.class
			};
			public Class getColumnClass(int columnIndex) {
				return this.columnTypes[columnIndex];
			};

			boolean[] canEdit = new boolean[]{
					false,true,true,true,true
			};

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return this.canEdit[columnIndex];
			}
		};
		this.table.setModel(this.model);
		this.table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

		scrollPane_1.setViewportView(this.table);
	}
	
	public void addUpdate(DocumentEvent event) {
	}

	public void deleteUpdate(DocumentEvent event) {
	}

	public void tabChange(ChangeEvent changeEvent) {
		JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
		int index = sourceTabbedPane.getSelectedIndex();
		if(index==0) {
			//Store Table
			storeData();
		} else if(index==1){
			//Reparse DScript
			parseDScript(this.textArea.getText());
			updateTable();
		}
		
		System.out.println("Tab changed to: " + sourceTabbedPane.getTitleAt(index));
	}
	
	public void parseDScript(String text) {
		Parser parser = new Parser();
		System.out.println("Text: "+text);
		try {
			DScriptText newText = parser.parseDScript(text);
			this.dText.update(newText);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateTable() {
		this.model.setNumRows(0);
		
		for(Wordstart word: this.dText.words) {
			Object[] rowData = {word.wordText,
					new Double(word.x),new Double(word.y),
					new Double(word.width), new Double(word.height)};

			this.model.addRow(rowData);
		}
	}

	public void storeData() {
		storeTable();
		
		if(this.heightTextField== null || this.widthTextField==null) {
			return;
		}
		
		if(this.heightTextField.getText()!=null) {
		this.dText.height= Double.parseDouble(this.heightTextField.getText());
		}
		
		if(this.widthTextField.getText()!=null) {
			this.dText.width = Double.parseDouble(this.widthTextField.getText());
		}
	}
	
	private void storeTable() {
		if(this.model==null) {
			return;
		}
		
		Vector<Vector<Object>> dataVec = this.model.getDataVector();
		for(int i=0;i<dataVec.size();i++) {
			Vector<Object> row = dataVec.get(i);
			
			Wordstart dataWord = this.dText.words.get(i);
			dataWord.x 		= ((Double)row.get(1)).doubleValue();
			dataWord.y 		= ((Double)row.get(2)).doubleValue();
			dataWord.width 	= ((Double)row.get(3)).doubleValue();
			dataWord.height = ((Double)row.get(4)).doubleValue();
			
			for(Object column: row) {
				System.out.println(column.toString());
			}
			System.out.println("----");
		}
	}

	public void buildDScript(DScriptText text) {
		DScriptBuilder build = new DScriptBuilder();
		
		try {
			SVGDocument doc = build.buildDScript(text);
			SVGExporter.exportSVG(doc, "test.svg");
		} catch (BuildingException | FileNotFoundException | UnsupportedEncodingException | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
