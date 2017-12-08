<%-- 
    Document   : reception-manage
    Created on : 07-Dec-2017, 15:48:07
    Author     : qsb17hdu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>  
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1"> 
	<meta http-equiv="Content-Language" content="en-us">
    <title>Reception Area</title>
    <!-- Bootstrap --> 
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
        <script>
            // get info from cookies (booking details
            function getinfo(){
                var b_ref = "${b_ref}";
                var check_in = "${check_in}";
                var check_out = "${check_out}";
                var total_cost = "${total_cost}";
                var balance = "${balance}";
               
                var c_name = "${c_name}";
                var notes = "${notes}";
                
               
               
                
                // writes the html to show info on page
                document.getElementById("booking_info").innerHTML = 
                                                "<p align=\"center\">" + 
                                                "<div>" +
                                                "<h3>Booking details</h3><br>" +
                                                "<div class=\"item-info\"> <label>Booking Reference:</label> <span> " + b_ref + " </span> </div>" +
                                                "<div class=\"item-info\"> <label>Customer Name </label> <span> " + c_name + " </span> </div>" +
                                                "<div class=\"item-info\"> <label>Booking Cost</label> <span> £" + total_cost + " </span> </div>" +
                                                "<div class=\"item-info\"> <label>Balance Outstanding</label> <span> £" + balance + " </span> </div>" +
						"<div class=\"item-info\"> <label>Checking date: </label> <span> " + check_in + " </span> </div>" +
						"<div class=\"item-info\"> <label>Checkout date: </label> <span> " + check_out + " </span> </div>" +
						"<div class=\"item-info\"> <input type=\"text\" name=\"notes\" value=\"" + notes + "\">Notes</div>" +
                                                "<div class=\"item-info\"> <input type=\"numeric\" name=\"extras\">Food & Drink</div>" +
                                                " <input type=\"text\" name=\"pay_amount\" placeholder=\"Amount\" value=\"0.0\">Pay<br>" +
                                                "</div>" +
                                                "</p>"
                                     
            }
            
            // checks amount to pay is correct. 
            function validate_form(){
                
            }
         
        </script>
    </head>
<body onload="getinfo()">
    <div class="body-wrapper">
		<header class="site-header">			 
			<div class="container">
				<div class="logo-inner">
					<a href="index.html"><img src="images/logo.png" alt="Heartache Hotel" style="width: 100%;"></a>
				</div> 
				<span class="buttonmenu-mobile"><i class="fa fa-bars" aria-hidden="true"></i></span>
				<div class="gdlr-navigation-wrapper">
					 <nav class="gdlr-navigation sf-js-enabled sf-arrows" id="gdlr-main-navigation" role="navigation">
						<ul id="menu-main-menu-1" class="sf-menu gdlr-main-menu">
							 <li class="menu-item"><a href="index.html">Home</a></li> 
							 <li class="menu-item active"><a href="reservation.html">Reservation</a></li> 
							 <li class="menu-item"><a href="room.html">Rooms</a></li> 
							 <li class="menu-item"><a href="about-us.html">About us</a></li> 
							 <li class="menu-item"><a href="contact-us.html">Contact us</a></li> 
						</ul>	
					 </nav>	 
				</div>
			</div>
		</header>
		<div class="content-wrapper">
			<div class="gdlr-page-title-wrapper">
				<div class="gdlr-page-title-overlay"></div>
				<div class="gdlr-page-title-container container">
					<h1 class="gdlr-page-title">Reception Area</h1>
                                  
				</div>	
			</div>
			<div class="content-page">
			<div class="container">
                            <form action="Reception_update">
                                <div id="booking_info">
                                    <!-- the booking info will be shown here -->
                                </div>
                                     <!-- form to either take payment or update room status -->
                                <input type="radio" name="status" value="check_in" >Check In
                                <input type="radio" name="status" value="check_out" >Check Out<br>
                               
                                <button type="submit">Update</button>
                            </form>
                            <form action="reception-home.html">
                                <br>
                                <br>
                                <button type="submit">Back</button>
                            </form>
			</div>
			</div>
			</div>

		
		<footer class="site-footer">
			<div class="container">
				
			</div>
		</footer>
	</div>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	
	<script>
		jQuery(window).scroll(function(){                

		if(jQuery(window).scrollTop() > 100){
			var dataleft = ((jQuery('.site-header .top-header .top-container').width() - 100) / 2) - (jQuery('.site-header .top-header .sitetitle').width() / 2) - 15;	
			jQuery('.site-header').addClass('top-header-fixed'); 
		}       
		else {
			jQuery('.site-header').removeClass('top-header-fixed'); 
		}
		
	});
	jQuery(document).ready(function($){ 
		$('.buttonmenu-mobile').click(function(){
			$('.gdlr-navigation-wrapper').slideToggle();
		});
	});
	</script>
  </body>
</html>