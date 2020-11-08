package com.mccakir.wordCounter

import akka.actor.{Actor, ActorLogging}

class WordCounterWorker extends Actor with ActorLogging {

  import WordCounterMaster._

  override def receive: Receive = {
    case WordCountTask(taskId, text) =>
      log.info(s"${self.path} I have received a task $taskId with $text")
      sender() ! WordCountReply(taskId, text.split(" ").length)
  }
}
