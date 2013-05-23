define(['mockjax'],function(){
	var firstPage;
	$.ajax({
		url: '/elim/json/parishioner/firstPage.json',
		dataType: 'json',
		contentType:'application/json',
		async: false,
		success: function(data){
			console.log('whoooo working'); 
			firstPage = data;
		},
		error: function(o,c,m) { console.log('errror' + m); }
	});
	var _mockFirstPage = function(){
		$.mockjax({
			url: '/elim/rest/parishioners/data',
			status: 200,
			statusText:'OK',
			dataType: 'json',
			contentType:'application/json',
			responseText:firstPage
		});
	};
	
	var _mockAll = function(){
		_mockFirstPage();
	};
	//_mockAll();
	return {
		mockFirstPage: _mockFirstPage,
		mockAll: _mockAll
	}
});