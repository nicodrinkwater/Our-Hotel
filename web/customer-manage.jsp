<%-- 
    Document   : customer-manage
    Created on : 09-Dec-2017, 13:16:33
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
    <title>Booking</title>
    <!-- Bootstrap --> 
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
        <!-- this javascript gets the room, check in, check out info from cookie and writes it to page -->
        <script>
            function get_info(){
                var room = "${room}";
                var check_in = "${check_in}";
                var check_out = "${check_out}";
                var cost = "${cost}";
                var number = "${number}";
                var b_ref = "${b_ref}";
                var name = "${name}";
                var address = "${address}";
                var email = "${email}";
                var notes = "${notes}";
                var nights = "${nights}";
                
              
                if("std_d" === room){
                    room = "Standard Double";
                } else if("std_t" === room){
                    room = "Standard Twin";
                } else if("sup_d" === room){
                    room = "Superior Double";
                } else if("sup_t" === room){
                    room = "Superior Twin";
                } 
  
                document.getElementById("room_info").innerHTML = 
                                                "<p align=\"center\">" + 
                                                "<div class=\"booking-info\">" +
                                                "<h3>Your booking</h3><br>" +
                                                "<div class=\"item-info\"> <label>Booking Reference:</label> <span> " + b_ref + " </span> </div>" +
                                                "<div class=\"item-info\"> <label>Room Type: </label> <span> " + room + " </span> </div>" +
                                                "<div class=\"item-info\"> <label>Number of Rooms: </label> <span>" + number + " </span> </div>" +
                                                "<div class=\"item-info\"> <label>Number of Nights: </label> <span>" + nights + " </span> </div>" +
						"<div class=\"item-info\"> <label>Checking date: </label> <span> " + check_in + " </span> </div>" +
						"<div class=\"item-info\"> <label>Checkout date: </label> <span> " + check_out + " </span> </div>" +
						"<div class=\"item-info\"> <label>Total: </label> <span> £" + cost + " </span> </div>" +
                                                "<div class=\"item-info\"> <label>Name</label> <span> " + name + " </span> </div>" +
                                                "<div class=\"item-info\"> <label>Address</label> <span> " + address + " </span> </div>" +
                                                "<div class=\"item-info\"> <label>Email</label> <span> " + email + " </span> </div>" +
                                                "<div class=\"item-info\"> <label>Notes</label> <span> " + notes + " </span> </div>" +
                                                "</div>"+
                                                "</p>"
            }
            
        </script>
  </head>
  <!-- get the info from the cookies about reservation -->
  <body onload="get_info()">
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
							 <li class="menu-item active"><a href="booked.html">Booked!</a></li> 
							 <li class="menu-item"><a href="room.html">Rooms</a></li> 
							 <li class="menu-item"><a href="about-us.html">About us</a></li> 
							 <li class="menu-item"><a href="contact-us.html">Contact us</a></li> 
                                                         <input type="text" name="" placeholder="">
						</ul>	
					 </nav>	 
				</div>
			</div>
		</header>
		<div class="content-wrapper">
			<div class="gdlr-page-title-wrapper">
				<div class="gdlr-page-title-overlay"></div>
				<div class="gdlr-page-title-container container">
					<h1 class="gdlr-page-title">Booking</h1>
				</div>	
			</div>
                        <form action="Cancel" method="post">
			<div class="content-page">
			<div class="container">
			
                            <h3 class="gdlr-item-title">Your booking information</h3>			
                            <div class="booking-information" >
                                    <div id="room_info">

                                    </div>

                            </div>
                          
			</div>
			<h3 class="gdlr-item-title"></h3>
                        <p><button type="submit">Cancel</button></p>
			<p><button href="index.html">Home</button></p>
			</div>
                     
                        </form>
		</div>
		<footer class="site-footer">
			<div class="container">
				<div class="col-sm-4 footer-section">
					<h3 class="footer-title">Contact US</h3>
					<div class="footer-section-content">
						<p>Hotel Heartache</p>
						<p>01603 444636</p>
						<p>Norwich</p>
						<p>NR1 3EJ</p>
					</div>
				</div>
				<div class="col-sm-4 footer-section">  
					<div class="footer-section-content">
						 <div id="fb-root"></div>
						<script>(function(d, s, id) {
						  var js, fjs = d.getElementsByTagName(s)[0];
						  if (d.getElementById(id)) return;
						  js = d.createElement(s); js.id = id;
						  js.src = 'https://connect.facebook.net/vi_VN/sdk.js#xfbml=1&version=v2.11&appId=1712380338980036';
						  fjs.parentNode.insertBefore(js, fjs);
						}(document, 'script', 'facebook-jssdk'));</script>
						<div class="fb-page" data-href="https://www.facebook.com/travelodge" data-small-header="false" data-adapt-container-width="true" data-hide-cover="false" data-show-facepile="true"><blockquote cite="https://www.facebook.com/marketingAPAC" class="fb-xfbml-parse-ignore"><a href="https://www.facebook.com/marketingAPAC">Facebook Business</a></blockquote></div>
					</div>
				</div>
				<div class="col-sm-4 footer-section">
					<div class="footer-section-content">
						<iframe src="https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d9686.91870447859!2d1.2963859!3d52.628729!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0x1cfc2d9a1e3212cd!2sNorwich+Castle+Museum+%26+Art+Gallery!5e0!3m2!1sen!2suk!4v1511704542835" width="340" height="300" frameborder="0" style="border:0" allowfullscreen></iframe>
					</div>
				</div>
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
