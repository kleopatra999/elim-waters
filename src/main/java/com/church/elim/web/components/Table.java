package com.church.elim.web.components;

import java.util.List;

/**
 * @author Adrian Dolha
 */
public class Table extends Component {
    protected List<Column> columns;
    protected Column sortColumn;
    protected List records;
    public Table(List records) {
        this.columns = columns;
        this.sortColumn = sortColumn;
        this.records = records;
    }
    public Table(List<Column> columns, Column sortColumn, List records) {
        this.columns = columns;
        this.sortColumn = sortColumn;
        this.records = records;
    }

    public List getRecords() {
        return records;
    }

    public void setRecords(List records) {
        this.records = records;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public Column getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(Column sortColumn) {
        this.sortColumn = sortColumn;
    }
}
