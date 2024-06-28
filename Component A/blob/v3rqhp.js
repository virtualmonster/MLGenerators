

var selectedArr1 = [];
var selectedArr2 = [];
var selectedArr3 = [];

 var url = "/HCLWCMExtension/HCLPnPServlet?cmd=getstories&pgsize=9";

      function loadStories(turl) {
          //alert("turl=" + turl);
          var xhttp = new XMLHttpRequest();
          xhttp.onreadystatechange = function() {
              if (this.readyState == 4 && this.status == 200) {
                  document.getElementById("stories").innerHTML = this.responseText;
              }
          };
          xhttp.open("GET",  turl, true);
          xhttp.send();
     }

       function pclick(text) {
            var x = document.getElementById("story-selected").innerHTML;
   // alert("x=" + x);
            var z = parseInt(x, 10);
   // alert("z=" + z);
            var max =  document.getElementById("story-size").innerHTML;
   // alert("max=" + max);
            var m = parseInt(max,10);
   // alert("m=" + m);
            var i = 1;
            if (text == "prev") {
                 if (z > 1) {
                     i = z - 1;
                 }
            } else
              if (text == "next") {
	          if (z < m) {
	              i = z  + 1;
	           } else {
                      i = m;
                   }
            } else {

               i = parseInt(text, 10);

            }
            listActive();
            var cats =  "&cat1=" + selectedArr1.join(",").replace(/&/g,'%26') + "&cat2=" + selectedArr2.join(",").replace(/&/g,'%26') + "&cat3=" + selectedArr3.join(",").replace(/&/g,'%26');
            loadStories(url + "&pgnum=" + i + cats);
     }

    // alert("Hello!");
    if ( document.getElementById("stories") != null) {
       loadStories(url + "&pgnum=1");
}

function listActive() {
        //alert("listActive!");

	    var children = $("#products_solutions").children();
	    //loop on all the childrens
	    selectedArr1.length = 0;
	    for(var i = 0; i < children.length; i++){
	      //check if the element has active class
	      if($($("#products_solutions").children()[i]).children('a').hasClass('active')){
	        selectedArr1.push($($("#products_solutions").children()[i]).children('a').attr('data-value'));

	      }
	    }

	    children = $("#industries").children();
	            selectedArr2.length = 0;
			    //loop on all the childrens
			    for(var i = 0; i < children.length; i++){
			      //check if the element has active class
			      if($($("#industries").children()[i]).children('a').hasClass('active')){
			        selectedArr2.push($($("#industries").children()[i]).children('a').attr('data-value'));

			      }
	            }

	    children = $("#regions").children();
	            selectedArr3.length = 0;
			    //loop on all the childrens
			    for(var i = 0; i < children.length; i++){
			      //check if the element has active class
			      if($($("#regions").children()[i]).children('a').hasClass('active')){
			        selectedArr3.push($($("#regions").children()[i]).children('a').attr('data-value'));
			      }
	    }
	    //Selected Array will be displayed here

	    console.log("selectedArr1:" + selectedArr1.join(",").replace(/&/g,'%26') );
	    console.log("selectedArr2:" + selectedArr2.join(",").replace(/&/g,'%26') );
	    console.log("selectedArr3:" + selectedArr3.join(",").replace(/&/g,'%26') );

}

 $('.dropdown-menu').on('click', 'a', function() {

	if ($(this).hasClass('active')) {

		 $(this).removeClass('active');
    } else {

		 $(this).addClass('active');
    }
    listActive();
    var url2 = url + "&pgnum=1" + "&cat1=" + selectedArr1.join(",").replace(/&/g,'%26') + "&cat2=" + selectedArr2.join(",").replace(/&/g,'%26') + "&cat3=" + selectedArr3.join(",").replace(/&/g,'%26');
    console.log("url=" + url2);
    loadStories(url2);
   // loadStories(url + "&pgnum=1" );


});