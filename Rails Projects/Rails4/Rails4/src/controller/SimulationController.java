package controller;

import algorithm.Algorithm;
import java.util.Random;

public class SimulationController {
    private boolean running = false;
    private static Thread simThread;
    public static SimulationController instance;
    
    public SimulationController() {
        simThread = new Thread (new Simulation (this));
        simThread.start();
    }

    private class Simulation implements Runnable
    {
        private SimulationController parent;
        private Algorithm algorithm = Algorithm.getInstance();
        
        protected Simulation (SimulationController parent)
        {
            System.out.println ("De simulatie loopt");
            this.parent         = parent;
            this.parent.running = true;
        }

        public void run()
        {
            Random stationRand  = new Random(),
                   passRand     = new Random(),
                   timeRand     = new Random();

            while (this.parent.running)
            {
                // Calculate a station and passenger count
                int stationBId  = stationRand.nextInt (8),
                    stationEId  = stationRand.nextInt (8),
                    passCount   = passRand.nextInt (25),
                    time        = timeRand.nextInt (10);

                // Don't want the time <= 5
                while (time <= 5)
                {
                    time = timeRand.nextInt (30);
                }
                
                // Don't want 0 passengers
                while ((passCount <= 0))
                {
                    passCount = passRand.nextInt (25);
                }
                
                // Don't want stationBId == stationEId
                while (stationBId == stationEId)
                {
                    stationEId  = stationRand.nextInt (8);
                }

                // Sleep for some time
                try
                {
                    simThread.sleep(time * 1000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                // Send train
                System.out.println(stationBId + "    " + stationEId + "    " + passCount);
                this.algorithm.orderTrain (stationBId, stationEId, passCount);
                //break; // Only for testing purpose!
            }
        }
    }
    public static SimulationController getInstance()
    {
        if (SimulationController.instance == null)
        {
            // Do some synchronizing for the threads :)
            synchronized (SimulationController.class)
            {
                if (SimulationController.instance == null)
                {
                    SimulationController.instance = new SimulationController();
                }
            }
        }
        return SimulationController.instance;
    }
}
