package com.church.elim.web.components;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Adrian Dolha
 * Date: 11/5/13
 * Time: 8:28 PM
 */
public class ParishionerTable extends PersonTable{
    private Column[] columns = new Column[]{
        Column.CHURCH,
        Column.BAPTISM_DATE
    };
    public ParishionerTable(List records) {
        super(records);
        List<Column> columns = getColumns();
        for(Column column: this.columns){
            columns.add(column);
        }
    }
}
