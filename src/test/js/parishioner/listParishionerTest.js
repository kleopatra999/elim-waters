require(["logintest", "parishioner/parishionerTable", "parishionerMocks"], 
		function(logintest,parishionerTable, parishionerMocks) {
	var init=function(){
		require(["parishioner/listParishioner"], function(){
			console.log('Loaded list parishioner script...');
			QUnit.start();
		});
	}
	// stop tests until login test succeeds
	module("parishioner", {
		setup: function() {
			// one for login and one to load Datatable
			QUnit.stop();
			//QUnit.stop();
			parishionerMocks.mockAll();
			//logintest.login(init);
			require(["parishioner/listParishioner"], function(){
				console.log('Loaded list parishioner script...');
				QUnit.start();
			});
			console.log("Parishioner module setup...");
		},
		teardown: function() {
			console.log("Tear down ok!");
		}
	});

	test("test switch membership from betel to elim", function(){
		console.log("testing switch membership");
		$(document).ready(function(){
			var elem = $('#betel-member').filter(':first');
			equal(elem.attr("id"), "betel-member","current id is betel-member");
			parishionerTable.switchMembership(elem);
			equal(elem.attr("id"), "elim-member","betel membership switched successfully");
		});
		
	});
	
	test("test process data: first name contains ioan",function(){
		var table = $(document).find('#parishioner-data-list').dataTable();	    
		var oParams = table.oApi._fnAjaxParameters(table.fnSettings());
		var aParams = parishionerTable.processData(oParams);
		console.log(aParams);
	});
});
