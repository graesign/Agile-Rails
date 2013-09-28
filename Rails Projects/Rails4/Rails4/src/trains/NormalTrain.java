package trains;

public class NormalTrain extends AbstractTrain
{
    /**
     * Constructor
     * @param trainId - A unique key to indentify the train
     */
    public NormalTrain(String trainId)
    {
        this.trainId    = trainId;
    }
    /**
     * At some point the train need to start doing things. This method will
     * start the inner class and thread.
     */
    public void startTrain()
    {
        this.trainUsed  = true;
        this.inner      = new RunTrain (this);
        trainThread     = new Thread (this.inner);
        trainThread.start();
    }
    /**
     * This inner class handles the train while moving
     */
    private class RunTrain extends AbstractRunTrain
    {
        /**
         * Main constructor
         * @param instance, The parent class so we can use everything that Cabin uses
         */
        protected RunTrain(NormalTrain instance)
        {
            this.parent     = instance;
            //this.running    = true;
        }
        
        /**
         * Main method, this method is called when a new thread is created.
         */
        @Override
        public void run()
        {
          //  while (this.running)
           // {
                

           // }
        }
        
        @Override
        protected boolean freeTrack() {
            return this.parent.algoritme.saveDistance (this.parent.trainId);
        }

        @Override
        protected void move(int xPos, int yPos) {
            throw new UnsupportedOperationException ("Not supported yet.");
        }
    }

    @Override
    public void orderTrain(int orderId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
