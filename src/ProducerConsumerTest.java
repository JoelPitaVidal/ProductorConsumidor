public class ProducerConsumerTest {
    public static void main(String[] args) {
        CubbyHole c = new CubbyHole();
        Producer p1 = new Producer(c, 1);
        Consumer c1 = new Consumer(c, 1);
        p1.start();
        c1.start();
    }
}

class CubbyHole {
    private int contents;
    private boolean available = false;

    // Metodo sincronizado para obtener un valor del CubbyHole
    public synchronized int get() {
        while (available == false) {
            try {
                wait(); // Espera hasta que haya un valor disponible
            } catch (InterruptedException e) {}
        }
        available = false;
        notifyAll(); // Notifica a todos los hilos en espera
        return contents; // Devuelve el contenido del CubbyHole
    }

    // Metodo sincronizado para colocar un valor en el CubbyHole
    public synchronized void put(int value) {
        while (available == true) {
            try {
                wait(); // Espera hasta que el CubbyHole esté vacío
            } catch (InterruptedException e) {}
        }
        contents = value;
        available = true;
        notifyAll(); // Notifica a todos los hilos en espera
    }
}

class Consumer extends Thread {
    private CubbyHole cubbyhole;
    private int number;

    public Consumer(CubbyHole c, int number) {
        cubbyhole = c;
        this.number = number;
    }

    public void run() {
        int value = 0;
        for (int i = 0; i < 10; i++) {
            value = cubbyhole.get(); // Obtener un valor del CubbyHole
            System.out.println("Consumidor #" + this.number + " obtuvo: " + value);
        }
    }
}

class Producer extends Thread {
    private CubbyHole cubbyhole;
    private int number;

    public Producer(CubbyHole c, int number) {
        cubbyhole = c;
        this.number = number;
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            cubbyhole.put(i); // Colocar un valor en el CubbyHole
            System.out.println("Productor #" + this.number + " colocó: " + i);
            try {
                sleep((int)(Math.random() * 100)); // Pausa aleatoria
            } catch (InterruptedException e) {}
        }
    }
}
