package domain

import infrastructure.config.Marshaller
import io.circe.Decoder.Result
import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder, HCursor, Json}

import java.time.LocalDateTime

case class SiteCreationMessage(body: OnboardingSiteRequest, createdAt: LocalDateTime)
object SiteCreationMessage {
  implicit val encoder: Encoder[SiteCreationMessage] = deriveEncoder
  implicit val decoder: Decoder[SiteCreationMessage] = deriveDecoder
}

case class OnboardingSiteRequest(
                                  cuit: String,
                                  legalName: String,
                                  brands: Seq[Brand],
                                  address: String,
                                  zipCode: String,
                                  city: String,
                                  state: Option[String],
                                  email: String,
                                  isFDDisable: Option[Boolean] = None,
                                  billingId: String
                                )
object OnboardingSiteRequest{
  implicit val encoder: Encoder[OnboardingSiteRequest] = deriveEncoder
  implicit val decoder: Decoder[OnboardingSiteRequest] = deriveDecoder
}

case class Brand(brand: String, establishments: Seq[Establishment])
object Brand{
  implicit val encoder: Encoder[Brand] = deriveEncoder
  implicit val decoder: Decoder[Brand] = deriveDecoder
}

case class Establishment(id: String, terminals: Seq[Terminal])
object Establishment{
  implicit val encoder: Encoder[Establishment] = deriveEncoder
  implicit val decoder: Decoder[Establishment] = deriveDecoder
}

case class Terminal (terminalNumber: String)
object Terminal{
  implicit val encoder: Encoder[Terminal] = deriveEncoder
  implicit val decoder: Decoder[Terminal] = deriveDecoder
}