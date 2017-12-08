<%-- 
    Document   : Housekeeping-manage
    Created on : 07-Dec-2017, 13:10:11
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
    <title>House Keeping Area</title>
    <!-- Bootstrap --> 
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" href="css/style.css">
       
        <script type="text/javascript">
                
                function get_info(){
  
                    var html = "";
                    var rooms = "${rooms}";
                    var number_rooms = rooms.length / 4;
                    for(var i = 0; i < number_rooms; i++){
                        html +=  "<div class=\"item-info\"> <label>Room: </label> <span> " + rooms.substr(i * 4, 3) + 
                                " </span> <input type=\"radio\" name=\""  + rooms.substr(i * 4, 3) + "\" value=\"C\" checked> CheckedOut " +
                                " </span> <input type=\"radio\" name=\""  + rooms.substr(i * 4, 3) + "\" value=\"A\"> Available " +
                                "<input type=\"radio\" name=\"" + rooms.substr(i * 4, 3) + "\" value=\"X\"> Unavailable </div>";
                    } 
                    document.getElementById("room_list").innerHTML = 
                                                    "<p align=\"center\">" + 
                                                    "<div class=\"booking-info\">" +
                                                    html + 
                                                    "</div>" + 
                                                    "</p>";
                
                }    
               
        </script>
  </head>
  <body onload="get_info()">
    <div class="body-wrapper">
		<header class="site-header">			 
			<div class="container">
				<div class="logo-inner">
					<a href="/index.html"><img src="images/logo.png" alt="Heartache Hotel" style="width: 100%;"></a>
				</div> 
				<span class="buttonmenu-mobile"><i class="fa fa-bars" aria-hidden="true"></i></span>
				<div class="gdlr-navigation-wrapper">
					 <nav class="gdlr-navigation sf-js-enabled sf-arrows" id="gdlr-main-navigation" role="navigation">
						<ul id="menu-main-menu-1" class="sf-menu gdlr-main-menu">
							 <li class="menu-item"><a href="/index.html">Home</a></li> 
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
					<h1 class="gdlr-page-title">House Keeping</h1>
				</div>	
			</div>
			<div class="content-page">
			<div class="container">
                            
                            <p align="center">
                                <form action="Housekeeping_update" method="post">
                                    <div id="room_list">
                                     <!-- this shows the list of rooms that are checked out -->

                                    </div>
                                    <button type="submit">Update Room</button>
                                </form>
                            </p>
			</div>
			</div>
			</div>
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