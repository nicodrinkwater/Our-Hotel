<%-- 
    Document   : my_booking
    Created on : 09-Dec-2017, 12:52:12
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
    </head>
    <body>
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
                                <li class="menu-item active"><a href="index.html">Home</a></li> 
                                <li class="menu-item"><a href="my_booking.jsp">My Booking</a></li>
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
                        <h1 class="gdlr-page-title">My account</h1>
                    </div>	
                </div>
                <div class="content-page">
                    <div class="container">

                        <h3 class="gdlr-item-title"></h3>	

                        <div class="booking-information" >
                            <form action="get_my_booking">
                                <div class="get-info">
                                    <div class="get-user-info">	<label>Booking Reference</label> <span> <input type="text" name="b_ref" placeholder="Reference Number" size="20"> </span> </div>
                                    <div class="get-user-info g-sep"><label>OR</label></div>
                                    <div class="get-user-info">	<label>Name</label> <span> <input type="text" name="name" placeholder="Name" size="20"> </span> </div>
                                    <div class="get-user-info">	<label>Check In Date</label> <span> <input type="date" name="check_in" size="20" value="Checkin date"> </span> </div>
                                </div>
                                <p align="center"><br><button name="submit">See my booking</button></p>
                            </form>
                        </div>
                    </div>
                    <footer class="site-footer">
                        <div class="container">
                            <div class="col-sm-4 footer-section">
                                <h3 class="footer-title">Contact US</h3>
                                <div class="footer-section-content">
                                    <p>Hotel Heartache</p>
                                    <p>01603 444636</p>
                                    <p>sales@hotelheartache.com</p>
                                    <p>Castle Hill, Norwich, NR1 3QE</p>
                                </div>
                            </div>
                            <div class="col-sm-4 footer-section">  
                                <div class="footer-section-content">
                                    <div id="fb-root"></div>
                                    <script>(function (d, s, id) {
                                            var js, fjs = d.getElementsByTagName(s)[0];
                                            if (d.getElementById(id))
                                                return;
                                            js = d.createElement(s);
                                            js.id = id;
                                            js.src = 'https://connect.facebook.net/vi_VN/sdk.js#xfbml=1&version=v2.11&appId=1712380338980036';
                                            fjs.parentNode.insertBefore(js, fjs);
                                                    }(document, 'script', 'facebook-jssdk'));</script>
                                    <div class="fb-page" data-href="https://www.facebook.com/marketingAPAC" data-small-header="false" data-adapt-container-width="true" data-hide-cover="false" data-show-facepile="true"><blockquote cite="https://www.facebook.com/marketingAPAC" class="fb-xfbml-parse-ignore"><a href="https://www.facebook.com/marketingAPAC">Facebook Business</a></blockquote></div>
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
                                                            jQuery(window).scroll(function () {

                                                                if (jQuery(window).scrollTop() > 100) {
                                                                    var dataleft = ((jQuery('.site-header .top-header .top-container').width() - 100) / 2) - (jQuery('.site-header .top-header .sitetitle').width() / 2) - 15;
                                                                    jQuery('.site-header').addClass('top-header-fixed');
                                                                } else {
                                                                    jQuery('.site-header').removeClass('top-header-fixed');
                                                                }

                                                            });
                                                            jQuery(document).ready(function ($) {
                                                                $('.buttonmenu-mobile').click(function () {
                                                                    $('.gdlr-navigation-wrapper').slideToggle();
                                                                });
                                                            });
                </script>
                </body>
                </html>