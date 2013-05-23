var lastSelected = null;
var lastShift = null;

function selectRow(elem, e){
	if (!lastSelected)
		lastSelected = elem;
		
	$(elem).addClass('row_selected');
}
function unselectAllRows(elem, e){
	if (e.ctrlKey){
		lastSelected = elem;
		lastShift = null;
		return;
	}
	
	if (e.shiftKey){
		if (!lastSelected)
			return;
			
	    var start = $(lastShift).find("td.row-count").text();
        var end = $(elem).find("td.row-count").text();
        
        if (lastShift)
        	$('tr').slice(Math.min(start,end), Math.max(start,end) + 1).removeClass('row_selected');
        
        start = $(lastSelected).find("td.row-count").text();
        $('tr').slice(Math.min(start,end), Math.max(start,end) + 1).addClass('row_selected');
        
        lastShift = elem;
        return;
	} 
	
	lastSelected = null;
	lastShift = null;
	$('.row_selected').removeClass('row_selected');
}

function selectNextRow(){
	var selectedRow = $('tr.row_selected');
	if(selectedRow==null){
		$('.row_selected').removeClass();
		$('table tbody').children(":first").addClass('row_selected');
	}else{
		$('.row_selected').removeClass().next().addClass('row_selected');
	}
}
function selectPreviousRow(){
	var selectedRow = $('tr.row_selected');
	if(selectedRow==null){
		$('.row_selected').removeClass();
		$('table tbody').children(":last").addClass('row_selected');
	}else{
		$('.row_selected').removeClass().prev().addClass('row_selected');
	}
}

function stringify_aoData(aoData) {
    var o = {};
    var modifiers = ['mDataProp_', 'sSearch_', 'iSortCol_', 'bSortable_', 'bRegex_', 'bSearchable_', 'sSortDir_'];
    jQuery.each(aoData, function(idx,obj) {
        if (obj.name) {
            for (var i=0; i < modifiers.length; i++) {
                if (obj.name.substring(0, modifiers[i].length) == modifiers[i]) {
                    var index = parseint(obj.name.substring(modifiers[i].length));
                    var key = 'a' + modifiers[i].substring(0, modifiers[i].length-1);
                    if (!o[key]) {
                        o[key] = [];
                    }
                    //console.log('index=' + index);
                    o[key][index] = obj.value;
                    //console.log(key + ".push(" + obj.value + ")");
                    return;
                }
            }
            //console.log(obj.name+"=" + obj.value);
            o[obj.name] = obj.value;
        }
        else {
            o[idx] = obj;
        }
    });
    return JSON.stringify(o);
};

require(["main"], function() {
	/* The following prevents test selection when the user selects multiple
	 * table rows using the SHIFT key. */
	$('table').mousedown(function (e) {
	    if (e.shiftKey) {
	        // For non-IE browsers
	        e.preventDefault();

	        // IE - it is so unadaptable
	        if ($.browser.msie) {
	            this.onselectstart = function () { return false; };
	            var me = this;  // capture in a closure
	            window.setTimeout(function () { me.onselectstart = null; }, 0);
	        }
	    }
	});

	/*CTRL + right arrow*/
	$.ctrlKey(39, function() {
		$('.paginate_enabled_next').trigger('click');
	}, []);
	/*CTRL + left arrow*/
	$.ctrlKey(37, function() {
		$('.paginate_enabled_previous').trigger('click');
	}, []);
}); 