package com.church.elim.domain;
public enum Church {
	ELIM ( "Elim" ),
	BETEL ("Betel");
    private String church;

    
    private Church(String church) {
        this.church=church;
    }

    @Override
    public String toString() {
        return church;
    }
}