package com.cluster.demo.in.cluster;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.OnSuccess;
import akka.util.Timeout;
import com.cluster.demo.in.cluster.actor.InTesterActor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static akka.pattern.Patterns.ask;

public class InTesterMain {

    public static void main(String[] args) {
        final Config config = ConfigFactory.load("application-in-cluster");
        final ActorSystem system = ActorSystem.create("ClusterSystem", config);
        ActorRef tester = system.actorOf(Props.create(InTesterActor.class), "testerActor");
        ActorRef myownUpperActor = system.actorOf(Props.create(InTesterActor.class), "myownUpperActor");

        final FiniteDuration interval = Duration.create(2, TimeUnit.SECONDS);
        final Timeout timeout = new Timeout(Duration.create(5, TimeUnit.SECONDS));
        final ExecutionContext ec = system.dispatcher();
        final AtomicInteger counter = new AtomicInteger();
        system.scheduler().schedule(interval, interval, new Runnable() {
            public void run() {
                ask(tester,
                        "hello-" + counter.incrementAndGet(),
                        timeout).onSuccess(new OnSuccess<Object>() {
                    public void onSuccess(Object result) {
                        System.out.println(result);
                    }
                }, ec);
            }

        }, ec);

    }
}
