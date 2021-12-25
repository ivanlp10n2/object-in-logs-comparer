package infrastructure

import cats._
import cats.effect.IO
import cats.syntax.applicative._
import fs2.io.file.Path
import io.circe.{Decoder, Encoder}
import org.typelevel.log4cats.Logger

object config {
  def load[F[_]: Applicative]: F[AppConfig] =
    AppConfig(
      Path("./data/source/file1"),
      Path("./data/source/file2"),
      Path("./data/destination/file3")
    ).pure[F]

  case class AppConfig(file1: Path, file2: Path, file3: Path)

  type Marshaller[A] = Decoder[A] with Encoder[A]

  object TestLogger{
    implicit def terminalLogger: Logger[IO] = new Logger[IO] {
      override def error(t: Throwable)(message: => String): IO[Unit] = ???

      override def warn(t: Throwable)(message: => String): IO[Unit] = ???

      override def info(t: Throwable)(message: => String): IO[Unit] = ???

      override def debug(t: Throwable)(message: => String): IO[Unit] = ???

      override def trace(t: Throwable)(message: => String): IO[Unit] = ???

      override def error(message: => String): IO[Unit] = IO.println(s"Error: $message")

      override def warn(message: => String): IO[Unit] = IO.println(s"Warn: $message")

      override def info(message: => String): IO[Unit] = IO.println(s"Info: $message")

      override def debug(message: => String): IO[Unit] = IO.println(s"Debug: $message")

      override def trace(message: => String): IO[Unit] = ???
    }
  }
}
