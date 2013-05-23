require(["caregiver/caregiverTable"], function(caregiverTable) {
	$(document).ready(function() {
		console.log("The dataTable is:");
		console.log(caregiverTable.configurecaregiverTable($('#caregiver-data-list')));
		$("#buttonPlaceholder").html('<input type="image" id="filterBtn" name="filterBtn" src="/elim/img/filter.png" title="Filter" alt="Filter" class="btnIcon" />');
		var options="";
		
	});

	$('tr').live("click",function(e){
		unselectAllRows(this,e);
		selectRow(this);
	});
	$('#caregiver-download').live("click",function(){
		var table = $(document).find('#caregiver-data-list').dataTable();	    
		var oParams = table.oApi._fnAjaxParameters(table.fnSettings());
		var aParams = caregiverTable.processData(oParams);
		console.log(oParams);
		console.log(aParams);
		caregiverTable.downloadcaregivers(aParams);
	});
	$.ctrl('E', function() {
		$('.row_selected').each(
				function(){
					caregiverTable.switchMembership(this);
				}
		);
	}, []);

	/*CTRL + down arrow*/
	$.ctrlKey(40, function() {
		selectNextRow();
	}, []);
	/*CTRL + down arrow*/
	$.ctrlKey(38, function() {
		selectPreviousRow();
	}, []);
});
