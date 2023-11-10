package it.voltats.gestionepista.db.entity.model;

public enum Promotions {
    NOTTURNA(0.50),
    FESTIVO(0.25),
    OTTO_ORE(0.25),
    SEI_ORE(0.20),
    QUATTRO_ORE(0.10);

    public final double value;

    private Promotions(double value) {
        this.value = value;
    }

    public double getValue(){
        return value;
    }
}
