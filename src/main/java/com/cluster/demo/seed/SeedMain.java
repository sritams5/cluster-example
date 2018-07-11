package com.cluster.demo.seed;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import com.cluster.demo.seed.actor.SimpleClusterListener;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class SeedMain {

    public static void main(String[] args) {
        final Config config = ConfigFactory.load("application-seed");
        final ActorSystem system = ActorSystem.create("ClusterSystem", config);
        ActorRef simpleClusterListener = system.actorOf(Props.create(SimpleClusterListener.class), "simpleClusterListener");
        Cluster.get(system).subscribe(simpleClusterListener, ClusterEvent.ClusterDomainEvent.class);
    }
}
