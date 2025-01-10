package com.challenge.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autor")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nombreAutor;
    private Integer anioNacimiento;
    private Integer anioFallecimiento;
   // @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   @OneToMany(mappedBy = "autor", fetch = FetchType.EAGER)
   @JsonBackReference
    private List<Libro> libros = new ArrayList<>();

    //Construtor
    public Autor(){}

    //Construtor personalizado
    public Autor(DatosAutor datosAutor){
        //Permitimos que o banco de dados crie o ID do autor
        //this.id = (long) datosAutor.id();
        this.nombreAutor = datosAutor.nombreAutor();
        this.anioNacimiento = datosAutor.anioNacimiento();
        this.anioFallecimiento = datosAutor.anioFallecimiento();
    }

    // Construtor personalizado com lista de livros
    public Autor(DatosAutor datosAutor, List<Libro> libros) {
        this(datosAutor); // Chame o outro construtor para inicializar os dados do autor
        this.libros = libros;
        for (Libro libro : libros) {
            libro.setAutor(this); // Defina este autor como o autor de cada livro
        }
    }

    //Getters
    public String getNombreAutor() {
        return nombreAutor;
    }

    public Integer getAnioNacimiento() {
        return anioNacimiento;
    }

    public Integer getAnioFallecimiento() {
        return anioFallecimiento;
    }
    public List<Libro> getLibros() {
        return libros;
    }

    //Setters
    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public void setAnioNacimiento(Integer anioNacimiento) {
        this.anioNacimiento = anioNacimiento;
    }

    public void setAnioFallecimiento(Integer anioFallecimiento) {
        this.anioFallecimiento = anioFallecimiento;
    }

    public void setLibros(List<Libro> libros) {
        libros.forEach(e -> e.setAutor(this));
        this.libros = libros;
    }

    @Override
    public String toString() {
        return
                " Nome do autor='" + nombreAutor + '\'' +
                ", Data de Nascimento=" + anioNacimiento + '\'' +
                ", Data de Falecimento=" + anioFallecimiento + '\''
              //  ", Libros=" + libros +
                ;
    }
}
