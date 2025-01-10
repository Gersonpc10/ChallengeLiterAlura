package com.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.challenge.enums.IdiomasEnum;
import com.challenge.model.Autor;
import com.challenge.model.Libro;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByTituloContainsIgnoreCase(String titulo);

    List<Libro> findByIdiomas(IdiomasEnum idiomasEnum);

    @Query("SELECT DISTINCT l.autor FROM Libro l")
    List<Autor> findAllUniqueAutores();

    @Query("SELECT DISTINCT l.autor FROM Libro l WHERE l.autor.anioNacimiento <= :anio AND (l.autor.anioFallecimiento IS NULL OR l.autor.anioFallecimiento > :anio)")
    List<Autor> findByYearAutores(@Param("anio") int anio );
}
