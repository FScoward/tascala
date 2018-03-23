package appservice.task

import java.time.ZonedDateTime

import controllers.base.ZonedDateTimeDecoder
import io.circe.Decoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._

case class TaskDoneRequest(requiredTime: Int, doneDateTime: ZonedDateTime)

object TaskDoneRequest extends ZonedDateTimeDecoder {
  implicit val config: Configuration =
    Configuration.default

  implicit val snakyDecoder: Decoder[TaskDoneRequest] = deriveDecoder
}
