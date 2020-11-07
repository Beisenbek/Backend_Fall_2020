import java.util.concurrent.atomic.AtomicLong

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer.DrainingControl
import akka.kafka.{ConsumerSettings, ProducerSettings, Subscriptions}
import akka.kafka.scaladsl.{Consumer, Producer}
import akka.stream.ClosedShape
import akka.stream.scaladsl.GraphDSL.Implicits.{SourceArrow, port2flow}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Keep, RunnableGraph, Sink, Source}
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.{Deserializer, StringDeserializer, StringSerializer}

import org.apache.kafka.common.serialization.{Deserializer, Serializer, StringDeserializer, StringSerializer}

import scala.concurrent.Future
import scala.util.{Failure, Success}

object MainApp extends  App {

  implicit val system = ActorSystem("QuickStart")
  implicit val ec = system.dispatcher

  def f1() = {

    val source = Source(1 to 10)
    val sink = Sink.fold[Int, Int](0)(_ + _)

    // connect the Source to the Sink, obtaining a RunnableGraph
    val runnable: RunnableGraph[Future[Int]] = source.toMat(sink)(Keep.right)

    // materialize the flow and get the value of the FoldSink
    val sum: Future[Int] = runnable.run()


    sum.onComplete {
      case Success(value) => {
        println(value)
        system.terminate()
      }
      case Failure(exception) => {
        println(exception)
      }
    }
  }

  def f2() = {

    val source = Source(1 to 10)
    val sink = Sink.fold[Int, Int](0)(_ + _)

    // materialize the flow, getting the Sinks materialized value
    val sum: Future[Int] = source.runWith(sink)

    sum.onComplete {
      case Success(value) => {
        println(value)
        system.terminate()
      }
      case Failure(exception) => {
        println(exception)
      }
    }
  }

  def f3() = {
    // connect the Source to the Sink, obtaining a RunnableGraph
    val sink = Sink.fold[Int, Int](0)(_ + _)
    val runnable: RunnableGraph[Future[Int]] =
      Source(1 to 10).toMat(sink)(Keep.right)

    // get the materialized value of the FoldSink
    val sum1: Future[Int] = runnable.run()
    val sum2: Future[Int] = runnable.run()

    sum1.onComplete {
      case Success(value) => {
        println(value)
        system.terminate()
      }
      case Failure(exception) => {
        println(exception)
      }
    }

    sum2.onComplete {
      case Success(value) => {
        println(value)
        system.terminate()
      }
      case Failure(exception) => {
        println(exception)
      }
    }
  }

  def f4() = {
    Source(1 to 6).via(Flow[Int].map(_ * 2)).to(Sink.foreach(println(_))).run()
  }

  def f5() = {
    // Broadcast to a sink inline
    val otherSink: Sink[Int, NotUsed] = Flow[Int].alsoTo(Sink.foreach(println(_))).to(Sink.ignore)
    Source(1 to 6).to(otherSink).run()
  }

  def f6() = {
    val source: Source[Int, NotUsed] = Source(1 to 20)

    def evenFlow: Flow[Int, Int, NotUsed] =  Flow[Int].filter(_ % 2 == 0)
    def oddFlow: Flow[Int, Int, NotUsed] = Flow[Int].filter(_ % 2 == 1)

    val evenSink = Sink.foreach[Int](x => print(x + "e "))
    val oddSink = Sink.foreach[Int](x => print(x + "o "))

    val graph = GraphDSL.create() { implicit builder =>

      val bcast = builder.add(Broadcast[Int](2))

      source ~> bcast.in
      bcast.out(0) ~> evenFlow ~> evenSink
      bcast.out(1) ~> oddFlow ~> oddSink

      ClosedShape
    }

    RunnableGraph.fromGraph(graph).run
  }

  def f7() = {
    val config = system.settings.config.getConfig("akka.kafka.producer")
    val server = system.settings.config.getString("akka.kafka.producer.kafka-clients.server")
    val topic  = system.settings.config.getString("akka.kafka.producer.kafka-clients.topic")

    val producerSettings =
      ProducerSettings(config, new StringSerializer, new StringSerializer)
        .withBootstrapServers(server)

    val done: Future[Done] =
      Source(101 to 110)
        .map(_.toString)
        .map(value => new ProducerRecord[String, String](topic, value))
        .runWith(Producer.plainSink(producerSettings))


    done.onComplete {
      case Success(value) => {
        println(value)
        system.terminate()
      }
      case Failure(exception) => {
        println(exception)
      }
    }

  }

  def f8() = {
    class OffsetStore {
      private val offset = new AtomicLong
      def businessLogicAndStoreOffset(record: ConsumerRecord[String, String]): Future[Done] = {
        println(s"DB.save: ${record.value}")
        offset.set(record.offset)
        Future.successful(Done)
      }
      def loadOffset(): Future[Long] = {
        Future.successful(offset.get)
      }
    }
    val topic = system.settings.config.getString("akka.kafka.producer.kafka-clients.topic")
    val bootstrapServers = system.settings.config.getString("akka.kafka.producer.kafka-clients.server")

    val consumerSettings = ConsumerSettings(system, new StringDeserializer, new StringDeserializer)
      .withBootstrapServers(bootstrapServers)
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
      .withClientId("externalOffsetStorage")

    val db = new OffsetStore
    db.loadOffset().map { fromOffset =>
      Consumer
        .plainSource(
          consumerSettings,
          Subscriptions.assignmentWithOffset(
            new TopicPartition(topic, 0) -> fromOffset
          )
        )
        .mapAsync(1)(db.businessLogicAndStoreOffset)
        .toMat(Sink.seq)(DrainingControl.apply)
        .run()
    }
  }
  f8()
}
