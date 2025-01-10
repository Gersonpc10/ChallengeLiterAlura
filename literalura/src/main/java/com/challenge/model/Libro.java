package com.challenge.model;

import com.challenge.enums.IdiomasEnum;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    @ManyToOne(cascade = CascadeType.ALL)
 //   @ManyToOne
    @JoinColumn(name = "autor_id")
    @JsonManagedReference
    private Autor autor;

    @Enumerated(EnumType.STRING)
    private IdiomasEnum idiomas;
    private Double numeroDeDescargas;

    //Construtor
    public Libro(){}

    //Construtor personalizado
    public Libro(DatosLibro datosLibro) {
        this.id = (long) datosLibro.id();
        this.titulo = datosLibro.titulo();
        //this.autor = datosLibro.autor();
        // Lidar com o autor
        if (datosLibro.autor() != null && !datosLibro.autor().isEmpty()) {
            this.autor = new Autor(datosLibro.autor().get(0));
        }
        //this.idiomas = datosLibro.idiomas();
        if (datosLibro.idiomas() != null && !datosLibro.idiomas().isEmpty()) {
            try {
                this.idiomas = IdiomasEnum.fromString(datosLibro.idiomas().get(0));
            } catch (IllegalArgumentException e) {
                // Lidar com o caso em que o idioma não é reconhecido
                // Você pode definir um valor padrão ou lançar uma exceção
                System.out.println("Idioma não reconhecido: " + datosLibro.idiomas().get(0));
                this.idiomas = IdiomasEnum.ES; // Idioma espanhol por padrão
            }
        }
        this.numeroDeDescargas = datosLibro.numeroDeDescargas();
    }

    //Getter
    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }
    public Autor getAutor() {
        return autor;
    }

//    public List<String> getIdiomas() {
//        return idiomas;
//    }


    public IdiomasEnum getIdiomas() {
        return idiomas;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    //Setter
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

//    public void setIdiomas(List<String> idiomas) {
//        this.idiomas = idiomas;
//    }


    public void setIdiomas(IdiomasEnum idiomas) {
        this.idiomas = idiomas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    @Override
    public String toString() {
        return  " Titulo='" + titulo + '\'' +
                ", Numero de descargas=" + numeroDeDescargas + '\'' +
                ", Idiomas=" + idiomas + '\'' +
                ", Datos del autor='" + autor ;

    }
}
