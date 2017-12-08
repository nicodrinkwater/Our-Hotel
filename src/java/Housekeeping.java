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
import javax.servlet.*;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author qsb17hdu
 * This servlet is called by housekeeping-home
 * It's purpose is to see which rooms have status 'C' (checked out) and
 * send this info to housekeeping-manage.
*/
public class Housekeeping extends HttpServlet {

    String next_page = "housekeeping-manage.html";
    String error_page = "error.html";
    String rooms = "";
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
            
          
           
            Statement statement = connection.createStatement();
            
            statement.execute("SET SEARCH_PATH TO hotelbooking;");
            
            // gets a list of rooms that have status 'C' and turns this list into a string "rooms"
            get_rooms(request, response, statement);
           
            connection.close();
            
            // forwards to the next page (So housekeeeping can view list)
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/Housekeeping-manage.jsp");
           
            rd.forward(request, response);
            
            
        } catch (Exception e) {
            next_page = error_page;
        }  
        //response.sendRedirect(next_page);
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

    // this gets the rooms that have status 'C' from room in hotelbooking database adds them to string "rooms" then adds "rooms" to session info.
    private void get_rooms(HttpServletRequest request, HttpServletResponse response, Statement statement) throws SQLException {
        statement.executeQuery("SELECT r_no FROM room WHERE r_status = 'C';");
        rooms = "";
        ResultSet r = statement.getResultSet();
        while(r.next()){
            rooms += r.getString(1) + ",";
        }
        HttpSession s = request.getSession();
        
        s.setAttribute("rooms", rooms);
    }

    
}
