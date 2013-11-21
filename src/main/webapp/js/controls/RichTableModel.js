function RichTableModel(richTableData) {
    if (richTableData === null) {
        this.view = {
            name: '',
            displayName: ''
        };
        this.metadata = {
            columns: this.getColumns()
        };
        this.search = '';
    }
}

