package edu.eci.arsw.math;

public class PiDigitsThread extends Thread {

    private final int start;
    private final int count;
    private byte[] digits;
    private int processed = 0;

    // Paso 1 - control de pausa compartido entre todos los hilos
    private static volatile boolean paused = false;
    private static final Object lock = new Object();

    public PiDigitsThread(int start, int count) {
        this.start = start;
        this.count = count;
    }

    public static void setPaused(boolean p) {
        paused = p;
        if (!p) {
            synchronized (lock) {
                lock.notifyAll();
            }
        }
    }

    public static Object getLock() {
        return lock;
    }

    public int getProcessed() {
        return processed;
    }

    @Override
    public void run() {
        // Paso 2 - procesar en bloques y verificar pausa entre cada bloque
        int chunkSize = 8;
        digits = new byte[count];
        int current = start;

        while (processed < count) {
            synchronized (lock) {
                while (paused) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }

            int remaining = count - processed;
            int toProcess = Math.min(chunkSize, remaining);
            byte[] chunk = PiDigits.getDigits(current, toProcess);
            System.arraycopy(chunk, 0, digits, processed, toProcess);
            processed += toProcess;
            current += toProcess;
        }
    }

    public byte[] getDigits() {
        return digits;
    }

}

