package org.example.enumeration;

public enum TypeBien {
    APPARTEMENT, VILLA, MAISON, HANGAR;

    public boolean equalsIgnoreCase(String critere) {
        return this.name().equalsIgnoreCase(critere);
    }
}


