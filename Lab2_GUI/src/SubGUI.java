
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SubGUI extends JDialog {
    
    private Book book;
    private JButton borrow = new JButton("Borrow");
    private JButton returnbook = new JButton("Return");
    private JButton reserve = new JButton("Reserve");
    private JButton waiting = new JButton("Waiting Queue");
    private JTextArea bookdata = new JTextArea();
    private JTextArea message = new JTextArea();
    private JPanel framepanel = new JPanel();
    private JPanel panel = new JPanel();

    public SubGUI(Book bk) {
        book = bk;
        setModal(true);
        refresh();                     
        framepanel.setLayout(new BorderLayout(5,5));
        bookdata.setEditable(false);
        framepanel.add(bookdata, BorderLayout.NORTH);
        panel.add(borrow);
        panel.add(returnbook);
        panel.add(reserve);
        panel.add(waiting);
        framepanel.add(panel, BorderLayout.CENTER);
        message.setEditable(false);
        framepanel.add(message, BorderLayout.SOUTH);      
        add(framepanel);
        this.setTitle(book.getTitle());
        this.setSize(600, 500);
        this.setLocationRelativeTo(null); // Center the frame 
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        ButtonListener listener = new ButtonListener();
        borrow.addActionListener(listener);
        returnbook.addActionListener(listener);
        reserve.addActionListener(listener);
        waiting.addActionListener(listener);
    }
   
    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {  
            if (e.getSource()==borrow) {
            	book.setAvailable(false);
                message.setText("This book is borrowed.");
            	refresh();
            } else if (e.getSource()==returnbook) {
            	returnbook();	
            } else if (e.getSource()==reserve) {
            	reserve();
            } else if (e.getSource()==waiting) {
            	message.setText(book.getWaitingQueueMsg());
            }
        }
    }
    /*If no user in the queue, set available to true. otherwise book is borrowed by others */
    private void returnbook(){
    	String message1 = "This book is returned.\n";
    	if(book.getReservedQueue().getSize() == 0) {
    		book.setAvailable(true);
    	} else {
    		message1 += "This book is borrowed by " + book.getReservedQueue().dequeue() + ".";
    	}
    	message.setText(message1);
    	refresh();
    }
         
    /*get user input and save into queue*/
    private void reserve() {
    	String name = JOptionPane.showInputDialog("Please enter your?");
    	book.getReservedQueue().enqueue(name);
    	message.setText("This book is reserved by the following user "  + name + ".");
    }
    
    private void refresh() {
        String info = "ISBN: " + book.getISBN() + "\nTitle: " + book.getTitle() + "\nAvailable: " + book.getAvailable() +"\n";
        bookdata.setText(info);
        boolean available = book.isAvailable();
        borrow.setEnabled(available);
        returnbook.setEnabled(!available);
        reserve.setEnabled(!available);
        waiting.setEnabled(!available);
    }
     
}