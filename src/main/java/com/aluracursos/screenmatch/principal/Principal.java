package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.DatosEpisodio;
import com.aluracursos.screenmatch.model.DatosSerie;
import com.aluracursos.screenmatch.model.DatosTemporadas;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private  final String API_KEY = "&apikey=716b5b12";
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();

    public void muestraElMenu(){
        System.out.println("Escribe el nombre de la serie que deseas buscar:");
        //Busca los datos generales de las series
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);
        System.out.println(datos);

        //Busca todos los datos de las temporadas
        List<DatosTemporadas> temporadas = new ArrayList<>();
        for (int i = 1; i <= datos.totalDeTemporadas(); i++) {
            json= consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + "&Season=" + i + API_KEY);
            var datosTemporadas = conversor.obtenerDatos(json, DatosTemporadas.class);
            temporadas.add(datosTemporadas);
        }

        //temporadas.forEach(System.out::println);

        //Mostrar solo el titulo de los episodios para las temporadas
        /*for (int i = 0; i < datos.totalDeTemporadas(); i++) {
            List<DatosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
            for (int j = 0; j < episodiosTemporada.size(); j++) {
                System.out.println(episodiosTemporada.get(j).titulo());
            }
        }*/

        // Mejoria usando la funcion lambda
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        //Convertir todas las informaciones a una lista de tipo DatosEpisodio

        List<DatosEpisodio> datosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());


        //Top 5 episodios
        System.out.println("Top 5 episodios");
        datosEpisodios.stream()
                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("Primer filtro (N/A)" + e))
                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
                .peek(e -> System.out.println("Segunda ordenaciòn (M>m)" + e))
                .map(e -> e.titulo().toUpperCase())
                .peek(e -> System.out.println("Tercer filtro Mayùscula (m>M)" + e))
                .limit(5)
                .forEach(System.out::println);

        //Convirtiendo los datos a una lista del tipo episodios
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(),d)))
                .collect(Collectors.toList());

        //episodios.forEach(System.out::println);


        // Busqueda de episodios por x año
        System.out.println("Indica el año a partir del cual deseas ver los episodios:");
        var fecha = teclado.nextInt();
        teclado.nextLine();


        LocalDate fechaBusqueda = LocalDate.of(fecha, 1,1);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        /*episodios.stream()
                .filter(e -> e.getFechaDeLanxzamiento() != null && e.getFechaDeLanxzamiento().isAfter(fechaBusqueda))
                .forEach(e -> System.out.println(
                   "Temporada: " + e.getTemporada() +
                           "Episodio: " + e.getTitulo() +
                           "Fecha de lanzamiento: " + e.getFechaDeLanxzamiento().format(dtf)
                ));*/
    }
}
