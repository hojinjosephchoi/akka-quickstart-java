package com.lattekafe.parallel;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import org.junit.Test;

public class ParallelTest {
    @Test
    public void testParallel(){
        ActorSystem system = ActorSystem.create("parallelTest-system");
        TestKit probe = new TestKit(system);
        ActorRef master = system.actorOf(Master.props());
        for(int inx = 0; inx < 5; inx++){
            master.tell(new Work(Integer.toString(inx)), probe.getRef());
        }
    }
}
