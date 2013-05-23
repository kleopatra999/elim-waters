function downloadParishioners(){
	$.ajax({
		type: "GET",
		url: '/elim/rest/parishioners/download/xls',
		contentType: "application/json; charset=utf-8",
		dataType: "json",
		data: condition
	});
}

function switchMembership(elem){
	if($(elem).is('#elim-member')){
		$(elem).attr("id","betel-member");			
	}else{
		$(elem).attr("id","elim-member");
	}
}

function getSelectedParishioners(){
	/*select first column of each selected row*/
	var parishioners=Array();
	$('.row_selected').each(function(index, value) {
		var firstColumn = $(this).find('td:eq(0)');
		var count=firstColumn.text();
		var table = $(document).find('#parishioner-data-list').dataTable();
		var row = table.fnGetData(count-1);
		var id= row.id;
		var church="betel";
		if($(this).find('.church-column').attr("id") == "elim-member"){
			church="elim";
		}
		parishioners.push(parishioner(id,church));
	});
	return parishioners;
}

function processData(aoData) {
	
	var conditions = [];
	$('input[type="checkbox"]:checked').each(function(index) {
		var condition = $(this).val();
		var field = condition.split('|')[0];
		var value = condition.split('|')[1];
		conditions[index] = {"field": field, "value": value};
	});
	
	aoData.push({ "name": "sComplexSearch", "value": conditions });

	return stringify_aoData(aoData);
}

require(["dataTables","parishioner/deleteParishioner"], function() {
	$(document).ready(function() {
		
		$('#parishioner-data-list').dataTable({
			//"sDom": 'f<"#buttonPlaceholder">t',
			"sDom": 'f<"#buttonPlaceholder">rtip',
			"aoColumns": getAdminViewCols(),
			"iDisplayLength": 25,
			"bProcessing": true,
			"bServerSide": true,
			"sAjaxSource": "/elim/rest/parishioners/data",
			"fnServerData": function ( sSource, aoData, fnCallback ) {
				$.ajax( {
					dataType: 'json',
					contentType: "application/json;charset=UTF-8",
					type: 'POST',
					url: sSource,
					data: processData(aoData),
					success: function(result){
						var data = result.aaData;
						// Add the retrieved data to the table
						var row, church;
						if(data!=null)
						{
							for (var index=0 ; index<data.length ; index++ )
							{
								row = data[index];
								row["count"]=index+aoData[3].value + 1;
								(row.church == 'elim') ? church = 'elim-member': church = 'betel-member';
								delete row.church;
								row["edit-action"] = '<a class="btnIcon" href="' + row.id + '?update\">\
								<img id="editBtn" name="editBtn" src="/elim/img/update.png" title="edit" alt="edit" class="btnIcon" \
								title="button.edit" alt="button.edit"/></a>';
								row["delete-action"] =  
									'<div>\
									<input type="hidden" name="page" value="1" />\
									<input type="hidden" name="size" value="10" />\
									<input type="image" id="deleteBtn" name="deleteBtn" src="/elim/img/delete.png" title="Delete" alt="Delete" class="btnIcon" title="button.delete" alt="button.delete"/>\
									</div>';
								row["church"] = '<div class="church-column" id="' + church + '"></div>';
							}
						}
						fnCallback(result);
					},
					error : function (e) {
						alert ("Error:" + e);
					}
				} );
			}
		});
		$("#buttonPlaceholder").html('<input type="image" id="filterBtn" name="filterBtn" src="/elim/img/filter.png" title="Filter" alt="Filter" class="btnIcon" />');
	});

	$('tr').live("click",function(e){
		unselectAllRows(this,e);
		selectRow(this);
	});
	$('#parishioner-download').live("click",function(){
		downloadParishioners();
	});
	$('.church-column').live("click",(function() {
		switchMembership(this);
	}));
	$.ctrl('E', function() {
		$('.row_selected .church-column').each(
				function(){
					switchMembership(this);
				}
		);
	}, []);

	$.ctrl('S', function() {
		var parishioners = getSelectedParishioners();
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
