package com.lattekafe.parallel;

import akka.actor.AbstractActor;

public class Worker extends AbstractActor{

    private void onWorkArrive(Work work){
        for(int inx = 0; inx < 3; inx++){
            try{
                Thread.sleep(500);
            }catch (Exception e){
                System.out.println("exception" + e);
            }

            System.out.println(work.payload);

        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                // Java 8 Method Reference(this::add)
                .match(Work.class, this::onWorkArrive)
                .build();
    }
}
