package com.church.elim.web.components;

/**
 * @author Adrian Dolha
 */
public enum ColumnType {
    INTEGER,
    DOUBLE ,
    STRING ,
    DATE ,
    IMAGE,
    FILE, ADDRESS, CNP, EMAIL, PHONE;
    private String type;
    ColumnType() {
        this.type = this.name().toLowerCase();
    }
    @Override
    public String toString(){
        return type;
    }
}
