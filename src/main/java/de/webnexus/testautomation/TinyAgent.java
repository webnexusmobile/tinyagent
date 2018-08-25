package de.webnexus.testautomation;

import java.io.File;
import java.util.logging.Logger;

/**
 * TinyAgent which get's things done and lays itself to sleep afterwards.
 * It will stop it's serious business whenever a file called "stahp" is found in it's working folder.
 *
 * @author webnexusmobile
 *
 */

public class TinyAgent
{
    private static final Logger logger = Logger.getLogger(TinyAgent.class.getName());
    private final Integer SLEEPY_TIME_IN_SECS = 10;

    public static void main( String[] args )
    {

        TinyAgent tinyAgent = new TinyAgent();
        tinyAgent.run();
    }

    private void run() {
        boolean interrupted = false;
        Thread controlThread = new Thread(new ControlRunner());
        controlThread.start();

        while (controlThread.isAlive() && !interrupted){
            try {
                workTask();
                logger.info("Going to sleep for " + SLEEPY_TIME_IN_SECS + " seconds.");
                Thread.sleep(SLEEPY_TIME_IN_SECS * 1000);
                logger.info("Just woke up.");
            } catch (InterruptedException e) {
                interrupted = true;
            }
        }
    }

    private void workTask(){
        logger.info("Doing some real serious stuff.");
    }

    private class ControlRunner implements Runnable {
        private static final String CONTROL_FILE = "stahp";
        private final Integer SLEEPY_TIME_IN_SECS = 5;
        private File controlFile;

        ControlRunner() {
            controlFile = new File(CONTROL_FILE);
        }

        @Override
        public void run() {
            logger.info("Control process started. Checking for '" + CONTROL_FILE + "' every " + SLEEPY_TIME_IN_SECS + " seconds.");
            while (!controlFile.exists()) {
                try {
                    Thread.sleep(SLEEPY_TIME_IN_SECS * 1000);
                } catch (InterruptedException e) {
                    logger.info("I've been interrupted. " + e);
                }
            }
            logger.info("Found '" + CONTROL_FILE + "' in local path. Exiting control process.");
        }
    }
}
