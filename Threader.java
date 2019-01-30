
import static java.lang.Thread.sleep;

public class Threader {

    public void startThreads() throws InterruptedException {
        /**
         * Creates an instance of thread one and then goes to sleep for 5 seconds
         * before creating thread two. Then kills the threads
         */
        ThreadOne t1 = new ThreadOne();
        Thread.sleep(5000);
        ThreadTwo t2 = new ThreadTwo();
        Thread.sleep(5000);
        t1.setAlive();
        sleep(5000);
        t2.setAlive();

    }
    class ThreadOne extends Thread{
        private boolean active = true;
        private boolean alive = true;

        public ThreadOne(){
            System.out.println("Creating and starting Thread 1");
            start();
        }

        /**
         * Pauses the thread
         */
        public void setActive(){
            if(active){
                active = false;
                System.out.println("Thread 1 on pause");
            } else {
                active = true;
                System.out.println("Thread 1 starting again");
            }
        }

        /**
         * Kills the thread
         */
        public void setAlive(){
            if(alive){
                alive = false;
                System.out.println("Killing thread 1");
            }
        }

        /**
         * Continous loop until 'alive' is set to false
         */
        public void run(){
            while(alive) {
                System.out.println("Thread 1");
                try { sleep(1000); } catch(InterruptedException ie) {}
            }
            try { sleep(25); } catch(InterruptedException ie) {}
        }
    }
    class ThreadTwo implements Runnable{
        Thread t2 = new Thread(this);
        private boolean active = true;
        private boolean alive = true;

        public ThreadTwo(){
            System.out.println("Creating and starting Thread 2");
            t2.start();
        }

        /**
         * Pauses the thread
         */
        public void setActive(){
            if(active){
                active = false;
                System.out.println("Thread 2 on pause");
            } else{
                active = true;
                System.out.println("Starting thread 2 again");
            }
        }

        /**
         * Kills the thread
         */
        public void setAlive(){
            if(alive){
                alive = false;
                System.out.println("Killing thread 2");
            }
        }

        /**
         * Continous loop until 'alive' is set to false
         */
        @Override
        public void run() {
            while(alive) {
                System.out.println("Thread 2");
                try { sleep(1000); } catch(InterruptedException ie) {}
            }
            try { sleep(25); } catch(InterruptedException ie) {}
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Threader threader = new Threader();
        threader.startThreads();
    }
}

