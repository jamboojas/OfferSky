package com.example.abhiraj.offersky.geofencing;

/**
 * Created by Abhiraj on 08-07-2017.
 */

public interface AccelerometerStepListener {
    /**
     * Called when a step has been detected.  Given the time in nanoseconds at
     * which the step was detected.
     */
    public void step(long timeNs);
}
