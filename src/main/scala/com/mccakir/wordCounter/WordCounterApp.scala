package com.mccakir.wordCounter

import akka.actor.{ActorSystem, Props}
import com.mccakir.wordCounter.SimpleTestActor.{Print, Run}

object WordCounterApp {

  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem("WordCounterActorSystem")
    val simpleTestActor = actorSystem.actorOf(Props[SimpleTestActor], "simpleTestActor")
    simpleTestActor ! Run
    Thread.sleep(500)
    simpleTestActor ! Print
  }

}
