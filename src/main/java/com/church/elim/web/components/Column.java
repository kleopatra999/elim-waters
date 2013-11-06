package com.church.elim.web.components;

/**
 * User: Adrian Dolha
 * Date: 11/5/13
 * Time: 8:46 PM
 */
public enum Column {
    ADDRESS("address", ColumnType.ADDRESS),
    BAPTISM_DATE("baptismDate", ColumnType.DATE),
    BIRTH_DATE("birthDate", ColumnType.DATE),
    CHURCH("church", ColumnType.STRING),
    CNP("cnp", ColumnType.CNP),
    COMPANY("company", ColumnType.STRING),
    EMAIL("email", ColumnType.EMAIL),
    FIRST_NAME("firstName", ColumnType.STRING),
    IMAGE("image", ColumnType.IMAGE),
    JOB("job", ColumnType.STRING),
    LAST_NAME("lastName", ColumnType.STRING),
    MARITAL_STATUS("maritalStatus", ColumnType.STRING),
    MARRIAGE_DATE("marriageDate", ColumnType.STRING),
    PHONE("phone", ColumnType.PHONE),
    PHOTO("photo", ColumnType.IMAGE),
    SPOUSE_NAME("spouseName", ColumnType.STRING),
    SPOUSE_RELIGION("spouseReligion", ColumnType.STRING),
    STUDIES("studies", ColumnType.STRING),
    WORKPLACE("workplace", ColumnType.STRING);
    private String name;
    private String displayName;
    private ColumnType type;

    private boolean isSortable;

    private boolean isSearchable;
    Column(String name, ColumnType type) {
        this.name = name;
        this.displayName = name;
        this.isSearchable = true;
        this.isSortable = true;
    }
    Column(String name, String displayName, ColumnType type) {
        this.name = name;
        this.displayName = displayName;
        this.type = type;
    }

    Column(String name, ColumnType type, boolean sortable, boolean searchable) {
        this(name, type);
        isSortable = sortable;
        isSearchable = searchable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ColumnType getType() {
        return type;
    }

    public void setType(ColumnType type) {
        this.type = type;
    }

    public boolean isSortable() {
        return isSortable;
    }

    public void setSortable(boolean sortable) {
        isSortable = sortable;
    }

    public boolean isSearchable() {
        return isSearchable;
    }

    public void setSearchable(boolean searchable) {
        isSearchable = searchable;
    }
}
