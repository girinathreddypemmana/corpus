$(function() {
  if ($.browser.msie && $.browser.version.substr(0,1)<7)
  {
    $('li').has('ul').mouseover(function(){
        $(this).children('ul').css('visibility','visible');
        }).mouseout(function(){
        $(this).children('ul').css('visibility','hidden');
        })
  }
}); 

function redirectToHome(){
	window.location="home.html"
}

function redirectToPage(pageName){
	var page = pageName+".html";
	window.location=page;
	
}