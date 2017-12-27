package com.lattekafe.parallel;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;

import java.util.ArrayList;
import java.util.List;

public class Master extends AbstractActor {

    public static Props props() {
        return Props.create(Master.class);
    }


    // 라우터에 5개의 워커 등록, 라운드 로빈 방식으로 메시지 분배
    Router router;
    // 초기화블록 (생성자 호출 전 초기화블록 호출됨, http://kamang-it.tistory.com/122)
   {
        List<Routee> routees = new ArrayList<>();
        for(int inx = 0; inx < 5; inx++){
            ActorRef workerActor = getContext().actorOf(Props.create(Worker.class));
            // watch -> 해당 액터의 작업이 끝나면 Terminated 메시지를 받는다.
            getContext().watch(workerActor);
            routees.add(new ActorRefRoutee(workerActor));
        }

        router = new Router(new RoundRobinRoutingLogic(), routees);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                // Work 메시지를 받으면 라우팅 시작
                .match(Work.class, work -> {
                    router.route(work, getSender());
                })
                // 특정 Worker의 작업이 끝나서 Terminated 메시지를 받은 경우 새로운 Worker를 라우터에 등록
                .match(Terminated.class, terminated -> {
                    router = router.removeRoutee(terminated.actor());
                    ActorRef workerActor = getContext().actorOf(Props.create(Worker.class));
                    getContext().watch(workerActor);
                    router = router.addRoutee(new ActorRefRoutee(workerActor));
                })
                .build();
    }
}
