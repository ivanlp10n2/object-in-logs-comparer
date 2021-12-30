import cats.effect._
import cats.syntax.all._
import domain.SiteCreationMessage
import fs2.io.file.{Files, Path}
import infrastructure._
import io.circe.fs2._
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder}
import fs2._
import org.typelevel.log4cats.Logger

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    import infrastructure.config.TestLogger._

    program[IO, SiteCreationMessage]
      .as(ExitCode.Success)
  }

  def program[F[_]: Logger: Files: Sync, A: Decoder: Encoder]: F[Unit] =
    config.load[F].flatMap(config => {
      val partAB = consumeFile.apply(config.file1)
      val partB = consumeFile.apply(config.file2)

      def notIn[B](b: List[B]): B => Boolean = elem => !b.contains(elem)

      val partA: F[List[A]] = (partAB, partB).mapN {
          case ((_, ab), (_, b)) => ab.filter(notIn(b))
      }

      writeFile(config.file3).apply(partA)
    })


  def consumeFile[F[_]: Sync: Files: Logger, A: Decoder]: Path => F[(Long, List[A])] = {
    def consumePath: Path => Stream[F, A] = path =>
       Files[F]
        .readAll(path)
        .through(text.utf8.decode)
        .through(text.lines)
        .through(stringStreamParser)
        .through(decoder[F, A])

    def countRecords: Stream[F,A] => F[(Long, List[A])] = _
      .compile
      .toList
      .map(l => (l.size, l))

    def logCount: F[(Long, List[A])] => F[(Long, List[A])] = _.flatTap { case (count, _) =>
      Logger[F].info(s"Consumed $count records")
    }

    consumePath andThen countRecords andThen logCount
  }


  def writeFile[F[_] : Sync : Files : Logger, A: Encoder](target: Path): F[List[A]] => F[Unit] = {
    def logCount: F[List[A]] => F[List[A]] ={
      def count:List[A] => Int = _.size

     _.flatTap(l => Logger[F].info(s"About to write ${count(l)} records"))
    }

    def writePath: F[List[A]] => Stream[F, INothing] =
      Stream.evalSeq(_)
        .through(_.map(_.asJson.noSpaces))
        .through(text.utf8.encode)
        .through(Files[F].writeAll(target))

    def compile[A]: Stream[F, A] => F[Unit] = _.compile.drain

    logCount andThen writePath andThen compile
  }
}