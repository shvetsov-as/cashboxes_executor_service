/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kassy;

import java.util.logging.Level;
import java.util.logging.Logger;
import static kassy.Kassy.queue;

/**
 *
 * @author User
 */
public class Kassa implements Runnable {

    public volatile boolean stop = false;

    @Override
    public void run() {

        while (!stop) {
            try {
                Thread.sleep(Kassy.getRandomServiceTime());
                //queue.take();
                System.out.println("Vziala klienta" + queue.take());

            } catch (InterruptedException ex) {
                Logger.getLogger(Kassa.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
}
