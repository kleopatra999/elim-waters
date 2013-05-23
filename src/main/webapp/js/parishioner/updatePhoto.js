function showExistingPhoto(id) {
	replacePhoto('/elim/rest/parishioners/' + id + '/photo');
}

function showDefaultPhoto() {
	replacePhoto('/elim/images/no_photo.jpg');
}

function replacePhoto(source) {
	// save a new img element
	var photo = document.createElement('img');
	$(photo).attr('src', source);
	$(photo).attr('id', 'person-photo');
		
	// remove previous photo
	$("#photo-placehoder").empty();
		
	// add the img element
	$(photo).appendTo($("#photo-placehoder"));
}

function updatePhoto(id) {
	$.get('/elim/rest/parishioners/' + id + '/photo', function() {
	}).success(function() { 
		showExistingPhoto(id);
	}).error(function() { 
		showDefaultPhoto();
	});
}

function previewPhoto(input){
	if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('#person-photo').attr('src', e.target.result);
        }

        reader.readAsDataURL(input.files[0]);
    }
}

function uploadPhoto(id) {
	
	if ($('#photo').val() == "")
		return;
	
	$.ajaxFileUpload
    (
        {
            url: '/elim/rest/parishioners/' + id + '/photo',
            secureuri: false,
            fileElementId: 'photo',
            dataType: 'json',
            
            success: function (data, status){
                alert("Success: "+data);
            }
        }
    );
}