// Validation of the reservation form on homepage
function check_reservation_form(){
    var check_in =  new Date(document.forms["reserve"]["check_in"].value);
    var check_out = new Date(document.forms["reserve"]["check_out"].value);
    var number = document.forms["reserve"]["number"].value;
    today = new Date();
    if ( check_in.getFullYear() > 2019 ) {
        alert("We are only taking bookings up to 2019");
        return false;
    }
    if (today >= check_in) {
        alert("Check in date must be from today");
        return false;
    }
    if(check_in >= check_out){
        alert("Check out date must later than check in date");
        return false;
    }
    if(check_out > check_in) {
        days = parseInt((check_out - check_in) / (1000 * 60 * 60 * 24)); 
        if (days > 14) {
            alert("If you would like to book for longer than 14 days please call 01603 444636");
            return false;
        }
    }
    return true;
}

// Validation of the customerInfo form on reservation page
function check_customerInfo_form(){
    var creditcard =  document.forms["customerInfo"]["cc_number"].value;
    var amno = /^(?:3[47][0-9]{13})$/;
    var mcno = /^(?:5[1-5][0-9]{14})$/;
    var vsno = /^(?:4[0-9]{16})$/;
    var cemail =  document.forms["customerInfo"]["email"].value;
    var mc = /\S+@\S+\.\S+/;
    var cname =  document.forms["customerInfo"]["name"].value;
    var nc = /^[a-zA-Z ]{1,30}$/;
    var uexp =  document.forms["customerInfo"]["cc_exp"].value;
    var uexpc = /^[0-9]{6}$/;
    var exp_year = uexp % 10000;
    var exp_month = (uexp - exp_year)/10000;
    var today = new Date();
    var ty = today.getFullYear();
    var tm = today.getMonth()+1;
    if (!cname.match(nc)){
        alert("Bad name");
        return false;
    }
    if (!cemail.match(mc)){
        alert("The email address is incorrect");
        return false;
    }
    if (!(creditcard.match(amno) || creditcard.match(vsno) || creditcard.match(mcno))){
        alert("Please input correct credit card number");
        return false;
    }
    if (!uexp.match(uexpc)){
        alert("The expire date is incorrect");
        return false;
    }
    if (exp_year < ty || (exp_year == ty && exp_month <= tm) || exp_month >12 ){
        alert("The expire date is incorrect");
        return false;
    }
    return true;
}

//JS to include another HTML file. Studied from https://www.w3schools.com/lib/w3.js
function includeHTML(cb) {
    var z, i, elmnt, file, xhttp;
    z = document.getElementsByTagName("*");

    for (i = 0; i < z.length; i++) {
        elmnt = z[i];
        file = elmnt.getAttribute("w3-include-html");
        if (file) {
            xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    elmnt.innerHTML = this.responseText;
                    elmnt.removeAttribute("w3-include-html");
                    w3.includeHTML(cb);
                }
            }      
            xhttp.open("GET", file, true);
            xhttp.send();
            return;
        }
    }
    if (cb) cb();
};

