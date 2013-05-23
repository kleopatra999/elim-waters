var options="";
var personsListOptions="";
function updateBaptist(parishionerId,baptistName){
	// don't update if default baptist
	if(baptistName == ''){
		return "";
	}
	return $.ajax({
		type:"POST",
		url:'/elim/rest/parishioners/'+parishionerId+'/baptist/update',
		contentType: "application/json; charset=utf-8",
		dataType: "json",
		data:baptistName,
		success:function(data){
			console.log(data);
			return true;
		},
		error:function(error,status, errorThrown){
			showError(error);
			return false;
		}});
}

function getPersonsNamesOption(persons){
	var options="<option id=\"default\" value=\"none\"></option>";
	var option = "<option id=\"{0}\" value=\"{0}\">{1}</option>"
		for (var index=0 ; index<persons.length ; index++ ){
			options += $.validator.format(option,persons[index][0],persons[index][2] + ' ' + persons[index][1]);
		}
	return options;
}

function getBaptistsList(persons){
	var baptists=[""];
	for (var index=0 ; index<persons.length ; index++ ){
		baptists.push(persons[index][1] + ' ' + persons[index][2]);
	}
	return baptists;
}
