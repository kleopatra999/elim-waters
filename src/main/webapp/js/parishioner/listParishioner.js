require(["parishioner/parishionerTable"], function(parishionerTable) {
	$(document).ready(function() {
		console.log("The dataTable is:");
		console.log(parishionerTable.configureParishionerTable($('#parishioner-data-list')));
		$("#buttonPlaceholder").html('<input type="image" id="filterBtn" name="filterBtn" src="/elim/img/filter.png" title="Filter" alt="Filter" class="btnIcon" />');
		var options="";
		
	});

	$('tr').live("click",function(e){
		unselectAllRows(this,e);
		selectRow(this);
	});
	$('#parishioner-download').live("click",function(){
		var table = $(document).find('#parishioner-data-list').dataTable();	    
		var oParams = table.oApi._fnAjaxParameters(table.fnSettings());
		var aParams = parishionerTable.processData(oParams);
		console.log(oParams);
		console.log(aParams);
		parishionerTable.downloadParishioners(aParams);
	});
	$('.church-column').live("click",(function() {
		parishionerTable.switchMembership(this);
	}));
	$.ctrl('E', function() {
		$('.row_selected .church-column').each(
				function(){
					parishionerTable.switchMembership(this);
				}
		);
	}, []);

	$.ctrl('S', function() {
		var parishioners = parishionerTable.getSelectedParishioners();
		$.ajax({
			type: "POST",
			url: '/elim/rest/parishioners/update-church',
			contentType: "application/json; charset=utf-8",
			dataType: "json",
			data: JSON.stringify(parishioners)
		})
		.always(function(data) { notifyBar(data.result); });

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
