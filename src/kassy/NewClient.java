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
public class NewClient extends Thread{
    
    public static boolean stopThreadClient = true;
    public static int num = 1;
    @Override
    public void run(){
        
        while(stopThreadClient){
            try {
                Thread.sleep(Kassy.getRandomClientDelay()*1000);
            } catch (InterruptedException ex) {
                System.out.println("Prervana generaciya");
            }
            Client client = new Client(num++);
            queue.add(client);
            //System.out.println(queue.poll());
                    
        Kassy.queueArea.append(client.toString() + ", ");
        }
    }
}
