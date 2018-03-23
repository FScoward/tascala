package controllers.task

import java.time.ZonedDateTime

import controllers.base.ZonedDateTimeDecoder
import io.circe.Decoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._

case class TaskUpdateRequest(
    title: String,
    description: Option[String],
    deadline: Option[ZonedDateTime],
    estimate: Option[Int]
)

object TaskUpdateRequest extends ZonedDateTimeDecoder {
  implicit val config: Configuration =
    Configuration.default

  implicit val snakyDecoder: Decoder[TaskUpdateRequest] = deriveDecoder
}
