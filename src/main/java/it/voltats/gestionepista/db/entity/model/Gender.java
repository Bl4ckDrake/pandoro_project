package it.voltats.gestionepista.db.entity.model;

public enum Gender {
    F("F"),
    M("M");

    private final String value;

    private Gender(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return value;
    }
}
