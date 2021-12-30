package sandbox

import cats.effect._
import cats.effect.kernel.Concurrent
import cats.effect.std.Console
import cats.syntax.all._
import eu.timepit.refined.numeric.Positive
import fs2._


object Sandbox extends IOApp {
  def a[F[_]]: List[Unit] =
    Stream(1, 1, 2, 3, 4)
      .through(dedupeDuplicates(_ == _))
      .toList
      .map(println)


  def dedupeDuplicates[F[_], I](f: (I, I) => Boolean): Pipe[F, I, I] = si =>
    si.zipWithNext.map {
      case (curr, Some(next: I)) if f(curr, next) => None
      case (curr, Some(_)) => Some(curr)
      case (curr, None) => Some(curr)
    }.unNone


  import scala.concurrent.duration._

  def parallelStreams[F[_] : Console : Concurrent : Temporal]: Stream[F, Any] = {
    Stream.eval(Deferred[F, Unit]).flatMap(switch => {
      val program1 = Stream("Mikami,", "Mikami", "la casafantasmas")
        .repeat
        .evalMap(Console[F].println)
        .metered(1.seconds)
        .interruptWhen(switch.get.attempt)

      val timer = Stream
        .eval(switch.complete())
        .delayBy(6.seconds)

      timer.concurrently(program1)
    })
  }
  //    Stream(1,2,3).evalTap()
  //    Stream(1,2,3).evalMap()
  //    Stream(1,2,3).through()
  //    Stream(1,2,3).printlns()
  //    Stream(1,2,3).filter()

  //    Stream.emits(0 to 10).flatMap(n =>
  //        Stream.emit(n).map(m => s"two numbers $m, $n"))
  //      .concurrently()
  //    }

  override def run(args: List[String]): IO[ExitCode] = {
    parallelStreams[IO]
      .compile
      .drain
      .as(ExitCode.Success)
  }
}

object Bribes extends App {
  final case class Person(sticker: Positive) extends AnyVal

  final case class Line(value: List[Person])

  object Env {
    val maxStepsForward = 2
  }

  /**
   * compare each index with current value
   * find diff between original position and current
   * if > 2 returns a Left(Too chaotic)
   * otherwise sum it
   *
   * @return
   */
  def completeLine: List[Int] => Either[String, Int] = {
    _.zipWithIndex
      .traverse {
        case (n, index) => {
          val stepsForward = n - (index + 1) // 1, 2, 4, 3 = 0, 0, 1, -1

          if (stepsForward > Env.maxStepsForward) {
            Left("Too chaotic")
          } else
            Right(stepsForward)
        }
      }
      .map(_.filter(_ > 0).sum)
  }

  def run(input: List[Int]): String = {
    new NonCats().completeLine(input).fold(identity, _.toString)
  }

  {
    val input = List(1, 2, 4, 3)
    println(run(input))
    assert(run(input) == "1")
  }

  {
    val input = List(4, 2, 4, 3)
    assert(run(input) == "Too chaotic")
  }

}

object ContinuationPassing extends App {
  def f[A, B](a: A, async: A => B): B =
    async(a)

  f(println("hello"), { _: Unit =>
    f(println("wn"), { _: Unit =>
      f("asd", { (s: String) =>
        println(s)
      })
    })
  })

}

object Flatten {
  sealed trait LightSwitch

  case object On extends LightSwitch

  case object Off extends LightSwitch

  type HttpStatus = Int

  trait Notifications {
    def notify(msg: String): IO[HttpStatus]
  }

  trait FSM {
    def toggle: IO[HttpStatus]
  }

  val fsmRef = new FSM {
    override def toggle: IO[HttpStatus] = {
      def runAction: IO[Unit] = ???

      def getStatus: IO[HttpStatus] = ???

      // Similar to CP
      runAction.map(_ => getStatus).flatten
    }
  }
}