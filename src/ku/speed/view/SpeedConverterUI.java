package ku.speed.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.xml.ws.WebServiceException;

import ku.speed.controller.SpeedConverterController;
import ku.speed.model.SpeedUnit;

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
	
	/** Label for show converting status. */
	private JLabel status;
	
	/** timer for label blink. */
	private Timer timerLB;
	
	/** SwingWorker */
	private SpeedConverterWorker work; 
	
	/** timer for check time out. */
	private Timer timerTO;
	
	/** timer for check KU login */
	private Timer timerKU;

	/**
	 * Constructor for this class.
	 */
	public SpeedConverterUI(){
		boolean builtController = false;
		
		while(!builtController){
			try {
				controller = new SpeedConverterController();
				builtController = true;
			} catch (WebServiceException e) {
				String[] option = {"Exit", "Retry"};
				int check = JOptionPane.showOptionDialog(null, 
														"No Internet Connection!", 
														"Retry or Exit?", 
														JOptionPane.YES_NO_OPTION, 
														JOptionPane.WARNING_MESSAGE, 
														null, option, 
														"Retry");
				if(check==1){
					continue;
				} else {
					System.exit(0);
				}
				
			
			}
		}
		
		initComponents();
		timerLB = new Timer(1000, new LbBlink(status));
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
		
		status = new JLabel("");
		status.setBounds(103, 41, 144, 20);
		
		button = new JButton("Convert!");
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				work = new SpeedConverterWorker(Double.parseDouble(input.getText()), (SpeedUnit)fromUnit.getSelectedItem(), (SpeedUnit)toUnit.getSelectedItem());
				work.execute();
				status.setForeground(java.awt.Color.black);
				status.setText("Coverting...");
				timerLB.start();
				
				timerTO = new Timer(11111, new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						timerTO.stop();
						work.cancel(true);
						JOptionPane.showMessageDialog(null, "Connection Time out!");
						input.setText("");
						output.setText("");
						status.setText("");
					}
				});
				
				timerKU = new Timer(5000, new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						timerKU.stop();
						work.cancel(true);
						JOptionPane.showMessageDialog(null, "Please Check Internet Access.");
						output.setText("");
						status.setText("");
						
					}
				});
				
				timerTO.start();
				timerKU.start();
			}

		});
		
		
		add(input);
		add(fromUnit);
		add(text);
		add(output);
		add(toUnit);
		add(button);
		add(status);
		
	}

	/**
	 * run method for GUI
	 */
	public void run(){
		pack();
		output.setEditable(false);
		setVisible(true);
	}
	
	class SpeedConverterWorker extends SwingWorker<String, String>{
		double convertedValue;
		String ans;
		
		double inputSpeed;
		SpeedUnit unit1;
		SpeedUnit unit2;
		
		public SpeedConverterWorker(double speed, SpeedUnit fromUnit, SpeedUnit toUnit){
			inputSpeed = speed;
			unit1 = fromUnit;
			unit2 = toUnit;
		}
		
		@Override
		protected String doInBackground() throws Exception {
			convertedValue = controller.convert(inputSpeed, unit1, unit2);
			return String.format("%.2f", convertedValue);
		}

		@Override
		protected void done() {
			try {
				output.setText(get());
				timerLB.stop();
				status.setForeground(java.awt.Color.red);
				status.setText("Done!   ");
				timerTO.stop();
				timerKU.stop();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch ( WebServiceException e ) {
				e.printStackTrace();
			}
			super.done();
		}
		
	}
}

class LbBlink implements ActionListener {  
    private javax.swing.JLabel label;
    private java.awt.Color cor1 = java.awt.Color.black;
    private java.awt.Color cor2 = java.awt.Color.red;
    private int count;

    public LbBlink(javax.swing.JLabel label){
        this.label = label;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(count % 2 == 0)
            label.setForeground(cor1);
        else
            label.setForeground(cor2);
        count++;
    }  
}
