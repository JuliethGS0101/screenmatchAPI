package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.service.ConsumoAPI;

import java.util.Scanner;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();

    public void muestraElMenu(){
        System.out.println("Por favor escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos("https://www.omdbapi.com/?t=Arrow&apikey=716b5b12");
    }
}
