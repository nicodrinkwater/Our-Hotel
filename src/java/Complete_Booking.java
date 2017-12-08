
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
            
          
            
            Statement statement = connection.createStatement();
            
            statement.execute("SET SEARCH_PATH TO hotelbooking;");
           
            add_customer_to_db(statement, request);
            add_booking_to_db(statement, request);
            // adds data to session info.
            createData(request);
            connection.close();
            
            // forwards to next page. The comfirmation page for customer where they receive their booking reference number
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/booked.jsp");
            rd.forward(request, response);
           
            
        } catch (Exception e) {
            response.sendRedirect("error.html");
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
            
            // get the info from form.
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
            
            // if customer not new update existing info and return (job done).
            ResultSet r = statement.executeQuery(check_if_new);
            while(r.next()){
                statement.executeUpdate(update_cust);
                return;
            }
            
          
            r = statement.executeQuery("select max(c_no) from customer");
            // this creates the customer number; the largest c_no in database + 1.
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
    private void add_booking_to_db(Statement statement, HttpServletRequest request) throws SQLException {
        
        // get info from session data
        HttpSession s = request.getSession();
        check_out = s.getAttribute("check_out").toString();
        check_in = s.getAttribute("check_in").toString();
        room = s.getAttribute("room").toString();
        cost = s.getAttribute("cost").toString();
        number_rooms = s.getAttribute("number").toString();
        
  
        ResultSet r = statement.executeQuery("select max(b_ref) from booking");
        // this creates the new booking reference: The largest b_ref on database + 1.
        while (r.next()) {
            b_ref = Integer.parseInt(r.getString(1)) + 1;
        }
        // the sql function 'create_booking' does most of the work here and creates both the booking and roombooking
        statement.execute("SELECT create_booking(" + b_ref + ", " + c_no + ", '"
                + room + "', '" + check_in + "', '" + check_out + "', " + number_rooms + ", " + cost + ");");
    }

    // add info to session.
    private void createData(HttpServletRequest request) {
        HttpSession s = request.getSession();
        
        s.setAttribute("name", name); 
        s.setAttribute("email", email);
        s.setAttribute("address", address);
        s.setAttribute("cc_type", cc_type);
        s.setAttribute("cc_number", cc_number);
        s.setAttribute("b_ref", Integer.toString(b_ref));
    }
}
