function getPersonsNamesOption(persons){
	var options="";
	var option = "<option id=\"{0}\" value=\"{0}\">{1}</option>"
		for (var index=0 ; index<persons.length ; index++ ){
			options += $.validator.format(option,persons[index][0],persons[index][2] + ' ' + persons[index][1]);
		}
	return options;
}

$(document).ready(function() {
	$('.datePicker').datepicker({
		changeMonth: true,
		changeYear: true
	});
	$.get('/elim/rest/persons/names', function(persons){
		var options=getPersonsNamesOption(persons);
		$('.persons-names-option').html(options);
	});

	$("#save-button").live("click", function(event) {
		$.post('/elim/rest/caregivers/save',
				$('form').serialize(),
				function(data){ 
			showResult(data, $('form'));
			if ($('#photo').val() == '')
				showDefaultPhoto();
			uploadPhoto(data.id);
		}
		);
	});

	$("#cancel-button").live("click", function(event) {
		if (confirm(jQuery.i18n.prop('ui.caregivers.confirm_quit')))
			listcaregivers();
	});

	$("#person-fields input,textarea,select").live("change", function(event) {
		removeErrorClass(event);
		removeResultDiv();
	});

	$('#photo').live('change', function(event) {
		previewPhoto(this);
	});
});
