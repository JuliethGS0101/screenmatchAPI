package com.aluracursos.screenmatch.principal;

import java.util.Arrays;
import java.util.List;

public class EjemploStreams {

    public void muestraEjemplo(){
        List<String> nombres = Arrays.asList("Carla", "Facu", "Joss", "Juan David", "Julieth");

        nombres.stream()
                .sorted()
                .limit(5)
                .filter(n -> n.startsWith("J"))
                .map(n -> n.toUpperCase())
                .forEach(System.out::println);
    }
}
