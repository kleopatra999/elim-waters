package com.church.elim.domain;

public enum MaritalStatus {
	NONE ( "none" ),
    REMARRIED ( "remarried" ),
    MARRIED ( "married" ),
    SINGLE ( "single" ), 
    DIVORCED ( "divorced" ),
    WIDOWER ( "widow(er)" ),
    COHABITING ("cohabiting");
    private String maritalStatus;

    
    private MaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    @Override
    public String toString() {
        return maritalStatus;
    }
}
