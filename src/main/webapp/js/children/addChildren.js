var _children = [];
var _persons=[];
var childBirthDateInput =
	"<input type=\"text\" class=\"datePicker\" " +
	"onclick=\"$(this).datepicker().datepicker('show')"+
	"id=\"child-birth-date\" placeholder=\"mm/dd/yyyy\" autofocus=\"autofocus\"/>";

var childItem = 
	"<li>" +
	"<div id=\"child\">" +
	"<a class=\"child-name\" id=\"{0}\" href=\"\">{1}</a>" + 
	"</div>" +
	"<a class=\"remove-child-button small-edit-button\" id=\"{0}\">x</a>" +
	"</li>";
var childItem1 = 
	"<li>" +
	"<div id=\"child\">" +
	"<a class=\"child-name\" href=\"\">{0} | {1}</a>" + 
	"</div>" +
	"<a class=\"remove-child-button small-edit-button\" id=\"{0}\" >x</a>" +
	"</li>";

function resetFields(){
	$('#child-name').attr('value','');
	$('#child-birth-date').attr('value','');
}

function getChildrenNames(){
	_names=[];
	for ( var i = 0; i < _children.length; i++) {
		_names.push(_children[i].childName+'|'+_children[i].birthDate);
	}
	return _names;
}

function Children(childName, birthDate){
	this.childName=childName;
	this.birthDate=birthDate;
}
function _getBirthDate(){
	return $('#child-birth-date').attr('value');
}

function getChildBirthDate(childName){
	var birthDate='';
	for ( var i = 0; i < _persons.length; i++) {
		if (childName.indexOf(_persons[i][1]) != -1 &&
				childName.indexOf(_persons[i][2]) != -1) {
			birthDate = _persons[i][3];
			break;
		}
	}
	if(birthDate === ''){
		return birthDate = _getBirthDate();
	}
	return $.datepicker.formatDate('mm/dd/yy', new Date(birthDate));
}

function _addChild(childName){
	var birthDate = getChildBirthDate(childName);
	var child = new Children(childName, birthDate);
	_children.push(child);
	_updateChildrenList();
}

function _removeChild(childName) {
	for ( var index = 0; index < _children.length; index++) {
		if (_children[index].childName == childName) {
			_children.splice(index, 1);
		}
	}
	_updateChildrenList();
}

function _removeAll() {
	_children=[];
	_updateChildrenList();
}



function _getChildrenList(idParent,children){
	var items="";
	var item = childItem;
	// var item = "<li id=\"child{0}\" href=\"\">{1}</li>"
	if(children != null){
		for (var index=0 ; index<children.length ; index++ ){
			items += $.validator.format(childItem,children[index].id, children[index].firstName + ' ' + children[index].lastName);
		}
	}
	return items;
}


function _updateChildrenList(){
	var newList="";
	for (var index=0 ; index<_children.length ; index++ ){
		newList+=$.validator.format(childItem1,_children[index].childName, _children[index].birthDate);
	}
	$('.children-list').html(newList);
	// reset value in input field
	resetFields();
}

define(['dataTables', "person/person"], function(dataTables, person){
	$(document).ready(function() {
		_persons=person.getPersons();
		$('#child-name').autocomplete({
			select: function (event) {
				if (event.which == 13) {
					$("#add-child-button").trigger("click");
					resetFields();
					// cancel the event, otherwise we can't reset field value
					event.preventDefault();
					return false;
				}
			}
		});
		$('#child-birth-date').keypress(function (e) {
			if (e.which == 13) {
				$("#add-child-button").trigger("click");
				resetFields();
			}
		});
	})
	return {

		deleteChildren: function(parentId,childId){
			return $.ajax({
				type:"POST",
				async:false,
				url:'/elim/rest/persons/'+parentId+'/children/delete/' + childId,
				success:function(data){
					console.log(data);
					return true;
				},
				error:function(error,status, errorThrown){
					showError(error);
					return false;
				}});
		},
		getChildrenList: _getChildrenList,
		updateChildren: function(id){
			$.get('/elim/rest/persons/'+id+'/children',
					function(children){
				$('.children-list').html(_getChildrenList(id, children));
			});
		},
		addChild: _addChild,
		removeChild: _removeChild,
		removeAll: _removeAll,
		saveChildren: function(parentId, callback){
			$.ajax({
				type: "POST",
				url: '/elim/rest/persons/'+parentId+'/children/add-list',
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				data: JSON.stringify(getChildrenNames()),
				success: function(_children){
					console.log("The children were added succsessfully for user " +
							"with id " + parentId + "!");
				},
				error: function(error,status,errorThrown){
					showError(error);
				}
			});
		},
		resetFields: resetFields
	}
});
