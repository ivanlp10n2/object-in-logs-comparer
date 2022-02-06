package domain

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}

case class DummyModel(name: String, age: Int)
object DummyModel{
  implicit val encoder: Encoder[DummyModel] = deriveEncoder
  implicit val decoder: Decoder[DummyModel] = deriveDecoder
}
