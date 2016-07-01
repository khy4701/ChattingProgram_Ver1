package Util;
 
import java.text.SimpleDateFormat;
import java.util.Date;

public class PrintOrderTime {

	
	public static void printUserAndTimeOrder(String user, String order)
	{
		System.out.println("["+ System.currentTimeMillis() +"]"+ "<"+ user+ "> : " + order); 
		
		

	}
	
	static String getTime() {
        String name = Thread.currentThread().getName();
        SimpleDateFormat f = new SimpleDateFormat("[hh:mm:ss]");
        return f.format(new Date()) + name;
        
    }
}
