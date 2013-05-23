function isEmpty(text) {
	
	if ( (text == null) || (text == "") )
		return true;
	
	return false;
}

function allInputsProvided() {
	
	if (isEmpty($("#field option[value='" + $('#field').val() + "']").text()))
		return false;
	
	if (isEmpty($("#operator option[value='" + $('#operator').val() + "']").text()))
		return false;
	
	if (isEmpty($('#value').val()))
		return false;
	
	return true;
}

function closeFilterWindow() {
	$('#cover').hide();
	$('#filterScreen').hide();
}

function updateAddButton() {
	if (allInputsProvided())
		$('#add').removeAttr('disabled');
	else
		$('#add').attr('disabled','disabled');
}

function applySelectedConditions() {
	closeFilterWindow();
	$('#parishioner-data-list').dataTable().fnDraw();
}

function addCondition() {
	var value = $('#field').val();
	value += '|' + $('#operator').val().replace('inputValue', $('#value').val()) + ' ';
	
	var text = "";
	text = '"' + $("#field option[value='" + $('#field').val() + "']").text() + '"';
	text += ' ' + $("#operator option[value='" + $('#operator').val() + "']").text() + ' ';
	text += '"' + $('#value').val() + '" ';
	
	var checkbox = '<span><input type="checkbox" checked="checked" value="' + value + '">' + text + '</span><br>';
	$('#filter-conditions').append(checkbox);
}

function removeSelectedConditions() {
	$('input[type="checkbox"]:checked').parent().remove();
}

$(document).ready(function() {
	
	$('#filterBtn').live("click", function(e) {

		e.preventDefault();
		
		var id = $("#filterScreen");
		
		if (id.html() == "")
			$("#filterScreen").load("/elim/parishioners/filter #filterWindow");
		
		//Get the screen height and width
		var maskHeight = $(document).height();
		var maskWidth = $(window).width();
	
		//Set heigth and width to mask to fill up the whole screen
		$('#cover').css({'width':maskWidth,'height':maskHeight});
		
		//transition effect		
		$('#cover').fadeIn(1000);	
		$('#cover').fadeTo("slow",0.8);	
	
		//Get the window height and width
		var winH = $(window).height();
		var winW = $(window).width();
              
		//Set the popup window to center
		$(id).css('top',  winH/2-$(id).height()/2);
		$(id).css('left', winW/2-$(id).width()/2);
	
		//transition effect
		$(id).fadeIn(2000); 
	
	});
	
	//if field value was changed
	$('#field').live("change", function() {
		updateAddButton();
	});
	
	//if operator value was changed
	$('#operator').live("change", function(event) {
		updateAddButton();
	});
	
	//if value input's value was changed
	$('#value').live("keyup", function() {
		updateAddButton();
	});
	
	//if add button is clicked
	$('#add').live("click", function(e) {
		e.preventDefault();
		addCondition();
	});
	
	//if applu=y button is clicked
	$('#apply').live("click", function(e) {
		e.preventDefault();
		applySelectedConditions();
	});
	
	//if add button is clicked
	$('#remove').live("click", function(e) {
		e.preventDefault();
		removeSelectedConditions();
	});
	
	//if cancel button is clicked
	$('#cancel').live("click", function (e) {
		e.preventDefault();
		closeFilterWindow();
	});		
	
	//if mask is clicked
	$('#cover').click(function () {
		$(this).hide();
		$('#filterScreen').hide();
	});			

	$(window).resize(function () {
	 
 		var box = $('#filterWindow');
 
        //Get the screen height and width
        var maskHeight = $(document).height();
        var maskWidth = $(window).width();
      
        //Set height and width to mask to fill up the whole screen
        $('#filterScreen').css({'width':maskWidth,'height':maskHeight});
               
        //Get the window height and width
        var winH = $(window).height();
        var winW = $(window).width();

        //Set the popup window to center
        box.css('top',  winH/2 - box.height()/2);
        box.css('left', winW/2 - box.width()/2);
	 
	});
	
});