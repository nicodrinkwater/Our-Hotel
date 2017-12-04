

import java.io.IOException;
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
 * This servlet checks if rooms are available for the dates requested 
 * From the form on the hotel homepage
 */
@WebServlet(urlPatterns = {"/check_availability"})
public class check_availability extends HttpServlet {

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
            throws ServletException, IOException, SQLException, ClassNotFoundException {
        response.setContentType("text/html;charset=UTF-8");
        
        try {
            // these are used to connect to database
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

            statement.executeUpdate("SET SEARCH_PATH TO hotelbooking; ");

            // create the cookies that hold checkin, checkout and room info
            create_Cookies(request, response, statement);

            // makes sure all cookies expire after 30 mins
            Cookie[] c = request.getCookies();
            for(int i = 0; i < c.length; i++){
                c[i].setMaxAge(30 * 60);
            }
            
            response.sendRedirect("reservation.html");
        } catch (Exception e){
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(check_availability.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(check_availability.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(check_availability.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(check_availability.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    // create the cookies that hold checkin, checkout and room info
    private void create_Cookies(HttpServletRequest request, HttpServletResponse response, Statement statement) throws SQLException, IOException {
        
        String room = request.getParameter("room");
        String check_in = request.getParameter("check_in");
        String check_out = request.getParameter("check_out");
        int number = Integer.parseInt(request.getParameter("number"));
        
        // checks how many rooms are available on this type on these days
        statement.executeQuery("SELECT check_room('" + check_in + "', '" + check_out + "', '" + room +"')");
        ResultSet r = statement.getResultSet();
        
        
        
        // if number available < number required send the customer to the unavailable page.
        while(r.next()){
            int num_avail = r.getInt(1);
            
            if(num_avail < number){
                response.sendRedirect("unavailable.html");
              
            }
        }
        
        float cost = 0;
        if("std_d".equals(room)){
            cost = (number * 65);
        } else if("std_t".equals(room)){
            cost = (number * 55);
        } else if("sup_d".equals(room)){
            cost = (number * 90);
        } else if("sup_t".equals(room)){
        } else {
            cost = (number * 75);
        }
        
        // create the cookies with info that will be accessed by the next page (reservation.html)
        response.addCookie(new Cookie("room", room));
        response.addCookie(new Cookie("check_in", check_in));
        response.addCookie(new Cookie("check_out", check_out));
        response.addCookie(new Cookie("cost", Float.toString(cost)));
        response.addCookie(new Cookie("number", Integer.toString(number)));
       
       
    }
}
