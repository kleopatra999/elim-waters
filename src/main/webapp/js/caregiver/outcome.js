function removeErrorClass(event){
	$(event.target).removeClass('fieldError');
}

function removeResultDiv(){
	$('#errors').remove();
	$('#success').remove();
}

function showError(error){
	var w = window.open();
	w.document.writeln(error.responseText);
}

function showResult(data){
	$('.fieldError').removeClass('fieldError');
	if(data != null && data.success == false){
		var items = [];
		for(var field in data.result) {
			  if (data.result.hasOwnProperty(field)){
				  var message = data.result[field];
				  $('#'+field).addClass('fieldError');
				  items.push('<li>' + message + '</li>');
			  }
		}
		appendDiv('errors', '<ul>' + items.join('') + '</ul>');
	} else if (data.success == true) {
		appendDiv('success', '<ul><li>' + data.result + '</li></ul>');
		$('#caregiver-id').val(data.id);
	}
}

function showResult(data, formToClear){
	$('.fieldError').removeClass('fieldError');
	if(data != null && data.success == false){
		var items = [];
		for(var field in data.result) {
			  if (data.result.hasOwnProperty(field)){
				  var message = data.result[field];
				  $('#'+field).addClass('fieldError');
				  items.push('<li>' + message + '</li>');
			  }
		}
		appendDiv('errors', '<ul>' + items.join('') + '</ul>');
	} else if (data.success == true) {
		appendDiv('success', '<ul><li>' + data.result + '</li></ul>');
		$('#caregiver-id').val(data.id);
		if(!jQuery.isEmptyObject(formToClear)){
			formToClear[ 0 ].reset();
		}
	}
}

function appendDiv(id, html, prevId){
	$('#errors').remove();
	$('#success').remove();
	
	div = document.createElement('div');
	$(div).attr('id', id);
	$(div).addClass('actionResult');
	$(div).html(html);
    $(div).appendTo($("#person-fields")); 
    $(div).click(function(){
        $(this).remove();
    });
}

function listcaregivers(){
	var location = window.location.toString();
	window.location.href = location.slice(0, location.lastIndexOf("/")+1) + "list";
}