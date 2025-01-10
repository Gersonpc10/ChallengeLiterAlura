package com.challenge.enums;

public enum IdiomasEnum {
    ES("es", "es - Espanhol"),
    EN("en", "en - Inglês"),
    PT("pt", "pt - Português"),
    FR("fr", "fr - Francês"),
    IT("it", "it - Italiano");

    private String idiomas;
    private String expresionEnEspanol;

    IdiomasEnum(String idiomas, String expresionEnEspanol) {
        this.idiomas = idiomas;
        this.expresionEnEspanol = expresionEnEspanol;

    }

    IdiomasEnum(String idiomas) {
        this.idiomas = idiomas;
    }

    public String getExpresionEnEspanol() {
        return expresionEnEspanol;
    }

    public static IdiomasEnum fromString(String text) {
        for (IdiomasEnum idiomasEnum : IdiomasEnum.values()) {
            if (idiomasEnum.idiomas.equalsIgnoreCase(text)) {
                return idiomasEnum;
            }
        }
        throw new IllegalArgumentException("Nenhum idioma encontrado: " + text);
    }
    public static IdiomasEnum fromEspanol(String text) {
        for (IdiomasEnum idiomasEnum : IdiomasEnum.values()) {
            if (idiomasEnum.expresionEnEspanol.equalsIgnoreCase(text)) {
                return idiomasEnum;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada: " + text);
    }
}
