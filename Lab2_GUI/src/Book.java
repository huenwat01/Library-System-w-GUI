
public class Book{
    private String Title;
    private String ISBN;
    private boolean available;
    private MyQueue<String> reservedQueue;
    public Book(){
        available = true;
        reservedQueue = new MyQueue<String>();
    }

    public Book(String ISBN, String title, String available){
    	this.ISBN = ISBN;
    	this.Title = title;
    	if(!available.contains("true"))
    	{
    		this.available = false;
    	}else {
    		this.available = true;
    	}
        reservedQueue = new MyQueue<String>();
    }
    
    public Book(String ISBN, String title){
    	this.ISBN = ISBN;
    	this.Title = title;
        available = true;
        reservedQueue = new MyQueue<String>();
    }
    
    
    
    

    
    
    
    
    public String getAvailable() {
    	if(available)
    		return "true";
    	return "false";
    }

    public MyQueue<String> getReservedQueue(){
        return reservedQueue;
    }
    
    public String getWaitingQueueMsg() {
    	String sysMsg = "The waiting queue: \n";
    	for (String name : this.getReservedQueue().getList()) {
    		sysMsg += name;
    		sysMsg += "\n";
    	}
    	return sysMsg;
    }

    public String getLogInfo() {
		String tabString = this.getISBN();
		tabString += "\t";  
		tabString += this.getTitle();
		tabString += "\t";
		tabString += this.getAvailable();
		tabString += "\t";
		tabString += this.getReservedQueue().toString();
		tabString += "\n";
		return tabString;
	}
    
    public void setISBN(String ISBN){
        this.ISBN = ISBN;
    }
   
    public void setTitle(String title){
        this.Title = title;
    }
    
    public void setAvailable(boolean available){
        this.available = available;
    }
  
    public String getISBN(){
        return this.ISBN;
    }
    
    public String getTitle(){
        return this.Title;
    }  
    
    public boolean isAvailable(){
        return this.available;
    }

    
    public void setReservedQueue(MyQueue<String> reservedQueue){
        this.reservedQueue = reservedQueue;
    }
    
	
	
}