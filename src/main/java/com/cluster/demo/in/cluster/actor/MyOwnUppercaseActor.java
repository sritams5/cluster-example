package com.cluster.demo.in.cluster.actor;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.Member;

public class MyOwnUppercaseActor extends AbstractActor {

    private final Cluster cluster = Cluster.get(getContext().getSystem());
    public static Props props() {
        // You need to specify the actual type of the returned actor
        // since Java 8 lambdas have some runtime type information erased
        return Props.create(MyOwnUppercaseActor.class, () -> new MyOwnUppercaseActor());
    }



    @Override
    public void preStart() throws Exception {
        cluster.subscribe(self(), ClusterEvent.MemberUp.class);
    }

    @Override
    public void postStop() throws Exception {
        cluster.unsubscribe(self());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message -> {
                    System.out.println("MyOwn Uppercasing given message :: " + message.toUpperCase());
                })
                .match(ClusterEvent.MemberUp.class, mUp -> {
                    register(mUp.member());
                })
                .build();
    }


    void register(Member member) {
        if (member.hasRole("tester"))
            getContext().actorSelection(member.address() + "/user/testerActor").tell(
                    "BACKEND_REGISTRATION", self());
    }

}
