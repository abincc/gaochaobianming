$(function() {
var val=$(window).width()/7.5;
$('html').css('font-size',val);
$(window).resize(function(event) {
	var val=$(window).width()/7.5;
	$('html').css('font-size',val);
});
})