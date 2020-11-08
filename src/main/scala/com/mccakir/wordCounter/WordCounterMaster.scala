package com.mccakir.wordCounter

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

object WordCounterMaster {

  case class Initialize(numOfWorkers: Int)

  case class WordCountTask(taskId: Int, text: String)

  case class WordCountReply(taskId: Int, count: Int)

}

class WordCounterMaster extends Actor with ActorLogging {

  import WordCounterMaster._

  override def receive: Receive = {
    case Initialize(numOfWorkers) =>
      log.info(s"Initializing word counter master with $numOfWorkers workers")
      val workerRefs = for (i <- 1 to numOfWorkers) yield context.actorOf(Props[WordCounterWorker],
        s"wordCounterWorker_$i")
      context.become(withWorkers(workerRefs, 0, 0, Map()))
  }

  def withWorkers(workerRefs: Seq[ActorRef], currentWorkerIndex: Int, currentTaskId: Int, requestMap: Map[Int,
    ActorRef]): Receive = {
    case text: String =>
      log.debug(s"I have received: $text - I will send it to child $currentWorkerIndex")
      val originalSender = sender()
      val task = WordCountTask(currentTaskId, text)
      val workerRef = workerRefs(currentWorkerIndex)
      workerRef ! task

      // round robin task distribution
      val nextWorkerIndex = (currentWorkerIndex + 1) % workerRefs.length
      val newTaskId = currentTaskId + 1
      val newRequestMap = requestMap + (currentTaskId -> originalSender)
      context.become(withWorkers(workerRefs, nextWorkerIndex, newTaskId, newRequestMap))

    case WordCountReply(taskId, count) =>
      log.info(s"I have received a reply for taskId: $taskId with count: $count")
      val originalSender = requestMap(taskId)
      originalSender ! count
      context.become(withWorkers(workerRefs, currentWorkerIndex, currentTaskId, requestMap - taskId))

  }
}
