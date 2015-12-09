package slot_machine;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import lobby.*;

/**
 * This class will show the GUI view for the slot game
 * 
 * @author zhiyuanli
 *
 */
public class SlotView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3448807048334175081L;

	// instance variables
	private Slot slot;
	private int finalPayout;

	// GUI variables
	private JPanel top, left, right, bottom, center;
	private JPanel subLeft, subCenter, subRight;
	private JButton spin, back;
	private JLabel[] reels;
	private ArrayList<ImageIcon> images;
	private Timer[] timer, spinner;

	/**
	 * constructor
	 */
	public SlotView() {
		slot = new Slot(3);
		images = new ArrayList<ImageIcon>();
		reels = new JLabel[3];
		timer = new Timer[3];
		spinner = new Timer[3];
		finalPayout = 0;
		
		display();
	}

	/**
	 * helper method to construct the slot view
	 */
	private void display() {
		setTitle("Slot");
		layOutComponents();
		attachListenersToComponents();
		setPreferredSize(new Dimension(1200, 400));
		setLocation();
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * set the location of window
	 */
	private void setLocation() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() / 2 - this.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() / 2 - this.getHeight()) / 2);
		setLocation(x, y);
	}

	/**
	 * helper method to set the layout
	 */
	private void layOutComponents() {
		setLayout(new BorderLayout());
		addPanels();
//		addButtons();
		addImage();
	}

	/**
	 * helper method to add 5 panels
	 */
	private void addPanels() {
		top = new JPanel();
		left = new JPanel();
		center = new JPanel();
		right = new JPanel();
		bottom = new JPanel();

		add(top, BorderLayout.NORTH);
		add(left, BorderLayout.WEST);
		add(center, BorderLayout.CENTER);

		add(right, BorderLayout.EAST);
		add(bottom, BorderLayout.SOUTH);
		addSubPanels();
	}

	/**
	 * helper method to add 5 sub-panels within the center panel
	 */
	private void addSubPanels() {
		// add 3 sub panels in center
		center.setLayout(new GridLayout(1, 3));
		subLeft = new JPanel();
		subLeft.setPreferredSize(new Dimension(300, 300));
		subCenter = new JPanel();
		subCenter.setPreferredSize(new Dimension(300, 300));
		subRight = new JPanel();
		subRight.setPreferredSize(new Dimension(300, 300));

		center.add(subLeft);
		center.add(subCenter);
		center.add(subRight);

		bottom.setLayout(new GridLayout(1, 3));

		// add "Spin" button
		bottom.add(new JLabel());
		spin = new JButton("Spin");
		bottom.add(spin);
		back = new JButton("Back");
		bottom.add(back);
		
	}

	/**
	 * helper method to add buttons
	 */
//	private void addButtons() {
//
//	}

	/**
	 * helper method to load image
	 */
	private ImageIcon loadImage(String imageName) {

		// adjust image size and add to view
		ImageIcon imageIcon = new ImageIcon(getClass().getResource(imageName));
		Image imageImage = imageIcon.getImage().getScaledInstance(250, 250, java.awt.Image.SCALE_SMOOTH);
		ImageIcon image = new ImageIcon(imageImage);
		return image;
	}

	/**
	 * helper method to transfer symbol to image name
	 * 
	 * @param order
	 * @return image name
	 */
	private String orderToImageName(int order) {
		switch (order) {
		case 0:
			return "bar.jpg";
		case 1:
			return "bell.jpg";
		case 2:
			return "cherry.jpg";
		case 3:
			return "grape.jpg";
		case 4:
			return "lemon.jpg";
		case 5:
			return "seven.jpg";
		case 6:
			return "watermelon.jpg";
		default:
			return "" + order;
		}
	}

	/**
	 * helper method to add image to the view
	 */
	private void addImage() {

		reels[0] = new JLabel(loadImage("unkown.jpg"));
		reels[1] = new JLabel(loadImage("unkown.jpg"));
		reels[2] = new JLabel(loadImage("unkown.jpg"));

		subLeft.add(reels[0]);
		subCenter.add(reels[1]);
		subRight.add(reels[2]);

		for (int i = 0; i < SlotSymbol.values().length; i++) {
			images.add(loadImage(orderToImageName(SlotSymbol.values()[i].getOrder())));
		}

	}

	/**
	 * attach action listeners to buttons
	 */
	private void attachListenersToComponents() {

		// add listener to spin
		spin.addActionListener(new SpinEvent());
		back.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new Lobby().setVisible(true);
			}
			
		});
		
	}

	/**
	 * inner class for spinning
	 */
	private class SpinEvent implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			slot.spin();
			finalPayout = slot.getPayout(1);

			timer[0] = new Timer(1000, new TimerEvent(0));
			timer[0].start();

			timer[1] = new Timer(2000, new TimerEvent(1));
			timer[1].start();
			
			timer[2] = new Timer(3000, new TimerEvent(2));
			timer[2].start();
			
			spinner[0] = new Timer(100, new SpinnerEvent(0));
			spinner[0].start();
			
			spinner[1] = new Timer(100, new SpinnerEvent(1));
			spinner[1].start();
			
			spinner[2] = new Timer(100, new SpinnerEvent(2));
			spinner[2].start();
		}

	}
	
	/**
	 * inner class to represent a timer event
	 */
	private class TimerEvent implements ActionListener {
		
		private int i;
		
		/**
		 * constructor
		 * @param i
		 */
		public TimerEvent(int i) {
			this.i = i;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
//			spinReelImage(i, slot.getSymbols()[i].getOrder());
			spinner[i].stop();
			timer[i].stop();
			
			if (i == 2) {
				JOptionPane.showMessageDialog(getParent(), "You get $" + finalPayout + "\n");
				clearReelImage();
			}
		}
		
	}

	/**
	 * helper method to change displayed image when reel spins
	 */
	private void spinReelImage(int i, int order) {
		// for (int i = 0; i < SlotSymbol.values().length + order; i++) {

		// reel.setIcon(images.get(i%SlotSymbol.values().length));
		// }
		spinner[i] = new Timer(100, new SpinnerEvent(i));
		spinner[i].start();
//		reels[i].setIcon(images.get(order));
	}
	
	/**
	 * inner class to represent a spinner event
	 */
	private class SpinnerEvent implements ActionListener {
		
		private int i;
		
		/**
		 * constructor
		 * @param i
		 */
		public SpinnerEvent(int i) {
			this.i = i;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			reels[i].setIcon(images.get(new Random().nextInt(7)));
		}
		
	}

	/**
	 * helper method to reset images
	 */
	private void clearReelImage() {
		reels[0].setIcon(loadImage("unkown.jpg"));
		reels[1].setIcon(loadImage("unkown.jpg"));
		reels[2].setIcon(loadImage("unkown.jpg"));
	}

}
