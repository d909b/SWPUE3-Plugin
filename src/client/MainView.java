package client;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import plugin.Plugin;
import plugin.PluginManager;

public class MainView implements ActionListener {

	JFrame frame;
	JLabel picLabel;
	Object lastSelected;
	
	public static void printUsage() {
		System.out.println("Please specify a valid plugin directory as the first command line parameter.");
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		if( args.length > 0) {
			PluginManager pluginManager = PluginManager.getInstance();
		
			try {
				pluginManager.setPluginDirectoryPath(args[0]);
			} catch(IllegalArgumentException e) {
				printUsage();
				System.exit(1);
			}
			
			pluginManager.loadPlugins();
		} else {
			printUsage();
			System.exit(1);
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView window = new MainView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("resources/img.png"));
		} catch (IOException e) {
			System.err.println("Could not load image.");
		}
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0};
		
		frame.setLayout(gridBagLayout);
		
		picLabel = new JLabel(new ImageIcon( image ));
		
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(15, 10, 5, 5);
		gbc_label.anchor = GridBagConstraints.NORTH;
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		
		frame.add( picLabel, gbc_label );
		
		ArrayList<Plugin> plugins = PluginManager.getInstance().getPlugins();
		Object[] items = new Object[plugins.size() + 1];
		
		items[0] = "No filter";
		for(int i = 0; i < plugins.size(); ++i) {
			items[i + 1] = plugins.get(i).getName(); 
		}
		
		JComboBox comboBox = new JComboBox(items);
		comboBox.addActionListener(this);
		
		GridBagConstraints gbc_box = new GridBagConstraints();
		gbc_box.insets = new Insets(15, 10, 5, 5);
		gbc_box.anchor = GridBagConstraints.SOUTH;
		gbc_box.gridx = 1;
		gbc_box.gridy = 0;
		
		frame.add(comboBox);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	    if ("comboBoxChanged".equals(e.getActionCommand())) {
	        JComboBox cb = (JComboBox)e.getSource();
	
	        Object newSelected = cb.getSelectedItem();
	
	        if( !newSelected.equals(lastSelected) ) {
	    		BufferedImage image = null;
	    		try {
	    			image = ImageIO.read(new File("resources/img.png"));
	    		} catch (IOException e1) {
	    			System.err.println("Could not load image.");
	    		}
	    		
	    		BufferedImage image2 = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
	    		
	    		image2.getGraphics().drawImage(image, 0, 0, null);
	    		
	    		image = image2;
	    		
	    		if(!newSelected.equals("No filter")) {
		    		Graphics graphics = image.getGraphics();
		    		
		    		((Graphics2D)graphics).drawImage(image, 
		    				PluginManager.getInstance().getFilter((String)newSelected).getImageFilterOperation(),
		    				0, 0);
	    		}
	    		
	    		picLabel.setIcon( new ImageIcon(image) );
	        }
	        
	        lastSelected = newSelected;
        }
	}

}
