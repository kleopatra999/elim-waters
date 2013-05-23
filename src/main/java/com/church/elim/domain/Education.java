package com.church.elim.domain;

public enum Education {
	NONE ( "none" ),
	POSTUNIVERSITY ( "post-university" ),
	UNIVERSITY ("university"),
	COLLEGE("college"),
    HIGHSCHOOL ( "highschool" ), 
    GYMNASIUM ( "gymnasium" );
    private String education;

    
    private Education(String education) {
        this.education = education;
    }

    @Override
    public String toString() {
        return education;
    }
}
