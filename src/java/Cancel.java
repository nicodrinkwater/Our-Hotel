/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
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
 * This serlvet cancels a booking identified b-ref. It removes it from booking and roombooking tables.
 */
public class Cancel extends HttpServlet {

    String b_ref;
    
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
            getDetails(request);
           
            // adds data to session.
            cancel(statement);
            
            connection.close();
            
            // forward to next page.
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/cancelled.jsp");
            rd.forward(request, response);
            
        } catch (Exception e) {
            // Any errors including if name, date or ref are incorrect will result in reloading of page. 
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/my_booking.jsp");
            rd.forward(request, response);
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

    private void getDetails(HttpServletRequest request) {
        HttpSession s = request.getSession();
        
        b_ref = (String)s.getAttribute("b_ref");
    }

    private void cancel(Statement statement) throws SQLException {
        statement.executeUpdate("DELETE FROM roombooking WHERE b_ref = " + b_ref + ";");
        statement.executeUpdate("DELETE FROM booking WHERE b_ref = " + b_ref + ";");
        
        
    }

}
