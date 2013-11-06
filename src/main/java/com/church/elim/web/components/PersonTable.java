package com.church.elim.web.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Adrian Dolha
 * Date: 11/5/13
 * Time: 8:28 PM
 */
public class PersonTable extends Table{
    private Column[] columns = new Column[]{
           Column.FIRST_NAME,
           Column.LAST_NAME,
           Column.BIRTH_DATE,
           Column.ADDRESS,
           Column.PHONE,
           Column.STUDIES
    };
    public PersonTable(List records) {
        super(records);
        super.columns = new ArrayList<Column>();
        for(Column column: this.columns){
            super.columns.add(column);
        }
    }
}
