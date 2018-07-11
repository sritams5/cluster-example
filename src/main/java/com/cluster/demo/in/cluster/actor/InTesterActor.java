package com.cluster.demo.in.cluster.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import com.cluster.demo.uppercase.actor.UppercaseActor;

import java.util.ArrayList;
import java.util.List;

public class InTesterActor extends AbstractActor {

    List<ActorRef> backends = new ArrayList<>();
    int jobCounter = 0;
    //ActorRef testActorRef =getContext().actorOf(UppercaseActor.props(), "ClusterSystem");
    ActorRef testActorRef =getContext().actorFor("/user/myownUpperActor");
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("BACKEND_REGISTRATION", message -> {
                    getContext().watch(sender());
                    backends.add(sender());
                })
                .match(String.class, message -> backends.isEmpty(), message -> {
                    testActorRef.tell("CallingFromIntest",self());
                })
                .match(String.class, message -> {
                    jobCounter++;
                    backends.get(jobCounter % backends.size()).forward(message, getContext());
                })
                .build();
    }
}
