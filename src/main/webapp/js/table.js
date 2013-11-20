function DataTable(table, options, searchForm, viewForm) {
    var inst = this;
    this.url = options.url;
    this.table = table;
    this.tableData = {
        view:{
            name:'',
            displayName:''
        },
        metadata: {
            columns: this.getColumns()
        },
        search: ''
    };
    this.searchForm = searchForm;
    this.viewForm = viewForm;
    $('.table-heading th a', table).on('click', function () {
        inst.sort($(this));
    });
    if (viewForm) {
        viewForm.submit(function (e) {
            e.preventDefault();
        });
        $('select', viewForm).on('change', function () {
            inst.setView($(this).val());
        });
    }
    if (searchForm) {
        searchForm.submit(function (e) {
            e.preventDefault();
        });
        $('input[name="search"]', searchForm).keypress(function (e) {
            if (e.which === 13) {
                inst.setSearch($(this).val());
            }
        });
    }
}

DataTable.prototype.sort = function (header) {
    var sortAsc = true;
    $('.headerSortUp', this.table).each(function () {
        $(this).removeClass('headerSortUp');
        sortAsc = false;
    });
    $('.headerSortDown', this.table).each(function () {
        $(this).removeClass('headerSortDown');
    });
    header.addClass(sortAsc ? 'headerSortUp' : 'headerSortDown');
};

DataTable.prototype.setView = function (viewName) {
    this.tableData.view.name = viewName;
    this.updateTable();
};

DataTable.prototype.setSearch = function (searchText) {
    this.tableData.search = searchText;
    this.updateTable();
};

DataTable.prototype.getColumns = function () {
    var columns = [];
    $('.header', this.table).each(function(){
        columns.push({
            name: $(this).text()
        });
    });
    return columns;
};

DataTable.prototype.updateTable = function () {
    var table = this.table;
    $.ajax({url: this.url,
        data: JSON.stringify(this.tableData),
        contentType: "application/json",
        dataType: 'html',
        type: 'POST',
        success: function (data) {
            console.log('success');
            $(table).html(data);
        }
    });
};

$.fn.dataTable = function (options, searchForm, viewForm) {
    return new DataTable(this, options, searchForm, viewForm);
};

