import java.awt.BorderLayout;

import javax.swing.*;
public class GUI extends JFrame{
	
	JPanel frameHolder = new JPanel();
	
	//Adding interactive layer to the UI of library admin system
	public GUI() {
		frameHolder.setLayout(new BorderLayout(5,5));
		frameHolder.add(new MainGUI(this), BorderLayout.CENTER);
		add(frameHolder);
	}

	public static void main(String[] args) {
		GUI frame = new GUI();
		frame.setTitle("Library Admin System");
		frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);	//Center the frame 
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);		//So that the action of closing frame can be reversed
        frame.setVisible(true);
	}
	
}