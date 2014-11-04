package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.webservicex.SpeedUnit;
import controller.SpeedConverterController;

/**
 * SpeedConverter class is user interface for SpeedConverter.
 * 
 * @author Latthapat Tangtrustham 5510547014
 * @version 2014/11/04
 *
 */
public class SpeedConverterUI extends JFrame {
	
	/** Converter controller. */
	private SpeedConverterController controller;
	
	/** TextField for input speed value. */
	private JTextField input;
	/** TextField for output speed value. */
	private JTextField output;
	
	/** Label for " >> " */
	private JLabel text;
	
	/** ComboBox for select input unit. */
	private JComboBox fromUnit;
	/** ComboBox for select output unit. */
	private JComboBox toUnit;
	
	/** Button to start convert speed value. */
	private JButton button;

	/**
	 * Constructor for this class.
	 */
	public SpeedConverterUI(){
		controller = new SpeedConverterController();
		initComponents();
	}
	
	/**
	 * initialize method
	 */
	private void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Speed Converter 1.0");
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		
		input = new JTextField();
		input.setBounds(103, 41, 144, 20);
		input.setColumns(10);
		output = new JTextField();
		output.setBounds(103, 41, 144, 20);
		output.setColumns(10);
		
		text = new JLabel(" >> ");
		text.setBounds(103, 41, 144, 20);
		
		fromUnit = new JComboBox(SpeedUnit.values());
		fromUnit.setBounds(113, 76, 107, 20);
		toUnit = new JComboBox(SpeedUnit.values());
		toUnit.setBounds(113, 76, 107, 20);
		
		button = new JButton("Convert!");
		button.addActionListener(new ActionListener() {
			double convertedValue;
			String ans;
			@Override
			public void actionPerformed(ActionEvent e) {
				convertedValue = controller.convert( Double.parseDouble(input.getText()), (SpeedUnit)fromUnit.getSelectedItem(), (SpeedUnit)toUnit.getSelectedItem());
				ans = String.format("%.2f", convertedValue);
				output.setText(ans);
			}

		});
		
		add(input);
		add(fromUnit);
		add(text);
		add(output);
		add(toUnit);
		add(button);
		
	}

	/**
	 * run method for GUI
	 */
	public void run(){
		pack();
		output.setEditable(false);
		setVisible(true);
	}
}
