package controllers.task

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import domain.model.ID
import io.circe.Encoder
import io.circe.generic.semiauto._

case class TaskResponse(
    id: Long,
    userId: Long,
    title: String,
    description: Option[String],
    deadline: Option[ZonedDateTime],
    estimate: Option[Int],
    status: String
)

object TaskResponse {
  implicit val encodeZonedDateTime: Encoder[ZonedDateTime] =
    Encoder.encodeString.contramap[ZonedDateTime](
      _.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
  implicit def idEncoder[T]: Encoder[ID[T]] =
    Encoder.encodeString.contramap[ID[T]](_.value.toString)
  implicit val taskEncoder: Encoder[TaskResponse] = deriveEncoder[TaskResponse]
}
