import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class CreateEvent extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Statement statement = null;
	private Connection connect = null;
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		
		initDatabase();
		
		try {
			PrintWriter out = resp.getWriter();
			String event_data = req.getParameter("data");
			
			JSONParser parser=new JSONParser();
			JSONObject event_json = null;
			
			event_json = (JSONObject)parser.parse(event_data);
			
			
			String name = (String) event_json.get("name");
			String destination = (String) event_json.get("destination");
			String pickup = (String) event_json.get("pickup");
			String departure = (String)event_json.get("departure");	//yyyy.MM.dd HH:mm:ss
			String cancel_before = (String) event_json.get("cancel_before"); //yyyy.MM.dd HH:mm:ss
			String description = (String) event_json.get("description");
			String host_id = (String) event_json.get("host_id");
			String participants_ids = (String) event_json.get("participants_ids");

			name = name.replace("\'", "\\\'");
			destination = destination.replace("\'", "\\\'");
			pickup = pickup.replace("\'", "\\\'");
			description = description.replace("\'", "\\\'");
			
			String field = "(name, destination, pickup, departure, cancel_before, host_id";
			String value = String.format("('%s','%s','%s','%s','%s','%s'", name, destination, pickup, departure, cancel_before, host_id);
			
			if (description != null) {
				field += ", description";
				value += String.format(",'%s'", description);
			}
			if (participants_ids != null) {
				field += ", participants_ids";
				value += String.format(",'%s'", participants_ids);
			}
			
			field += ")";
			value += ")";
			
			String query = String.format("INSERT INTO event %s VALUES %s", field, value);
			
			out.println(query);
			
			int rst = 0;
			rst = statement.executeUpdate(query);
		    out.println("status-code:\t" + rst);
		    out.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			PrintWriter out = null;
			try {
				out = resp.getWriter();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			out.println(e.getMessage());
		}
		
	}

	
	private void initDatabase() {
		
		try {
			// this will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			// setup the connection with the DB.
	    	connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/shareit?"
	              + "user=root&password=root");
	    	
	    	// statements allow to issue SQL queries to the database
		    statement = connect.createStatement();
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
		
}