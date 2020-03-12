package com.finger.agent.scrap;

import com.finger.agent.scrap.tools.ScrapTools;

public class KIDGenerator {

    private static KIDGenerator generator;

    private long currTimeMillis;
    private long currSeq;

    private KIDGenerator(){
        currTimeMillis = System.currentTimeMillis();
        currSeq = 0;
    }

    public synchronized static KIDGenerator getInstance(){
        if( generator == null){
            generator = new KIDGenerator();
        }
        return generator;
    }

    public String makeKey() {
        synchronized(generator){
            long tempTimeMillis = System.currentTimeMillis();
            if( currTimeMillis == tempTimeMillis )
                currSeq++;
            else
            {
                currTimeMillis = tempTimeMillis;
                currSeq = 1;
            }
        }
        String random = Long.toString(currSeq)
                        + ScrapTools.writeZero( (int) Math.round(Math.random() * 1000), 3);
        return Long.toString(currTimeMillis).substring(9,13) + random.substring(0,4);
    }

    public static void main(String args[]) {
        for(int i=0; i < 1000; i++) {
            KIDGenerator key = KIDGenerator.getInstance();
            System.out.println("key="+key.makeKey());
        }
    }

}