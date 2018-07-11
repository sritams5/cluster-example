package com.cluster.demo.out.cluster;

import akka.actor.ActorPath;
import akka.actor.ActorPaths;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.cluster.client.ClusterClient;
import akka.cluster.client.ClusterClientSettings;
import akka.cluster.routing.ClusterRouterGroup;
import akka.cluster.routing.ClusterRouterGroupSettings;
import akka.routing.RandomGroup;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class OutTesterMain {
    public static void main(String[] args) {
        final Config config = ConfigFactory.load("application-out-cluster");
        final ActorSystem system = ActorSystem.create("MySystem", config);

        String actorRoute = "/user/uppercaseActor";
        final ActorRef c = system.actorOf(ClusterClient.props(
                ClusterClientSettings.create(system).withInitialContacts(initialContacts())),
                "client");
        c.tell(new ClusterClient.Send(actorRoute, "hello", true), ActorRef.noSender());

    }


    static Set<ActorPath> initialContacts() {
        return new HashSet<ActorPath>(Arrays.asList(ActorPaths.fromString("akka.tcp://ClusterSystem@127.0.0.1:1111/system/receptionist")));
    }
}
