
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author nicod
 * this servlet completes the booking. It creates or updates customer on db, creates a booking and a roombooking
 * and provides a booking reference for the customer.
 */
@WebServlet(urlPatterns = {"/Complete_Booking"})
public class Complete_Booking extends HttpServlet {

    String name, email, address, cc_type, cc_number, cc_exp, room, check_in, check_out, cost, number_rooms;
    int b_ref, c_no;
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
       
        try {
            Class.forName("org.postgresql.Driver");
            String cmpHost = "cmpstudb-02.cmp.uea.ac.uk";
            String dbUsername = "qsb17hdu";
            String dbName = "qsb17hdu";
            String dbPassword = "qsb17hdu";
            String myDBurl = ("jdbc:postgresql://" + cmpHost + "/" + dbName);
            
            /* Uncomment this to connect to uni database .*/
            Connection connection = DriverManager.getConnection(myDBurl, dbName, dbPassword);
            
            // connect to database on my laptop
            //Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/postgres", "postgres", "fuck1234");
            
            
            Statement statement = connection.createStatement();
            
            statement.execute("SET SEARCH_PATH TO hotelbooking;");
            
            
            add_customer_to_db(statement, request);
            add_booking_to_db(statement, request);
           
            create_cookies(request, response);
            connection.close();
            
            // makes sure all cookies expire after 30 mins
//            Cookie[] c = request.getCookies();
//            for(int i = 0; i < c.length; i++){
//                c[i].setMaxAge(30 * 60);
//            }
            
            response.sendRedirect("booked.html");
            
        } catch (Exception e) {
           
        }
    }
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            
        }
    }

    @Override
    public String getServletInfo() {
        return "Puts customer details in database";
    }// </editor-fold>

    // checks if new or not then either updates or adds new customer to database
    private void add_customer_to_db(Statement statement, HttpServletRequest request) throws SQLException {
          
            name = request.getParameter("name");
            email = request.getParameter("email"); 
            cc_type = request.getParameter("cc_type");
            cc_number = request.getParameter("cc_number");
            cc_exp = request.getParameter("cc_exp");
            address = request.getParameter("address");
         
            String check_if_new = "select c_email from customer where c_email = '" + email + "' AND c_name = '" + name + "';";
            String update_cust = "update customer set "
                    + "c_name = '" + name + "', "
                    + "c_address = '" + address + "', "
                    + "c_cardno = '" + cc_number + "', "
                    + "c_cardtype = '" + cc_type + "', "
                    + "c_cardexp = '" + cc_exp + "' "
                    + "where c_email = '" + email + "';";
            
            ResultSet r = statement.executeQuery(check_if_new);
            while(r.next()){
                statement.executeUpdate(update_cust);
                return;
            }
            
            // get the max customer number and add 1 for new customer number
            r = statement.executeQuery("select max(c_no) from customer");
            
            while(r.next()){
                c_no = Integer.parseInt(r.getString(1)) + 1;   
            }
            
            String insert_cust = "INSERT INTO customer "
                    + "VALUES( "
                    + c_no + ", '" 
                    + name + "', '" 
                    + email + "', '" 
                    + address + "', '" 
                    + cc_type + "', '" 
                    + cc_exp + "', '" 
                    + cc_number + "');";
            
            statement.execute(insert_cust);

         
  
    }
    
    
    // adds booking to database
    private void add_booking_to_db(Statement statement, HttpServletRequest request) {
        
        Cookie[] booking_details = request.getCookies();
        for (int i = 0; i < booking_details.length; i++) {
            String name = booking_details[i].getName();
            String value = booking_details[i].getValue();
            
            if("room".equals(name)){
                room = value;
            } else if("check_in".equals(name)){
                check_in = value;
            } else if("check_out".equals(name)){
                check_out = value;
            } else if("number".equals(name)){
                number_rooms = value;
            } else if("cost".equals(name)){
                cost = value;
            }
        }
       
        
        try {
                ResultSet r = statement.executeQuery("select max(b_ref) from booking");
               
                while(r.next()){
                    b_ref = Integer.parseInt(r.getString(1)) + 1;   
            }
            // the sql function 'create_booking' does most of the work here and creates both the booking and roombooking
            statement.execute("SELECT create_booking(" + b_ref + ", " + c_no + ", '" +
                room + "', '" + check_in + "', '" + check_out + "', " + number_rooms + ", " + cost + ");");
        } catch (SQLException ex) {
            Logger.getLogger(Complete_Booking.class.getName()).log(Level.SEVERE, null, ex);
            // TODO error handling
        }
    }

    // creates the cookies containing info that will be accessed by the next html page (confimation page)
    private void create_cookies(HttpServletRequest request, HttpServletResponse response) {
        
        response.addCookie(new Cookie("name", name));
        response.addCookie(new Cookie("email", email));
        response.addCookie(new Cookie("address", address));
        response.addCookie(new Cookie("cc_type", cc_type));
        response.addCookie(new Cookie("cc_number", cc_number));  
        response.addCookie(new Cookie("b_ref", Integer.toString(b_ref)));  
    }
}
