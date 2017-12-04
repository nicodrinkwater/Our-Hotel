
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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author nicod
 */
// this servlet is for reception to get customer details then go to pager where they can either take payment or update room status.
public class Reception extends HttpServlet {
    
    // these are variables that will hold details of booking.
    int number_rooms;
    String balance, total_cost, notes, room1, room2, room3;
    String email, address, room_type, cc_exp, cc_number, cc_type, b_ref, c_name, check_in, check_out, c_no;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try{
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
            
            getDetails(request, response, statement);
            connection.close();
            
            // makes sure all cookies expire after 30 mins
//            Cookie[] c = request.getCookies();
//            for(int i = 0; i < c.length; i++){
//                c[i].setMaxAge(30 * 60);
//            }
            
            response.sendRedirect("reception-home.html");
            
        } catch (Exception e) {
           
        }  
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    // gets customer details from either ref or name and check in
    private void getDetails(HttpServletRequest request, HttpServletResponse response, Statement statement) throws SQLException, IOException {
        
        b_ref = request.getParameter("ref");
        c_name = request.getParameter("name");
        
        check_in = request.getParameter("check_in");
        
        // get the booking reference if it is not provided and check validity in the case that it is
        try{
            if(b_ref == null || b_ref.equals("")){
           
                get_booking_reference(statement);
           
            // if reference not found go back to home
            } else if(!ref_exists(statement, response)){
               response.sendRedirect("reception-home.html"); 
            } 
        } catch(Exception e) {
                response.sendRedirect("reception-home.html");
        } 
        // get all booking details
        get_all_the_details(statement, request, response);
        
        //put the details in cookies
        create_cookies(response);
        
        // go to next webpage.
        response.sendRedirect("reception-manage.html");
    }

    // gets booking reference from cusrtomer name and checkin date.
    private void get_booking_reference(Statement statement) throws SQLException {
        statement.executeQuery("SELECT booking.b_ref "
                    + "FROM booking "
                    + "JOIN roombooking "
                    + "ON booking.b_ref = roombooking.b_ref "
                    + "JOIN customer ON booking.c_no = customer.c_no "
                    + "WHERE c_name = '" + c_name + "' "
                    + "AND checkin = '" + check_in + "';"); 
        
        ResultSet r = statement.getResultSet();
        while(r.next()){
            b_ref = r.getString(1);
        }
        r.close();
    }

    // returns true if booking reference is found in database. Gets the number of rooms with b_ref.
    private boolean ref_exists(Statement statement, HttpServletResponse response) throws IOException, SQLException {  
        statement.executeQuery("SELECT count(*) FROM roombooking WHERE b_ref = " + b_ref + ";");
        int n = 0;
        ResultSet r = statement.getResultSet();
        while(r.next()){
            n = r.getInt(1);
        }
        r.close();
        if(n == 0){
            return false;
        } else {
            number_rooms = n;
            return true;
        }
    }

    // get the details from b_ref
    private void get_all_the_details(Statement statement, HttpServletRequest request, HttpServletResponse response) throws SQLException {
        
        // gets booking details (cost, balance outstanding,  notes)
        statement.executeQuery("SELECT * FROM booking WHERE b_ref = " + b_ref + ";");
        ResultSet r = statement.getResultSet();
        while(r.next()){
            c_no = r.getString(2);
            total_cost = r.getString(3);
            balance = r.getString(4);
            notes = r.getString(5);       
        }
        
        // gets the card details
        statement.executeQuery("SELECT * FROM customer WHERE c_no = " + c_no + ";");
        r = statement.getResultSet();
        while(r.next()){
            c_name = r.getString(2);
            cc_type = r.getString(5);
            cc_number = r.getString(7);
            cc_exp = r.getString(6);
        }
        
        // gets the roombooking details (number, checkin, checkout)
        statement.executeQuery("SELECT * FROM roombooking WHERE b_ref = " + b_ref + ";");
        r = statement.getResultSet();
        int n = 1;
        while(r.next()){
            if(n == 1){
                room1 = r.getString(1);
            } else if(n == 2){
                room2 = r.getString(1);
            } else if(n == 3){
                room3= r.getString(1);
            }
            check_in = r.getString(3);
            check_out = r.getString(4);
            
            n++;
        }
    }

    private void create_cookies(HttpServletResponse response) {
        response.addCookie(new Cookie("b_ref", b_ref));
        response.addCookie(new Cookie("c_name", c_name));
        response.addCookie(new Cookie("check_in", check_in));
        response.addCookie(new Cookie("check_out", check_out));
        response.addCookie(new Cookie("total_cost", total_cost));
        response.addCookie(new Cookie("balance", balance));
        response.addCookie(new Cookie("cc_type", cc_type));
        response.addCookie(new Cookie("cc_exp", cc_exp));
        response.addCookie(new Cookie("cc_number", cc_number));
        response.addCookie(new Cookie("notes", notes));
        
        
        for(int i = 1; i <= number_rooms; i++){
            if(i == 1){
                response.addCookie(new Cookie("room1", room1));
            } else if(i == 2){
                response.addCookie(new Cookie("room2", room2));
            } else if(i == 3){
                response.addCookie(new Cookie("room3", room2));
            }
        }
        
    }
}
