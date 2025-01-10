package com.challenge.principal;

import java.util.*;
import java.util.stream.Collectors;

import com.challenge.enums.IdiomasEnum;
import com.challenge.model.Autor;
import com.challenge.model.Datos;
import com.challenge.model.DatosLibro;
import com.challenge.model.Libro;
import com.challenge.repository.LibroRepository;
import com.challenge.services.ConsumoAPI;
import com.challenge.services.ConvierteDatos;


public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosLibro> datosLibros = new ArrayList<>();
    private List<Libro> libros;
    private List<Autor> autor;

    //Inyeccion de dependencias
    private LibroRepository repositorio;
    public Principal(LibroRepository repository) {
        this.repositorio = repository;
    }

    public void muestraElMenu() {
//        var json = consumoAPI.obtenerDatos(URL_BASE);
//        System.out.println(json);
//        var datos = conversor.obtenerDatos(json, Datos.class);
//        System.out.println(datos);

        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    \n *** Selecione uma opção abaixo *** \n
                    1 - Buscar livro pelo título 
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros em um determinado idioma
                    0 - Sair
                    """;
            System.out.println(menu);
            try {
                opcion = teclado.nextInt();
                teclado.nextLine();

                switch (opcion) {
                    case 1:
                        System.out.println("\uD83D\uDCD6 Buscar livro pelo título ");
                        buscarLibroWeb();
                        break;
                    case 2:
                        System.out.println("\uD83D\uDCD6 Listar livros registrados ");
                        mostrarLibrosBuscados();
                        break;
                    case 3:
                        System.out.println("\uD83D\uDCD6 Listar autores registrados \n");
                        listaDeAutores();
                        break;
                    case 4:
                        System.out.println("\uD83D\uDCD6 Listar autores vivos em um determinado ano");
                        autoresPorAnio();
                        break;
                    case 5:
                        System.out.println("\uD83D\uDCD6 Listar livros em um determinado idioma ");
                        buscarLibroPorIdioma();
                        break;
                    case 0:
                        System.out.println("Aplicação Encerrada.");
                        break;
                    default:
                        System.out.println("Opção inválida, selecione outra opção");
                }
            } catch(InputMismatchException e){
                System.out.println("Entrada inválida. Por favor, insira um número entre 1 à 5.");
                teclado.nextLine();
            }
        }


    }


    private DatosLibro getDatosLibros() {
        System.out.println("\n *** Digite o nome do livro que deseja procurar. *** \n");
        var nombreLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "%20"));
        //System.out.println(json);

        Datos datos = conversor.obtenerDatos(json, Datos.class);
        List<DatosLibro> libros = datos.resultados();
//        datos.resultados().stream()
//                        .forEach(System.out::println);
//        System.out.println(datos);

        if (!libros.isEmpty()) {
            return libros.get(0);
        } else {
            return null; // Se nenhum livro for encontrado, retornar null.
        }
    }
    private void buscarLibroWeb() {
        DatosLibro datos = getDatosLibros();
        if(datos != null) {
            Optional<Libro> tituloYaExiste = repositorio.findByTituloContainsIgnoreCase(datos.titulo());

            if(tituloYaExiste.isPresent()){
                System.out.println("\n *** O livro já existe no banco de dados ***\n");
            } else{
                Libro libro = new Libro(datos);
                repositorio.save(libro);
                System.out.println("\n *** Pasta de trabalho adicionada ao banco de dados ***\n");
                //datosLibros.add(datos);
                System.out.println(" *** *** *** *** \n");
                System.out.printf("""
                    Titulo: %s
                    Idioma: %s
                    Numero de download: %s
                    Autor: %s ( %s - %s ) \n
                    """, datos.titulo(), IdiomasEnum.fromString(datos.idiomas().get(0)).getExpresionEnEspanol(), datos.numeroDeDescargas(),
                        datos.autor().get(0).nombreAutor(),
                        (datos.autor().get(0).anioNacimiento() != null ) ? datos.autor().get(0).anioNacimiento(): "Data de nascimento desconhecida",
                        (datos.autor().get(0).anioFallecimiento() != null ) ? datos.autor().get(0).anioFallecimiento() : " " );
                System.out.println(" *** *** *** *** \n");

            }

        } else {
            System.out.println("\n *** O livro ou autor que você está tentando pesquisar não foi encontrado *** \n");
        }

       // System.out.println("datos "+datos);

    }

    private void mostrarLibrosBuscados() {
        //datosSeries.forEach(System.out::println);
        libros = repositorio.findAll();
        //List<Serie> series = repositorio.findAll();
//        List<Serie> series = new ArrayList<>();
//        series = datosSeries.stream()
//                .map(d -> new Serie(d))
//                .collect(Collectors.toList());
            if (libros.isEmpty()) {
                System.out.println("\n *** Nenhum livro no banco de dados*** \n");
            } else {
                System.out.println("\n *** *** *** *** ");
                libros.stream()
                        .sorted(Comparator.comparing(Libro::getTitulo))
                        .forEach(l -> System.out.printf("""
                                Livro: %s
                                Idioma: %s
                                Numero de download: %s
                                Autor: %s \n
                                """, l.getTitulo(), l.getIdiomas().getExpresionEnEspanol(), l.getNumeroDeDescargas(), l.getAutor().getNombreAutor()));
                System.out.println(" *** *** *** *** \n");
            }

    }

    private void buscarLibroPorIdioma(){

        System.out.println("\n *** Selecione o idioma do livro que deseja pesquisar*** \n");
        System.out.println("1. Espanhol");
        System.out.println("2. Inglês");
        System.out.println("3. Português");
        System.out.println("4. Francês");
        System.out.println("5. Italiano");
        System.out.print("\n  Digite o número da opção desejada:  ");
        try {
            int opcion = Integer.parseInt(teclado.nextLine());
            IdiomasEnum idiomaSeleccionado;

            switch (opcion) {
                case 1:
                    idiomaSeleccionado = IdiomasEnum.ES;
                    break;
                case 2:
                    idiomaSeleccionado = IdiomasEnum.EN;
                    break;
                case 3:
                    idiomaSeleccionado = IdiomasEnum.PT;
                    break;
                case 4:
                    idiomaSeleccionado = IdiomasEnum.FR;
                    break;
                case 5:
                    idiomaSeleccionado = IdiomasEnum.IT;
                    break;
                default:
                    System.out.println("Opção inválida. O espanhol será usado por padrão.");
                    idiomaSeleccionado = IdiomasEnum.ES;
            }

            List<Libro> librosPorIdioma = repositorio.findByIdiomas(idiomaSeleccionado);

            if (librosPorIdioma.isEmpty()) {
                System.out.println("\n *** Nenhum livro encontrado no idioma selecionado: " + idiomaSeleccionado.name() + " *** \n");
            } else {
                //idiomaSeleccionado.name()
                System.out.println("\n *** Livros encontrados em(" + idiomaSeleccionado.getExpresionEnEspanol() + ") *** \n");
                librosPorIdioma.forEach(libro -> System.out.printf("""
                                Titulo: %s
                                Autor: %s \n
                                """,
                        libro.getTitulo(), libro.getAutor().getNombreAutor()));
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, insira um número entre 1 à 5.");
        }
    }

    private void listaDeAutores(){
        autor = repositorio.findAllUniqueAutores();

        if (autor.isEmpty()) {
            System.out.println("\n *** Não foram encontrados autores no banco de dados *** \n");
        } else {
            autor.forEach(autor -> {
                String titulos = autor.getLibros().stream()
                            .map(Libro::getTitulo)
                            .collect(Collectors.joining(", "));
                        System.out.printf(
                                """                                      
                                Autor: %s ( %s - %s )
                                Titulos: %s \n
                                """, autor.getNombreAutor(),
                                autor.getAnioNacimiento(), autor.getAnioFallecimiento(), titulos);
            });
        }
//        List<Libro> libros = repositorio.findAll();
//
//        // Extraer autores únicos de los libros
//        autor = libros.stream()
//                .map(Libro::getAutor)
//                .filter(Objects::nonNull)
//                .distinct()
//                .collect(Collectors.toList());
//
//        if (autor.isEmpty()) {
//            System.out.println("No se encontraron autores en la base de datos.");
//        } else {
//            System.out.println("Lista de autores:");
//            autor.forEach(autor -> System.out.println("- " + autor.getNombreAutor()));
//        }
    }
    private void autoresPorAnio(){
        System.out.print("\n *** Digite o ano para pesquisar autores vivos:  *** \n");
        int anio = Integer.parseInt(teclado.nextLine());
        autor = repositorio.findByYearAutores(anio);

        if(autor.isEmpty()){
            System.out.println("\n *** Nenhum autor vivo foi encontrado no ano " + anio + " *** \n");
        }else{
            System.out.println("\n *** Autores vivos no ano" + anio + " *** \n");
            autor.forEach(autor -> {
                String estadoVital = autor.getAnioFallecimiento() == null ?
                        "Ainda vivo" :
                        "Falecido em " + autor.getAnioFallecimiento();
                System.out.println("- " + autor.getNombreAutor() +
                        " (Nascido em: " + autor.getAnioNacimiento() +
                        ", " + estadoVital + ") \n");
            });
        }
    }



}
