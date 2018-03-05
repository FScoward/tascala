package domain.model.auth.google

import io.circe.{Decoder, Encoder}
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._

case class JsonIdentity(
    kind: String,
    gender: String,
    sub: String,
    name: String,
    givenName: String,
    familyName: String,
    profile: String,
    picture: String,
    email: String,
    emailVerified: Boolean,
    locale: String
)

object JsonIdentity {

  implicit val config: Configuration =
    Configuration.default.withSnakeCaseMemberNames

  implicit val emailVerifiedDecoder = Decoder.decodeString.map {
    case "true"  => true
    case "false" => false
    case _       => throw new Exception("") // TODO
  }
  implicit val snakyEncoder: Encoder[JsonIdentity] = deriveEncoder
  implicit val snakyDecoder: Decoder[JsonIdentity] = deriveDecoder
}
