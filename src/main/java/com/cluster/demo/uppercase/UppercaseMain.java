package com.cluster.demo.uppercase;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.client.ClusterClientReceptionist;
import com.cluster.demo.uppercase.actor.UppercaseActor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class UppercaseMain {
    public static void main(String[] args) {
        final Config config = ConfigFactory.load("application-uppercase");
        final ActorSystem system = ActorSystem.create("ClusterSystem", config);
        ActorRef uppercaseActor = system.actorOf(Props.create(UppercaseActor.class), "uppercaseActor");

        //Below line registers actor as service
        ClusterClientReceptionist.get(system).registerService(uppercaseActor);
        uppercaseActor.tell("mytestmessage",null);
    }
}
