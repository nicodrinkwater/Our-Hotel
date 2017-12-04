
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
    
    //this is the next page that will be called if no errors.
    String next_page = "reception-manage.html";
    //this is the page that will called if error
    String error_page = "error.html";
    
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
            create_cookies(response);
            connection.close();
            
        } catch (Exception e) {
            next_page = error_page;
        }  
        
        response.sendRedirect(next_page);
          
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
        // if error go back to reception home
        } catch(Exception e) {
            response.sendRedirect("reception-home.html"); 
        } 
        // get all booking details
        get_all_the_details(statement, request, response);
        
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

    // creates the cookies that will be used by next page (reception-manage.html)
    private void create_cookies(HttpServletResponse response) {
        
        // max oge of cookie is 30 mins
        int max_age = 30 * 60;
        
        Cookie a = new Cookie("b_ref", b_ref);
        a.setMaxAge(max_age);
        Cookie b = new Cookie("c_name", c_name);
        b.setMaxAge(max_age);
        Cookie c = new Cookie("check_in", check_in);
        c.setMaxAge(max_age);
        Cookie d = new Cookie("check_out", check_out);
        d.setMaxAge(max_age);
        Cookie e = new Cookie("total_cost", total_cost);
        e.setMaxAge(max_age);
        Cookie f = new Cookie("balance", balance);
        f.setMaxAge(max_age);
        Cookie g = new Cookie("cc_type", cc_type);
        g.setMaxAge(max_age);
        Cookie h = new Cookie("cc_exp", cc_exp);
        h.setMaxAge(max_age);
        Cookie i = new Cookie("cc_number", cc_number);
        i.setMaxAge(max_age);
        Cookie j = new Cookie("notes", notes);
        j.setMaxAge(max_age);
        
        response.addCookie(a);
        response.addCookie(b);
        response.addCookie(c);
        response.addCookie(d);
        response.addCookie(e);
        response.addCookie(f);
        response.addCookie(g);
        response.addCookie(h);
        response.addCookie(i);
        response.addCookie(j);
       
        Cookie r1,r2,r3;
        // this is for multiple roooms up to three
        for(int n = 1; n <= number_rooms; n++){
            if(n == 1){
                r1 = new Cookie("room1", room1);
                r1.setMaxAge(max_age);
                response.addCookie(r1);
            } else if(n == 2){
                r2 = new Cookie("room2", room2);
                r2.setMaxAge(max_age);
                response.addCookie(r2);
            } else if(n == 3){
                r3 = new Cookie("room3", room3);
                r3.setMaxAge(max_age);
                response.addCookie(r3);
            }
        }
        
       
        
        
        
        
        
        
        
        
    }
}
