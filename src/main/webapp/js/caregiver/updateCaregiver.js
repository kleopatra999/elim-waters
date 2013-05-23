var selectedRow = 1;
var options="";
var personsListOptions="";
function updateForm(){
	var text = $('.row_selected').children(":first").html();
	selectedRow = $('.row_selected .row-count').html();
	if(text != null)
		updateCaregiver(text);
	$('#caregiver-update-form input').removeAttr("placeholder");
}

function updateCaregiver(id){
	$.get('/elim/rest/caregivers/update-form/'+id,
			function(data){
		if(data!=null){
			$("#caregiver-update-form").html(data);
			createDatePickers();
			// set options list for baptist
			$('.persons-names-option').html(options);
			$('.persons-names-option option #'+id).attr('selected','selected')
			// select the baptist from the list
			$('#'+id).attr('selected','selected');
			updatePhoto($('#caregiver-id').val());
		}
	});
}

function updateTableItem() {
	table = $('#caregiver-data-update').dataTable();
	table.fnUpdate($('#firstName').val(), selectedRow-1, 2);
	table.fnUpdate($('#lastName').val(), selectedRow-1, 3);
	table.fnDraw();
}

function addTableItem() {
	table = $('#caregiver-data-update').dataTable();
	var index = table.fnGetData().length + 1;
	table.fnAddData( [index,
	                  $('#firstName').val(),
	                  $('#lastName').val(),
	                  '']);

	table.fnDraw();
}

function getPersonsNamesOption(persons){
	var options="";
	var option = "<option id=\"{0}\" value=\"{0}\">{1}</option>"
		for (var index=0 ; index<persons.length ; index++ ){
			options += $.validator.format(option,persons[index][0],persons[index][2] + ' ' + persons[index][1]);
		}
	return options;
}

function getPersonsListOptions(persons){
	var options="";
	var option = "<option id=\"{0}\" value=\"{1}\"></option>"
		for (var index=0 ; index<persons.length ; index++ ){
			options += $.validator.format(option,persons[index][0],persons[index][2] + ' ' + persons[index][1]);
		}
	return options;
}

require(["dataTables"], function() {
	$(document).ready(function() {
		var location = window.location.href;
		$(window).load(function() {
			updatePhoto($('#caregiver-id').val());
		});
		var text = location.slice(location.lastIndexOf('/')+1, location.indexOf("?"));
		$.get('/elim/rest/persons/names', function(persons){
			options=getPersonsNamesOption(persons);
			// personsListOptions = getPersonsListOptions(persons);
		});



		if ($('#person-id').html() != text)
			updateCaregiver(text);

		$('#caregiver-data-update').dataTable({
			"iDisplayLength": 10
		});
		$('.caregiver-data tr').live("click",function(e) {
			unselectAllRows(this, e);
			selectRow(this, e);
			updateForm();
		});

		$("#update-button").live("click", function(event) {
			$.post('/elim/rest/caregivers/update',
					$('form').serialize(),
					function(data){
				showResult(data);
				if (data.success == true) {
					uploadPhoto(data.id);
					updateTableItem();
				}
			}
			).error(function(error,status,errorThrown){
				handleError(error,status,errorThrown);
			});
		});			    		

		$("#save-button").live("click", function(event) {
			// $.get('/elim/caregivers/save');
			$.post('/elim/rest/caregivers/save',
					$('form :input[id!="id"][id!="caregiver-id"]').serialize(),
					function(data){ 
				showResult(data);
				if (data.success == true) {
					if ($('#photo').val() == '')
						showDefaultPhoto();
					uploadPhoto(data.id);
					addTableItem();
				}
			}
			).error(function(error,status,errorThrown){
				handleError(error,status,errorThrown);
			});
		});

		$("#cancel-button").live("click", function(event) {
			if (confirm(jQuery.i18n.prop('ui.caregivers.confirm_quit')))
				listcaregivers();
		});

		$("#person-fields input,textarea,select").live("change", function(event) {
			removeErrorClass(event);
			removeResultDiv();
		});

		$('#caregiver-update-form').live('keydown', 'Ctrl+s',function(event){
			$.post('/elim/rest/caregivers/update',
					$('form').serialize(),
					function(data){
				showResult(data);
			}
			).error(function(error,status,errorThrown){
				handleError(error,status,errorThrown);
			});
		});

		$('#photo').live('change', function(event) {
			previewPhoto(this);
		});
		$('#caregiver-update-form').bind('keydown', 'Ctrl+down',function(event){
			selectNextRow();
			updateForm();
		});

		$('#caregiver-update-form').bind('keydown', 'Ctrl+up',function(event){
			selectPreviousRow();
			updateForm();
		});
	});
	/*CTRL + down arrow*/
	$.ctrlKey(40, function() {
		selectNextRow();
		updateForm();
	}, []);
	/*CTRL + down arrow*/
	$.ctrlKey(38, function() {
		selectPreviousRow();
		updateForm();
	}, []);
});
