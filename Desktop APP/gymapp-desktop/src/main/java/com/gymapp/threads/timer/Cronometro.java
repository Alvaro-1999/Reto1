package com.gymapp.threads.timer;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Cronometro implements Runnable {
    public enum Tipo { ASCENDENTE, DESCENDENTE }

    private int segundos;
    private final Tipo tipo;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Thread hilo;
    private final CronometroListener listener;

    public interface CronometroListener {
        void onTick(int segundos);
        void onFinish();
    }

    public Cronometro(int segundosIniciales, Tipo tipo, CronometroListener listener) {
        this.segundos = segundosIniciales;
        this.tipo = tipo;
        this.listener = listener;
    }

    public void start() {
        if (hilo == null || !hilo.isAlive()) {
            running.set(true);
            hilo = new Thread(this);
            hilo.start();
        } else {
            resume();
        }
    }

    public void pause() {
        running.set(false);
    }

    public void resume() {
        running.set(true);
        synchronized (hilo) {
            hilo.notify();
        }
    }

    public void stop() {
        running.set(false);
        if (hilo != null) {
            hilo.interrupt();
        }
    }

    public void reset(int nuevosSegundos) {
        stop();
        this.segundos = nuevosSegundos;
    }

    @Override
    public void run() {
        while (true) {
            try {
                synchronized (hilo) {
                    while (!running.get()) {
                        hilo.wait();
                    }
                }
                SwingUtilities.invokeLater(() -> listener.onTick(segundos));

                if (tipo == Tipo.ASCENDENTE) {
                    segundos++;
                } else {
                    segundos--;
                    if (segundos < 0) {
                        SwingUtilities.invokeLater(listener::onFinish);
                        break;
                    }
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public int getSegundos() {
        return segundos;
    }
}
