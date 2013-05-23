var parishioner = function (id, church) {  
	return {  
		id: id,  
		church: church  
	};  
};  

function initi18n(){
	jQuery.i18n.properties({
		name:'messages', 
		path:'/elim/i18n/', 
		mode:'both',
		callback: function(){ 
			//alert( jQuery.i18n.prop('global_menu_new') ); 
		}
	});
}
/* Handle commons errors like access is denied.
 */
function handleError(error,status,errorThrown){
	if(error.status=="405"){
		alert("Access is denied!");
	}else{
		alert(error);
	}	
}

$(document).ready(function() {
	$('.hidden').hide();
	createDatePickers();
});

function createDatePickers(){ //on document.ready
	$('.datePicker').live('click', function() {
		$(this).datepicker({showOn:'focus'}).focus();
	});	
	$('.datePicker').datepicker({
		changeMonth: true,
		changeYear: true,
		yearRange: "-100:+0"
	});
	$('.datePicker').attr("autocomplete","off");
};

function notifyBar(message){
	$('.jbar').text(message);
	$('.jbar').removeClass('hidden');
	/*when application starts it adds a display:none style*/
	$('.jbar').removeAttr('style');
	setTimeout(function(){
		$('.jbar').addClass('hidden');
	}, 4000);
}
$.ctrl=function(key, callback, args) {
	var isCtrl = false;
	$(document).keydown(function(e) {
		if(!args) args=[]; // IE barks when args is null

		if(e.ctrlKey) isCtrl = true;
		if(e.keyCode == key.charCodeAt(0) && isCtrl) {
			callback.apply(this, args);
			return false;
		}
	}).keyup(function(e) {
		if(e.ctrlKey) isCtrl = false;
	});
};

$.ctrlKey=function(keyCode, callback, args) {
	var isCtrl = false;
	$(document).keydown(function(e) {
		if(!args) args=[]; // IE barks when args is null
		if(e.ctrlKey) isCtrl = true;
		if(e.keyCode == keyCode && isCtrl) {
			callback.apply(this, args);
			return false;
		}
	}).keyup(function(e) {
		if(e.ctrlKey) isCtrl = false;
	});
};	

$('.jbar').click(function(){
	if(!$(this).hasClass()){
		$(this).addClass('hidden');
	}
});

require(["upload"], function() {
	$(document).ready(function() {
		$('#importFromExcelFile').live('click',function(e){
			ajaxFileUpload('chooseExcelFile','//rest/parishioners/upload-excel');
		});
		initi18n();
		/*
		$('#importFromExcelFile').change(function(e){
		        var file=$(this).val();
			alert(file);
			upload(file,'//rest/parishioners/upload-excel');
		});*/

		$.extend ({
			URLEncode: function (s) {
				s = encodeURIComponent (s);
				s = s.replace (/\~/g, '%7E').replace (/\!/g, '%21').replace (/\(/g, '%28').replace (/\)/g, '%29').replace (/\'/g, '%27');
				s = s.replace (/%20/g, '+');
				return s;
			},
			URLDecode: function (s) {
				s = s.replace (/\+/g, '%20');
				s = decodeURIComponent (s);
				return s;
			}
		});
	});
});