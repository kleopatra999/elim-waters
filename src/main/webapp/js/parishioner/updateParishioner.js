var selectedRow = 1;
var persons=[""];
var baptists=[""];
var personsListOptions="";
var personsNames;

function updateForm(personsNames, children){
	var text = $('.row_selected').children(":first").html();
	selectedRow = $('.row_selected .row-count').html();
	if(text != null)
		updateParishioner(text, personsNames, children);
	$('#parishioner-update-form input').removeAttr("placeholder");
}

function updateParishioner(id, personsNames, children){
	$.get('/elim/rest/parishioners/update-form/'+id,
			function(data){
		if(data!=null){
			$("#parishioner-update-form").html(data);
			createDatePickers();
			var baptistID=$('#baptist-id').val();
			$("#baptist").autocomplete({
	            source: personsNames
	        });
			$("#child-name").autocomplete({
	            source: personsNames
	        });
			updatePhoto($('#parishioner-id').val());
			children.updateChildren(id);
		}
	});
}

function updateTableItem() {
	table = $('#parishioner-data-update').dataTable();
	table.fnUpdate($('#firstName').val(), selectedRow-1, 2);
	table.fnUpdate($('#lastName').val(), selectedRow-1, 3);
	table.fnDraw();
}

function addTableItem() {
	table = $('#parishioner-data-update').dataTable();
	var index = table.fnGetData().length + 1;
	table.fnAddData( [index,
	                  $('#firstName').val(),
	                  $('#lastName').val(),
	                  '']);

	table.fnDraw();
}

function getPersonsListOptions(persons){
	var option = "<option id=\"{0}\" value=\"{1}\"></option>"
		for (var index=0 ; index<persons.length ; index++ ){
			options += $.validator.format(option,persons[index][0],persons[index][2] + ' ' + persons[index][1]);
		}
	return options;
}

require(["dataTables","baptist/baptistsList","person/person", "children/addChildren"], function(dataTables,baptistsList,person, children) {
	$(document).ready(function() {
		var location = window.location.href;
		personsNames = person.getPersonsNames();
		console.log(person);
		$(window).load(function() {
			updatePhoto($('#parishioner-id').val());
		});
		var text = location.slice(location.lastIndexOf('/')+1, location.indexOf("?"));
		if ($('#person-id').html() != text)
			updateParishioner(text, personsNames, children);

		$('#parishioner-data-update').dataTable({
			"iDisplayLength": 10
		});
		$('.parishioner-data tr').live("click",function(e) {
			unselectAllRows(this, e);
			selectRow(this, e);
			updateForm(personsNames, children);
		});

		/*$('#baptist-select').live("change",function(e){
			console.log(e);
		});*/
		$("#update-button").live("click", function(event) {
			$.post('/elim/rest/parishioners/update',
					$('form').serialize(),
					function(data){
				if (data.success == true) {
					uploadPhoto(data.id);
					// var baptistId = $('.persons-names-option option:selected').attr('id');
					var baptistName = $('#baptist').val();
					updateBaptist(data.id, baptistName);
					updateTableItem();
				}
				showResult(data);
			}
			).error(function(error,status,errorThrown){
				handleError(error,status,errorThrown);
			});
		});			    		

		$("#save-button").live("click", function(event) {
			// $.get('/elim/parishioners/save');
			$.post('/elim/rest/parishioners/save',
					$('form :input[id!="id"][id!="parishioner-id"]').serialize(),
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
			if (confirm(jQuery.i18n.prop('ui.parishioners.confirm_quit')))
				listParishioners();
		});

		$("#person-fields input,textarea,select").live("change", function(event) {
			removeErrorClass(event);
			removeResultDiv();
		});

		$('#parishioner-update-form').live('keydown', 'Ctrl+s',function(event){
			$.post('/elim/rest/parishioners/update',
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
		$("#add-child-button").live("click", function(event) {
			//var childId = $('#persons-list option:selected').attr('id');
			var childName = $('#child-name').val();
			// console.log("Child to be added:" + childId);
			var parentId = $('#parishioner-id').val();
			$.ajax({
				type: "POST",
				url: '/elim/rest/persons/'+parentId+'/children/add',
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				data: childName,
				success: function(_children){
					var message = "Added "+_children.child.id+" to the parent " + _children.parent.id+"!"; 
					console.log(message);
					children.updateChildren(parentId);
					children.resetFields();
					showResult(message);
				},
				error: function(error,status,errorThrown){
					showError(error);
				}
			});
		});
		
		$(".remove-child-button").live("click", function(event) {
			var childId = $(this).attr('id');
			// console.log("Child to be added:" + childId);
			var parentId = $('#parishioner-id').val();
			$.ajax({
				type: "POST",
				url: '/elim/rest/persons/'+parentId+'/children/delete',
				contentType: "application/json; charset=utf-8",
				data: ""+childId+"",
				success: function(message){
					console.log(message);
					children.updateChildren(parentId);
					showResult(message);
				},
				error: function(error,status,errorThrown){
					showError(error);
				}
			});
		});
		
		$('#parishioner-update-form').bind('keydown', 'Ctrl+down',function(event){
			selectNextRow();
			updateForm(personsNames, children);
		});

		$('#parishioner-update-form').bind('keydown', 'Ctrl+up',function(event){
			selectPreviousRow();
			updateForm(personsNames, children);
		});
	});
	/*CTRL + down arrow*/
	$.ctrlKey(40, function() {
		selectNextRow();
		updateForm(personsNames, children);
	}, []);
	/*CTRL + down arrow*/
	$.ctrlKey(38, function() {
		selectPreviousRow();
		updateForm(personsNames, children);
	}, []);
});
