import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class ListLatestNum extends HttpServlet {
	
	private static final long serialVersionUID = 2L;
	private Statement statement = null;
	private Connection connect = null;
	private static SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		
		initDatabase();
		
		try {
			PrintWriter out = resp.getWriter();
			int num = Integer.parseInt(req.getParameter("num"));
			
			String query = String.format("SELECT * FROM event LIMIT %d", num);
			out.println(query);
			ResultSet rst_set = statement.executeQuery(query);
			JSONArray event_array = new JSONArray();
			
			while(rst_set.next()) {
				
				String event_id = rst_set.getString("event_id");
				String name = rst_set.getString("name");
				String destination = rst_set.getString("destination");
				String pickup = rst_set.getString("pickup");
				Date departure = rst_set.getDate("departure");
				Date cancel_before = rst_set.getDate("cancel_before");
				String description = rst_set.getString("description");
				String host_id = rst_set.getString("host_id");
				String participants_id = rst_set.getString("participants_ids");
				
				String departure_string = fmt.format(departure);
				String cancel_before_string = fmt.format(cancel_before);
				
				JSONObject event = new JSONObject();
				event.put("event_id", event_id);
				event.put("name", name);
				event.put("destination", destination);
				event.put("pickup", pickup);
				event.put("departure", departure_string);
				event.put("cancel_before", cancel_before_string);
				event.put("description", description);
				event.put("host_id", host_id);
				event.put("participants_id", participants_id);
				event_array.add(event);
			}
			
			StringWriter event_array_out = new StringWriter();
			event_array.writeJSONString(event_array_out);
			String jsonText = event_array_out.toString();
			out.println("status-code:\t" + 0);
			out.println(jsonText);
			
			
			
//			String name = (String) event_json.get("name");
//			String destination = (String) event_json.get("destination");
//			String pickup = (String) event_json.get("pickup");
//			String departure = (String)event_json.get("departure");	//yyyy.MM.dd HH:mm:ss
//			String cancel_before = (String) event_json.get("cancel_before"); //yyyy.MM.dd HH:mm:ss
//			String description = (String) event_json.get("description");
//			String host_id = (String) event_json.get("host_id");
//			String participants_ids = (String) event_json.get("participants_ids");
//
//			name = name.replace("\'", "\\\'");
//			destination = destination.replace("\'", "\\\'");
//			pickup = pickup.replace("\'", "\\\'");
//			description = description.replace("\'", "\\\'");
//			
//			String field = "(name, destination, pickup, departure, cancel_before, host_id";
//			String value = String.format("('%s','%s','%s','%s','%s','%s'", name, destination, pickup, departure, cancel_before, host_id);
//			
//			if (description != null) {
//				field += ", description";
//				value += String.format(",'%s'", description);
//			}
//			if (participants_ids != null) {
//				field += ", participants_ids";
//				value += String.format(",'%s'", participants_ids);
//			}
//			
//			field += ")";
//			value += ")";
//			
//			String query = String.format("INSERT INTO event %s VALUES %s", field, value);
//			
//			out.println(query);
			
		    
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