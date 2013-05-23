function deleteParishioner(id){
	return $.ajax({
		type:"POST",
		async:false,
		url:'/elim/rest/parishioners/delete/' + id,
		success:function(data){
			return true;
		},
		error:function(error,status, errorThrown){
			handleError(error,status,errorThrown);
			return false;
		}});
}

/* Delete selected parishioners.
 */
function deleteSelectedParishioners(){
	if (confirm(jQuery.i18n.prop('ui.parishioners.confirm_delete_selected_parishioners'))){
		var deleted=true;
		$('.row_selected').each(function() {
			var firstColumn = $(this).find('td:eq(0)');
			var count=firstColumn.text();
			var table = $(document).find('#parishioner-data-list').dataTable();
			var row = table.fnGetData(count-1);
			var id= row.id;
			deleted=deleteParishioner(id);
			if(deleted==true||deleted.statusText=="OK"){
				var row = $(this).closest("tr").get(0);
				var table = $(document).find('#parishioner-data-list').dataTable();
				var index = table.fnGetPosition(row);
				console.log("Deleting " + index);
				table.fnDeleteRow(row,null,true);
			}else{
				return false;
			}
		});
		if(deleted==true||deleted.statusText=="OK"){
			notifyBar(jQuery.i18n.prop('ui.parishioners.delete_selected_parishioners_success'));
		}
	}
}
require(["dataTables"], function(){
	$(document).ready(function() {
		$("#deleteParishioners").live("click", function(event) {
			deleteSelectedParishioners();
		});	
		$("#deleteBtn").live("click", function(event) {
			//deleteSelectedParishioners();
			if (confirm(jQuery.i18n.prop('ui.parishioners.confirm_delete_single_parishioner'))){
				var tr = $(this).closest("tr");
				var firstColumn = tr.find('td:eq(0)');
				var count=firstColumn.text();
				var table = $(document).find('#parishioner-data-list').dataTable();
				var row = table.fnGetData(count-1);
				var id= row.id;
				var deleted = deleteParishioner(id);
				if(deleted==true||deleted.statusText=="OK"){
					var row = tr.get(0);
					var table = $(document).find('#parishioner-data-list').dataTable();
					var index = table.fnGetPosition(row);
					console.log("Deleting " + index);
					table.fnDeleteRow(row,null,true);
					notifyBar(jQuery.i18n.prop('ui.parishioners.delete_single_parishioner_success'));
				}
			}
		});
	} );
});