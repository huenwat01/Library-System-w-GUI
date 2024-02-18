
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainGUI extends JPanel {
	
		//Compnent for UI
		private String names = "Student Name and ID:  \n" + "Student Name and ID: ";	
		private final String[] col_header = { "ISBN", "Title", "Available" };
		private TableSetting tableModel = new TableSetting(col_header);
		private JTable jtable = new JTable(tableModel);
		private JScrollPane ScrollPane = new JScrollPane(jtable);		
		private JButton add = new JButton("Add");
		private JButton edit = new JButton("Edit");
		private JButton save = new JButton("Save");
		private JButton delete = new JButton("Delete");
		private JButton search = new JButton("Search");
		private JButton more = new JButton("More>>");
		private JButton load = new JButton("Load Test Data");
		private JButton display = new JButton("Display All");
		private JButton displayByISBN = new JButton("Display All by ISBN");
		private JButton displayByTitle = new JButton("Display All by Title");
		private JButton exit = new JButton("Exit");
		private JLabel isbnL = new JLabel("ISBN:");
		private JLabel titleL = new JLabel("Title:");
		private JTextField ipTitle = new JTextField();
		private JTextField ipISBN = new JTextField();
		private SimpleDateFormat dateformat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
		private JTextArea time = new JTextArea(dateformat.format(new Date(System.currentTimeMillis())));
		private JTextArea nameText = new JTextArea(names);		
		private JPanel btnPanel = new JPanel();
		private JPanel btnPanel2 = new JPanel();
		private JPanel inputPanel = new JPanel();
		private JPanel textarea = new JPanel();
		private JPanel subButton = new JPanel();
		private JPanel subTable = new JPanel();
		private JFrame thisFrame;
		
		//Book list for the Library system
		MyLinkedList<Book> bookLinkedList = readdata();
		
		/*AO means ascending order, 1 = ascending order    0 = descending order */
		int ISBN_AO = 1;
	    int Title_AO = 1;	
	    int editIndex = -1;		//Saving the index of row in the table 
	    int size = 0;
	    int index = 0;
	    
		public static void main(String[] args) {
			JFrame mainframe = new JFrame();
			mainframe.add(new MainGUI(mainframe), BorderLayout.CENTER);
			mainframe.setTitle("Library Admin System");
			mainframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			mainframe.setLocationRelativeTo(null);		
			mainframe.setSize(700, 500);
			mainframe.setVisible(true);				
		}
		
		public MainGUI(JFrame thisFrame) {
			
			//Saving the frame for later use
			this.thisFrame = thisFrame;

			//Load saved data into the system, and display it
			while (tableModel.getRowCount() > 0) {
				tableModel.removeRow(0);
			}
			int i=0;
			while(i < bookLinkedList.size()) {
				Book bk = bookLinkedList.get(i);
				tableModel.addRow(new String[] { bk.getISBN(), bk.getTitle(), bk.getAvailable() });
				i++;
			}
			
			ipISBN.setPreferredSize(new Dimension(200, 20));
			ipTitle.setPreferredSize(new Dimension(200, 20));
						
			time.setEditable(false);
			nameText.setEditable(false);

			//Set listener for the buttons
			ButtonListener listener = new ButtonListener();
			add.addActionListener(listener);
			edit.addActionListener(listener);
			save.addActionListener(listener);
			delete.addActionListener(listener);
			search.addActionListener(listener);
			more.addActionListener(listener);
			load.addActionListener(listener);
			display.addActionListener(listener);
			displayByISBN.addActionListener(listener);
			displayByTitle.addActionListener(listener);
			exit.addActionListener(listener);

			btnPanel.add(add);
			btnPanel.add(edit);
			btnPanel.add(save);
			btnPanel.add(delete);
			btnPanel.add(search);
			btnPanel.add(more);		
			btnPanel2.add(load);
			btnPanel2.add(display);
			btnPanel2.add(displayByISBN);
			btnPanel2.add(displayByTitle);
			btnPanel2.add(exit);	
			inputPanel.add(isbnL);
			inputPanel.add(ipISBN);
			inputPanel.add(titleL);
			inputPanel.add(ipTitle);

			save.setEnabled(false);

			//Combine the panels for display
			textarea.setLayout(new GridLayout(0,1));
			textarea.add(nameText);
			textarea.add(time);
			
			subButton.setLayout(new GridLayout(0, 1));
			subButton.add(inputPanel);
			subButton.add(btnPanel);
			subButton.add(btnPanel2);
			subTable.setLayout(new GridLayout(0, 1));
			subTable.add(ScrollPane);

			setLayout(new BorderLayout(5,5));
			add(textarea,BorderLayout.NORTH);
			add(subButton, BorderLayout.SOUTH);
			add(subTable, BorderLayout.CENTER);
			
			
			jtable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent event) {
					if (!event.getValueIsAdjusting() && jtable.getSelectedRow() != -1) {				
						String selectedISBN = jtable.getValueAt(jtable.getSelectedRow(), 0).toString();	//Display in the input fields
						String selectedTitle = jtable.getValueAt(jtable.getSelectedRow(), 1).toString();
						ipISBN.setText(selectedISBN);
						ipTitle.setText(selectedTitle);
					}
				}
			});
				
			//Set a window listener for responding to closing application
			thisFrame.addWindowListener(new WindowAdapter() {
	            @Override
	            public void windowClosing(WindowEvent e) {
	                asksave();
	            }
	        });

		}

		/* Check which button they user clicked and call function*/
		class ButtonListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				String ISBN = ipISBN.getText();
				String title = ipTitle.getText();
				if (e.getSource() == add) {	//Add book
					addbook(ISBN, title);
				} else if (e.getSource() == edit) {	
					editIndex = editbook(ISBN);
				} else if (e.getSource() == save) {	//Save changes to book
					savebook(ISBN, title);
				} else if (e.getSource() == delete) {	//Delete book
					deletebook(ISBN);
				} else if (e.getSource() == search) {	//Search for book
					searchbook(ISBN, title);
				} else if (e.getSource() == more) {		//Manipulate the reservedQueue of the book
					more(ISBN);
				} else if (e.getSource() == load) {		//Load test data
					addbook("0131450913", "HTML How to Program");
					addbook("0131857576", "C++ How to Program");
					addbook("0132222205", "Java How to Program");
				} else if (e.getSource() == display) {		//Display the books by adding order
					displayall();
				} else if (e.getSource() == displayByISBN) {		//Display the books by ISBN order
					displayallByISBN();
				} else if (e.getSource() == displayByTitle) {	//Display the books by title order
					displayallByTitle();
				} else if (e.getSource() == exit) {		//Exit the program
					asksave();
				}
			}
		}

		/*Add the book to the table and linked list*/
		private void addbook(String ISBN, String title) {
			if (ISBN.equals("") || title.equals("")) {
				JOptionPane.showMessageDialog(thisFrame, "Error! Please type in the input field ");
			} else if (findISBN(ISBN) != -1) {
				JOptionPane.showMessageDialog(thisFrame,
						"Error! This book already exists in the system");
			} else {
				Book book = new Book(ISBN, title);
				bookLinkedList.add(book);
				tableModel.addRow(new String[] { book.getISBN(), book.getTitle(), book.getAvailable() });
				ipISBN.setText("");
				ipTitle.setText("");
				ISBN_AO = 1;
				Title_AO = 1;
				time.setText(dateformat.format(new Date(System.currentTimeMillis())));
			}
		}
		
		/*Set some button to enable or disable */
		private int editbook(String ISBN) {
			if (ISBN.equals("")) {
				JOptionPane.showMessageDialog(thisFrame, "Error! Please input the ISBN ");
				return -1;
			} else {
				int index = findISBN(ISBN);
				if (index == -1) {
					JOptionPane.showMessageDialog(thisFrame,
						"Error! This book does not exists in the system");
				} else {
					String title = bookLinkedList.get(index).getTitle();
					ipTitle.setText(title);

					add.setEnabled(false);
					edit.setEnabled(false);
					save.setEnabled(true);
					delete.setEnabled(false);
					search.setEnabled(false);
					more.setEnabled(false);
					load.setEnabled(false);
					display.setEnabled(false);
					displayByISBN.setEnabled(false);
					displayByTitle.setEnabled(false);
					exit.setEnabled(false);
				}
				return index;
			}
		}

		/* Save the input to the change book data and Set some button to enable or disable*/
		private void savebook(String newISBN, String newTitle) {
			if (newISBN.equals("") || newTitle.equals("")) {
				JOptionPane.showMessageDialog(thisFrame, "Error! Please type in the input field ");
			} else if ((findISBN(newISBN) != -1) && (findISBN(newISBN) != editIndex)) {
				JOptionPane.showMessageDialog(thisFrame,"Error! This ISBN already exists in the system");
			} else {
				String oISBN = bookLinkedList.get(editIndex).getISBN();	//record the original ISBN for table searching
				bookLinkedList.get(editIndex).setISBN(newISBN);
				bookLinkedList.get(editIndex).setTitle(newTitle);
				for (int i = 0; i < tableModel.getRowCount(); i++) {		//Update the book information display, if the book existed in the table
					if (tableModel.getValueAt(i, 0).toString().equals(oISBN)) {
						tableModel.setValueAt(newISBN, i, 0);
						tableModel.setValueAt(newTitle, i, 1);
						break;
					}
				}
				ipISBN.setText("");
				ipTitle.setText("");
				add.setEnabled(true);
				edit.setEnabled(true);
				save.setEnabled(false);
				delete.setEnabled(true);
				search.setEnabled(true);
				more.setEnabled(true);
				load.setEnabled(true);
				display.setEnabled(true);
				displayByISBN.setEnabled(true);
				displayByTitle.setEnabled(true);
				exit.setEnabled(true);			
				ISBN_AO = 1;
				Title_AO = 1;
				time.setText(dateformat.format(new Date(System.currentTimeMillis())));
			}
		}

		/*delete book from the table and book linked list */
		private void deletebook(String ISBN) {
			if (ISBN.equals("")) {
				JOptionPane.showMessageDialog(thisFrame, "Error! Please type in the input field ");
			} else {
				int index = findISBN(ISBN);
				if (index == -1) {
					JOptionPane.showMessageDialog(thisFrame, "Error! This book does not exists in the system");
				} else {
					bookLinkedList.remove(index);
					ISBN_AO = 1;
					Title_AO = 1;
					time.setText(dateformat.format(new Date(System.currentTimeMillis())));
					displayall();
					ipISBN.setText("");
					ipTitle.setText("");
				}
			}
		}

		//Search the books which is the same as input and display them 
		private void searchbook(String ISBN, String title) {
			if (ISBN.equals("") && title.equals("")) {
				JOptionPane.showMessageDialog(thisFrame, "Error! Please type in the input field");
			} else {
				while (tableModel.getRowCount() > 0) {
					tableModel.removeRow(0);
				}
				ipISBN.setText("");
				ipTitle.setText("");
				for (Book book : bookLinkedList) {
					if ((!ISBN.equals("")) && (book.getISBN().contains(ISBN))) {
						tableModel.addRow(new String[] { book.getISBN(), book.getTitle(), book.getAvailable() });
					} else if ((!title.equals(""))&&(book.getTitle().contains(title))) {
						tableModel.addRow(new String[] { book.getISBN(), book.getTitle(), book.getAvailable() });
					}
				}
			}
		}

		/* Show up a new window */
		private void more(String ISBN) {
			if (ISBN.equals("")) {
				JOptionPane.showMessageDialog(thisFrame, "Error! Please type the ISBN");
			} else {
				int index = findISBN(ISBN);
				if (index == -1) {
					JOptionPane.showMessageDialog(thisFrame, "Error! This book does not exists in the system");
				} else {
					Book selectBook = bookLinkedList.get(index);
					SubGUI more = new SubGUI(selectBook);
					more.setVisible(true);
					int i = 0;
					while(i < tableModel.getRowCount()) 
					{
						if (ISBN.equals(tableModel.getValueAt(i, 0).toString())) {
							tableModel.setValueAt(selectBook.getAvailable(), i, 2);
							break;
						}
						i++;
					}
				}
			}
		}

		//Display all books 
		private void displayall() {
			Book book;
			while (tableModel.getRowCount() > 0) {
				tableModel.removeRow(0);
			}
			for (int i = 0; i < bookLinkedList.size(); i++) {
				book = bookLinkedList.get(i);
				tableModel.addRow(new String[] { book.getISBN(), book.getTitle(), book.getAvailable() });
			}
		}

		//Display all books by ISBN
		private void displayallByISBN() {
			Book book;
			while (tableModel.getRowCount() > 0) {
				tableModel.removeRow(0);
			}
			ArrayList<Integer> Order = sort(1);
			if (ISBN_AO == 1) {
				for (int i = 0; i < bookLinkedList.size(); i++) {
					book = bookLinkedList.get((int) Order.get(i));
					tableModel.addRow(new String[] { book.getISBN(), book.getTitle(), book.getAvailable() });
				}
			} else {
				for (int i = bookLinkedList.size() - 1; i >= 0; i--) {
					book = bookLinkedList.get((int) Order.get(i));
					tableModel.addRow(new String[] { book.getISBN(), book.getTitle(), book.getAvailable() });
				}
			}
			if(ISBN_AO == 1) {
				ISBN_AO = 0;
			}
			else {
				ISBN_AO = 1;
			}
		}

		//Display all books by title
		private void displayallByTitle() {
			Book book;
			while (tableModel.getRowCount() > 0) {
				tableModel.removeRow(0);
			}
			ArrayList<Integer> order = sort(2);
			if (Title_AO == 1) {
				for (int i = 0; i < bookLinkedList.size(); i++) {
					book = bookLinkedList.get((int) order.get(i));
					tableModel.addRow(new String[] { book.getISBN(), book.getTitle(), book.getAvailable() });
				}
			} else {
				for (int i = bookLinkedList.size() - 1; i >= 0; i--) {
					book = bookLinkedList.get((int) order.get(i));
					tableModel.addRow(new String[] { book.getISBN(), book.getTitle(), book.getAvailable() });
				}
			}
			if(Title_AO == 1) {
				Title_AO = 0;
			}
			else {
				Title_AO = 1;
			}
		}

		/*Return the sorted list of index of books in the book linked list*/
		private ArrayList<Integer> sort(int type) {
			ArrayList<Integer> order = new ArrayList<>();
			for (int i = 0; i < bookLinkedList.size(); i++) {
				order.add(i);
			}
			if (type == 1) {
				order.sort(
						(book1, book2) -> ((bookLinkedList.get(book1)).getISBN()).compareTo(((bookLinkedList.get(book2)).getISBN())));
			} else {
				order.sort((book1, book2) -> ((bookLinkedList.get(book1)).getTitle()).compareToIgnoreCase(((bookLinkedList.get(book2)).getTitle())));
			}
			return order;
		}		
		
		/* Read the data from a file  */
		private MyLinkedList<Book> readdata() {	
			MyLinkedList<Book> datalist = new MyLinkedList<Book>();
			String row = "";
			try {
				BufferedReader readdata = new BufferedReader(new FileReader("data/data.csv"));
				try {
					while ((row = readdata.readLine()) != null) {
						String[] data = row.split("\t");
						Book book = new Book(data[0], data[1], data[2]);
						datalist.add(book);
					}
					readdata.close();
				} catch (IOException e) {
					
				}
			} catch (FileNotFoundException e) {
				
			}
			return datalist;
		}
			
		/* Show a dialog box and ask user if they want to save data or not*/
		private void asksave() {
			int dialogResult = JOptionPane.showConfirmDialog (null, "Do you need to save the data?", "Library Admin System", JOptionPane.YES_NO_OPTION);
			if(dialogResult == JOptionPane.YES_OPTION){	
				savedata();
				thisFrame.dispose();
				System.exit(0);
			} else if(dialogResult == JOptionPane.NO_OPTION){	
				thisFrame.dispose();
				System.exit(0);
			}
		}
					
		//Save the data in the book linkedlist into a file
		private void savedata() {	
			try {
				File datafile = new File("data");
				if (!datafile.isDirectory()){
					datafile.mkdir();
				}
				File file = new File("data/data.csv");
				FileWriter datawrite;
				datawrite = new FileWriter(file); 			
				for (Book book : bookLinkedList) {
					datawrite.append(book.getLogInfo());
				}
				datawrite.flush();
				datawrite.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
				
		private int findISBN(String ISBN) {
			for (int i = 0; i < bookLinkedList.size(); i++) {
				if (bookLinkedList.get(i).getISBN().equals(ISBN)) {
					return i;
				}
			}
			return -1;
		}
		
}
