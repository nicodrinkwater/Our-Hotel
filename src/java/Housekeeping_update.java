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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author qsb17hdu
 */
@WebServlet(urlPatterns = {"/Housekeeping_update"})
public class Housekeeping_update extends HttpServlet {
    
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
            
            update_rooms(request, response, statement);
            
            connection.close();
            
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/housekeeping-home.html");
           
            rd.forward(request, response);
            
        } catch (Exception e) {
            response.sendRedirect("error.html");
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

    private void update_rooms(HttpServletRequest request, HttpServletResponse response, Statement statement) throws SQLException {
        
        String r_number = "", r_status = "";
        statement.executeQuery("SELECT COUNT(r_no) AS num FROM room WHERE r_status = 'C';");
        ResultSet result = statement.getResultSet();
        String count = result.getString(1);
        int n = Integer.parseInt(count);
        
     
        for(int i = 0; i < n; i++){
            statement.executeQuery("SELECT r_no FROM room WHERE r_status = 'C';");
            ResultSet r = statement.getResultSet();
            for(int j = 0; j <= i; j++){
                r.next();
            } 
            r_number = r.getString(1);   
            r_status = request.getParameter(r_number);
            statement.executeUpdate("UPDATE room SET r_status = '" + r_status + "' WHERE r_no = " + r_number + "; ");
        }
    }
}
