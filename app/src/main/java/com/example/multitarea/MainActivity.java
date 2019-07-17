package com.example.multitarea;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final int numCajeras = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void normal(View view){
        Cliente cliente1 = new Cliente("Cliente 1", new int[] { 2, 2, 1, 5, 2, 3 });
        Cliente cliente2 = new Cliente("Cliente 2", new int[] { 1, 3, 5, 1, 1 });

        Cajera cajera1 = new Cajera("Cajera 1");
        Cajera cajera2 = new Cajera("Cajera 2");

        // Tiempo inicial de referencia
        long initialTime = System.currentTimeMillis();

        cajera1.procesarCompra(cliente1, initialTime);
        cajera2.procesarCompra(cliente2, initialTime);
    }

    public void thread(View view) {

        Cliente cliente1 = new Cliente("Cliente 1", new int[] { 2, 2, 1, 5, 2, 3 });
        Cliente cliente2 = new Cliente("Cliente 2", new int[] { 1, 3, 5, 1, 1 });

        // Tiempo inicial de referencia
        long initialTime = System.currentTimeMillis();
        CajeraThread cajera1 = new CajeraThread("Cajera 1", cliente1, initialTime);
        CajeraThread cajera2 = new CajeraThread("Cajera 2", cliente2, initialTime);

        cajera1.start();
        cajera2.start();
    }

    public void executor(View view) {

        ArrayList<Cliente> clientes = new ArrayList<Cliente>();
        clientes.add(new Cliente("Cliente 1", new int[] { 2, 2, 1, 5, 2 })); // 12 Seg
        clientes.add(new Cliente("Cliente 2", new int[] { 1, 1, 5, 1, 1 })); //  9 Seg
        clientes.add(new Cliente("Cliente 3", new int[] { 5, 3, 1, 5, 2 })); // 16 Seg
        clientes.add(new Cliente("Cliente 4", new int[] { 2, 4, 3, 2, 5 })); // 16 Seg
        clientes.add(new Cliente("Cliente 5", new int[] { 1, 3, 2, 2, 3 })); // 11 Seg
        clientes.add(new Cliente("Cliente 6", new int[] { 4, 2, 1, 3, 1 })); // 11 Seg
        clientes.add(new Cliente("Cliente 7", new int[] { 3, 3, 2, 4, 7 })); // 19 Seg
        clientes.add(new Cliente("Cliente 8", new int[] { 6, 1, 3, 1, 3 })); // 14 Seg
        // Tiempo total en procesar todos los pedidos = 108 segundos

        long init = System.currentTimeMillis();  // Instante inicial del procesamiento

        ExecutorService executor = Executors.newFixedThreadPool(numCajeras);
        for (Cliente cliente: clientes) {
            Runnable cajera = new CajeraRunnable(cliente, init);
            executor.execute(cajera);
        }
        executor.shutdown();	// Cierro el Executor
        while (!executor.isTerminated()) {
            // Espero a que terminen de ejecutarse todos los procesos
            // para pasar a las siguientes instrucciones
        }

        long fin = System.currentTimeMillis();	// Instante final del procesamiento
        System.out.println("Tiempo total de procesamiento: "+(fin-init)/1000+" Segundos");
    }
}
