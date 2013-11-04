require(["dataTables", "baptist/baptistsList", "person/person", "children/addChildren"], function (dataTables, baptistsList, person, children) {
    $(document).ready(function () {
        $("#add-child-button").live("click", function (event) {
            //var childId = $('#persons-list option:selected').attr('id');
            var childName = $('#child-name').val();
            children.addChild(childName);
            children.resetFields();
        });

        $(".remove-child-button").live("click", function (event) {
            var childName = $(this).attr("id");
            children.removeChild(childName);
        });
        var personsNames = person.getPersonsNames();
        $("#child-name").autocomplete({
            source: personsNames
        });

        $('.datePicker').datepicker({
            changeMonth: true,
            changeYear: true
        });

        $("#baptist").autocomplete({
            source: personsNames
        });

        $("#spouseName").autocomplete({
            source: personsNames
        });

        $("#save-button").live("click", function (event) {
            $.post('/elim/rest/parishioners/save',
                $('form').serialize(),
                function (data) {
                    var baptistName = $('#baptist').val();
                    updateBaptist(data.id, baptistName);
                    $("#baptist").autocomplete({
                        source: personsNames
                    });
                    if ($('#photo').val() == '')
                        showDefaultPhoto();
                    uploadPhoto(data.id);
                    // Time to save the children
                    children.saveChildren(data.id);
                    children.removeAll();
                    showResult(data, $('form'));
                }
            );
        });

        $("#cancel-button").live("click", function (event) {
            if (confirm(jQuery.i18n.prop('ui.parishioners.confirm_quit')))
                listParishioners();
        });

        $("#person-fields input,textarea,select").live("change", function (event) {
            removeErrorClass(event);
            removeResultDiv();
        });

        $('#photo').live('change', function (event) {
            previewPhoto(this);
        });
    });
});