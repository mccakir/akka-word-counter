package com.mccakir.wordCounter

import akka.actor
import akka.actor.{Actor, ActorLogging}
import com.mccakir.wordCounter.SimpleTestActor.{Print, Run}
import com.mccakir.wordCounter.WordCounterMaster.Initialize

object SimpleTestActor {

  case object Run

  case object Print

}

class SimpleTestActor extends Actor with ActorLogging {

  override def receive: Receive = countReceive(0)

  def countReceive(wordCount: Int): Receive = {
    case Run =>
      val masterActor = context.actorOf(actor.Props[WordCounterMaster], "wordCounterMasterActor")
      masterActor ! Initialize(5)
      val texts = List("Among other public buildings in a certain town, which for many reasons it will be prudent to" +
        " refrain from mentioning, and to which I will assign no fictitious name, there is one anciently common to " +
        "most towns, great or small: to wit, a workhouse; and in this workhouse was born; on a day and date which I" +
        " need not trouble myself to repeat, inasmuch as it can be of no possible consequence to the reader, in this" +
        " stage of the business at all events; the item of mortality whose name is prefixed to the head of this " +
        "chapter.", "For a long time after it was ushered into this world of sorrow and trouble, by the parish " +
        "surgeon, it remained a matter of considerable doubt whether the child would survive to bear any name at all; " +
        "in which case it is somewhat more than probable that these memoirs would never have appeared; or, if they had," +
        " that being comprised within a couple of pages, they would have possessed the inestimable merit of being the" +
        " most concise and faithful specimen of biography, extant in the literature of any age or country.", "Although " +
        "I am not disposed to maintain that the being born in a workhouse, is in itself the most fortunate and enviable " +
        "circumstance that can possibly befall a human being, I do mean to say that in this particular instance, it was " +
        "the best thing for Oliver Twist that could by possibility have occurred.", "The fact is, that there was " +
        "considerable difficulty in inducing Oliver to take upon himself the office of respiration,--a troublesome " +
        "practice, but one which custom has rendered necessary to our easy existence; and for some time he lay gasping " +
        "on a little flock mattress, rather unequally poised between this world and the next: the balance being decidedly " +
        "in favour of the latter.", "Now, if, during this brief period, Oliver had been surrounded by careful grandmothers, " +
        "anxious aunts, experienced nurses, and doctors of profound wisdom, he would most inevitably and indubitably" +
        " have been killed in no time.", "There being nobody by, however, but a pauper old woman, who was rendered " +
        "rather misty by an unwonted allowance of beer; and a parish surgeon who did such matters by contract; " +
        "Oliver and Nature fought out the point between them.", " The result was, that, after a few struggles, " +
        "Oliver breathed, sneezed, and proceeded to advertise to the inmates of the workhouse the fact of a new " +
        "burden having been imposed  upon the parish, by setting up as loud a cry as could reasonably have been " +
        "expected from a male infant who had not been possessed of that very useful appendage, a voice, for a much " +
        "longer space of time than three minutes and a quarter.")
      texts.foreach(text => masterActor ! text)
    case count: Int =>
      println(s"new count is $count")
      context.become(countReceive(wordCount + count))
    case Print => log.info(s"Word count is: $wordCount")
  }
}
