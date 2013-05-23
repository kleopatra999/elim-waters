function deleteCaregiver(id){
	return $.ajax({
		type:"POST",
		async:false,
		url:'/elim/rest/caregivers/delete/' + id,
		success:function(data){
			return true;
		},
		error:function(error,status, errorThrown){
			handleError(error,status,errorThrown);
			return false;
		}});
}

/* Delete selected caregivers.
 */
function deleteSelectedcaregivers(){
	if (confirm(jQuery.i18n.prop('ui.caregivers.confirm_delete_selected_caregivers'))){
		var deleted=true;
		$('.row_selected').each(function() {
			var firstColumn = $(this).find('td:eq(0)');
			var count=firstColumn.text();
			var table = $(document).find('#caregiver-data-list').dataTable();
			var row = table.fnGetData(count-1);
			var id= row.id;
			deleted=deleteCaregiver(id);
			if(deleted==true||deleted.statusText=="OK"){
				var row = $(this).closest("tr").get(0);
				var table = $(document).find('#caregiver-data-list').dataTable();
				var index = table.fnGetPosition(row);
				console.log("Deleting " + index);
				table.fnDeleteRow(row,null,true);
			}else{
				return false;
			}
		});
		if(deleted==true||deleted.statusText=="OK"){
			notifyBar(jQuery.i18n.prop('ui.caregivers.delete_selected_caregivers_success'));
		}
	}
}
require(["dataTables"], function(){
	$(document).ready(function() {
		$("#deleteCaregivers").live("click", function(event) {
			deleteSelectedcaregivers();
		});	
		$("#deleteBtn").live("click", function(event) {
			//deleteSelectedcaregivers();
			if (confirm(jQuery.i18n.prop('ui.caregivers.confirm_delete_single_caregiver'))){
				var tr = $(this).closest("tr");
				var firstColumn = tr.find('td:eq(0)');
				var count=firstColumn.text();
				var table = $(document).find('#caregiver-data-list').dataTable();
				var row = table.fnGetData(count-1);
				var id= row.id;
				var deleted = deleteCaregiver(id);
				if(deleted==true||deleted.statusText=="OK"){
					var row = tr.get(0);
					var table = $(document).find('#caregiver-data-list').dataTable();
					var index = table.fnGetPosition(row);
					console.log("Deleting " + index);
					table.fnDeleteRow(row,null,true);
					notifyBar(jQuery.i18n.prop('ui.caregivers.delete_single_caregiver_success'));
				}
			}
		});
	} );
});