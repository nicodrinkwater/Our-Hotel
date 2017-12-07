

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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

// this is so that database updated after reception checks customers in and out and take payments
public class Reception_update extends HttpServlet {
    
    String b_ref;

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
            
            // get b_ref from cookies.
            get_info(request);
            
            update_rooms(statement, request);
            update_payment(statement, request);
            
            connection.close();
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/reception-home.html");
           
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

    // update the rooms if customer is checking in or out
    private void update_rooms(Statement statement, HttpServletRequest request) throws SQLException {
        String status = request.getParameter("status");
        if(status != null){
            if(status.equals("check_in")){
                statement.executeUpdate("SELECT update_status(" + b_ref + ", 'X');");
            } else if(status.equals("check_out")){
                statement.executeUpdate("SELECT update_status(" + b_ref + ", 'C');");
            }  
        }
    }

    // update balance in booking. 
    private void update_payment(Statement statement, HttpServletRequest request) throws SQLException {
        
        String pay_amount = request.getParameter("pay_amount");
        
        if(pay_amount.equals("") || pay_amount.equals(null)){
            // do nothing
        } else {
            statement.executeUpdate("Update booking SET b_outstanding = b_outstanding - " + pay_amount + " WHERE b_ref = " + b_ref + ";");
        }
        
    }

    private void get_info(HttpServletRequest request) {
        b_ref = request.getSession().getAttribute("b_ref").toString();
    }

}
