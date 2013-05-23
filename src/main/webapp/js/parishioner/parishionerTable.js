define(['dataTables','parishioner/deleteParishioner'], function(){
	/* specify data table columns properties
	 * mDataProp - specifies which property from the aaData object should 
	 *	be assigned to this column. 
	 */
	var _adminViewCols = [ { "bSearchable": false,
		"bVisible":false,
		"mDataProp":"id"},
		{"sClass":"row-count",
			"bSortable":false,
			"mDataProp":"count"},
			{"mDataProp":"firstName"},
			{"mDataProp":"lastName"},
			{"mDataProp":"birthDate"},
			{"mDataProp":"address"},
			{"mDataProp":"phone"},
			{"mDataProp":"email"},
			{"mDataProp":"maritalStatus"},
			{"mDataProp":"studies"},
			{"mDataProp":"baptismDate"},
			{"mDataProp":"edit-action"},
			{"mDataProp":"delete-action"}
			];
	var _processData = function(aoData) {
		var conditions = [];
		$('input[type="checkbox"]:checked').each(function(index) {
			var condition = $(this).val();
			var field = condition.split('|')[0];
			var value = condition.split('|')[1];
			conditions[index] = {"field": field, "value": value};
		});

		aoData.push({ "name": "sComplexSearch", "value": conditions });

		return stringify_aoData(aoData);
	};
	return {
		adminViewCols: _adminViewCols,
		// start of functions
		downloadParishioners: function(aParams){
			// we must encode uri otherwise it complains for some characters like %
			var uri = encodeURI('/elim/rest/parishioners/download/xls?dataTablesRequest='+ aParams);
			window.open(uri);
		},
		//content is the data (a string) you'll write to file.
		//filename is a string filename to write to on server side.
		//This function uses iFrame as a buffer, it fills it up with your content
		//and prompts the user to save it out.
		save_content_to_file: function(content, filename){
			var dlg = false;
			with(document){
				ir=createElement('iframe');
				ir.id='ifr';
				ir.location='about.blank';
				ir.style.display='none';
				body.appendChild(ir);
				with(getElementById('ifr').contentWindow.document){
					open("text/plain", "replace");
					charset = "utf-8";
					write(content);
					close();
					document.charset = "utf-8";
					dlg = execCommand('SaveAs', false, filename);
				}
				body.removeChild(ir);
			}
			return dlg;
		},
		// elem the td from table
		switchMembership: function(elem){
			if($(elem).is('#elim-member')){
				$(elem).attr("id","betel-member");			
			}else{
				$(elem).attr("id","elim-member");
			}
		},

		getSelectedParishioners: function(){
			/*select first column of each selected row*/
			var parishioners=Array();
			$('.row_selected').each(function(index, value) {
				var firstColumn = $(this).find('td:eq(0)');
				var count=firstColumn.text();
				var table = $(document).find('#parishioner-data-list').dataTable();
				var row = table.fnGetData(count-1);
				var id= row.id;
			});
			return parishioners;
		},

		processData: _processData,

		configureParishionerTable: function(table){
			return table.dataTable({
				//"sDom": 'f<"#buttonPlaceholder">t',
				"sDom": 'f<"#buttonPlaceholder">rtip',
				"aoColumns": _adminViewCols,
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
						data: _processData(aoData),
						success: function(result){
							console.log("The data returned from the server:");
							console.log(result);
							var data = result.aaData;
							// Add the retrieved data to the table
							var row
							if(data!=null)
							{
								for (var index=0 ; index<data.length ; index++ )
								{
									row = data[index];
									row["count"]=index+aoData[3].value + 1;
									row["edit-action"] = '<a class="btnIcon" href="' + row.id + '?update\">\
									<img id="editBtn" name="editBtn" src="/elim/img/update.png" title="edit" alt="edit" class="btnIcon" \
									title="button.edit" alt="button.edit"/></a>';
									row["delete-action"] =  
										'<div>\
										<input type="hidden" name="page" value="1" />\
										<input type="hidden" name="size" value="10" />\
										<input type="image" id="deleteBtn" name="deleteBtn" src="/elim/img/delete.png" title="Delete" alt="Delete" class="btnIcon" title="button.delete" alt="button.delete"/>\
										</div>';
								}
							}
							fnCallback(result);
						},
						error : function (request, status, e) {
							console.log('An error has occured during data filtering!');
							console.log(request.responseText);
							console.log(status);
							console.log(e);
							alert ("Error: " + request.responseText);
						}
					} );
				}
			});
		}

		// end of functions
	}
});
