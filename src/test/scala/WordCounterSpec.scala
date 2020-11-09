import WordCounterSpec.SimpleTestActor
import akka.actor
import akka.actor.{Actor, ActorLogging, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import com.mccakir.wordCounter.WordCounterMaster
import com.mccakir.wordCounter.WordCounterMaster.Initialize
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

class WordCounterSpec extends TestKit(ActorSystem("BasicSpec"))
  with ImplicitSender // used for send reply scenarios in actors
  with AnyWordSpecLike // natural language style test description
  with BeforeAndAfterAll // hooks will be called before and after test cases
{
  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A simple word counter actor" should {
    "count words in given list of string" in {
      val simpleCounterActor = system.actorOf(actor.Props[SimpleTestActor])
      simpleCounterActor ! "run"
      //TODO find a way without sleep
      Thread.sleep(500)
      simpleCounterActor ! "getWordCount"
      val totalWordCount = expectMsgType[Int]
      assert(totalWordCount == 39)

    }
  }
}

object WordCounterSpec {

  class SimpleTestActor extends Actor with ActorLogging {
    override def receive: Receive = countReceive(0)

    def countReceive(wordCount: Int): Receive = {
      case "run" =>
        val masterActor = context.actorOf(actor.Props[WordCounterMaster], "wordCounterMasterActor")
        masterActor ! Initialize(2)
        val texts = List("The greatest glory in living lies not in never falling, but in rising every time we fall.",
        "It is during our darkest moments that we must focus to see the light.",
        "Whoever is happy will make others happy too.")
        texts.foreach(text => masterActor ! text)
      case count: Int =>
        context.become(countReceive(wordCount + count))
      case "getWordCount" => sender() ! wordCount
    }
  }

}
