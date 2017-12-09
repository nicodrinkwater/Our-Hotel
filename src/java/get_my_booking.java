/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author qsb17hdu
 * This servlet is for the a customer to retrieve their booking info. 
 */
public class get_my_booking extends HttpServlet {

    //this is the page that will called if error
    String error_page = "error.html";
    
    // these are variables that will hold details of booking.
    int number_rooms, nights;
    String balance, total_cost, notes, room1, room2, room3;
    String email, address, room_type, cc_exp, cc_number, cc_type, b_ref, c_name, check_in, check_out, c_no;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
       
         try{
            Class.forName("org.postgresql.Driver");
            String cmpHost = "cmpstudb-02.cmp.uea.ac.uk";
            
            String dbName = "qsb17hdu";
            String dbPassword = "qsb17hdu";
            String myDBurl = ("jdbc:postgresql://" + cmpHost + "/" + dbName);
            
            /* Uncomment this to connect to uni database .*/
            Connection connection = DriverManager.getConnection(myDBurl, dbName, dbPassword);
            
         
           
            Statement statement = connection.createStatement();
            
            statement.execute("SET SEARCH_PATH TO hotelbooking;");
            
            // gets the details of booking, customer from database.
            getDetails(request, response, statement);
           
            // adds data to session.
            createData(request);
            
            connection.close();
            
            // forward to next page.
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/customer-manage.jsp");
            rd.forward(request, response);
            
        } catch (Exception e) {
            // Any errors including if name, date or ref are incorrect will result in reloading of page. 
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/my_booking.jsp");
            rd.forward(request, response);
        }  
    }
    
     // gets customer details from either ref or name and check in
    private void getDetails(HttpServletRequest request, HttpServletResponse response, Statement statement) throws SQLException, IOException {
        
        b_ref = request.getParameter("b_ref");
        c_name = request.getParameter("name");
        
        check_in = request.getParameter("check_in");
        
        // get the booking reference if it is not provided.
        if(b_ref == null || b_ref.equals("")){
            get_booking_reference(statement);
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
            address = r.getString(4);
            email = r.getString(3);
            cc_type = r.getString(5);
            cc_number = r.getString(7);
            cc_exp = r.getString(6);
        }
        
         // gets the number of rooms from roombooking with right b_ref.
        statement.executeQuery("SELECT COUNT(r_no) FROM roombooking WHERE b_ref = " + b_ref + ";");
        r = statement.getResultSet();
        while(r.next()){
            number_rooms = r.getInt(1);
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
        
        statement.executeQuery("SELECT r_class FROM room WHERE r_no = " + room1 + ";");
        r = statement.getResultSet();
        while(r.next()){
            room_type = r.getString(1);
        }
        
       // gets the muber of nights that booking would constitute from database
        statement.executeQuery("SELECT number_of_nights('" + check_in + "', '" + check_out + "')");
        r = statement.getResultSet();
        while(r.next()){
            nights = r.getInt(1);
        }
    }
    
    // adds data to session
    private void createData(HttpServletRequest request){
        HttpSession s = request.getSession();
        s.setAttribute("b_ref", b_ref);
        s.setAttribute("name", c_name);
        s.setAttribute("check_in", check_in);
        s.setAttribute("check_out", check_out);
        s.setAttribute("cost", total_cost);
        s.setAttribute("balance", balance);
        s.setAttribute("notes", notes);
        s.setAttribute("email", email);
        s.setAttribute("address", address);    
        s.setAttribute("number", number_rooms);    
        s.setAttribute("room", room_type);
        s.setAttribute("nights", nights);
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

}
