require(["ajaxFileUpload"], function() {
	function ajaxFileUpload(elementId, url)
	{
		$.ajaxFileUpload
		(
			{
				url:url,
				secureuri:false,
				fileElementId:'uploadExcelFile',
				dataType: 'json',
				contentType:'application/vnd.ms-excel',
				beforeSend:function()
				{
					$("#loading").show();
				},
				complete:function()
				{
					$("#loading").hide();
				},				
				success: function (data)
				{
					alert(data);
				},
				error: function (e)
				{
					alert(e);
				}
			}
		)
		
		return false;

	}
	function upload(file,url){
		$.post(url,
			"file="+file,
			function(data) {
				window.alert(data);
			}
		);

	}
});



