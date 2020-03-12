$(document).ready(function(){


    $(window).scroll(function(){
        var winScroll1 = $(window).scrollTop();
        
        if(winScroll1 > 90){
            $(".btn-top").show();
            $("header").addClass("up");
        }else{
            $(".btn-top").hide();
            $("header").removeClass("up");
        }
    });/*window scroll*/

    
    $(".btn-top").click(function(){
           $("html, body").animate({'scrollTop':'0'},500) 
        });/*btn click*/
    
    
    
    	// google map
		var map;
		function initMap() {
		  map = new google.maps.Map(document.getElementById('map'), {
		    center: {lat: -34.397, lng: 150.644},
		    zoom: 8
		  });
		}

});




