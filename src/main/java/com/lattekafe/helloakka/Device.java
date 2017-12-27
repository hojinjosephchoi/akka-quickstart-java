package com.lattekafe.helloakka;

import akka.actor.AbstractActor;
import akka.actor.Props;

import java.util.Optional;

public class Device extends AbstractActor{

    final String groupId;
    final String deviceId;

    public Device(String groupId, String deviceId) {
        this.groupId = groupId;
        this.deviceId = deviceId;
    }

    /**
     * Actor 생성자 정의
     * @return
     */
    public static Props props(String groupId, String deviceId){
        return Props.create(Device.class, groupId, deviceId);
    }

    /**
     * 메시지 타입 - ReadTemperature
     */
    public static final class ReadTemperature{
        final long requestId;

        public ReadTemperature(long requestId){
            this.requestId = requestId;
        }
    }

    /**
     * 메시지 타입 - ResponseTemperature
     */
    public static final class RespondTemperature{
        final long requestId;
        final Optional<Double> value;

        public RespondTemperature(long requestId, Optional<Double> value) {
            this.requestId = requestId;
            this.value = value;
        }
    }

    // Device가 측정하는 온도가 여기 기록된다고 가정
    Optional<Double> lastTemperatureReading = Optional.empty();


    /**
     * 메시지 프로토콜 정의 - ReadTemerature 메시지를 수신할 경우 RespondTemperature로 응답한다.
     * @return
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ReadTemperature.class, readTemperature -> {
                    getSender().tell(new RespondTemperature(readTemperature.requestId, lastTemperatureReading), getSelf());
                })
                //.match(MyMsg.class, this::onMsg)
                .build();
    }
}