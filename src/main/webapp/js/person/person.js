define(['dataTables'], function(){
	return {
		personsNames: [],
		setPersonsNames: function(persons){
			var _personsNames=[];
			for (var index=0 ; index<persons.length ; index++ ){
				_personsNames.push(persons[index][1] + ' ' + persons[index][2]);
			}
			this.personsNames=_personsNames;
		},
		getPersonsNames: function(){
			if(this.personsNames.length==0){
				var persons=this.getPersons();
				this.setPersonsNames(persons);
			}
			return this.personsNames;
		},
		getPersons: function(){
			var _persons=[];
			$.ajax({
				type:"GET",
				async:false,
				url:'/elim/rest/persons/names',
				contentType:'application/json',
				success:function(persons){
					_persons = persons;
				},
				error:function(error,status, errorThrown){
					showError(error);
					return null;
				}});
			return _persons;
		}
	}
});
