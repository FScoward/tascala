package domain.model.task

import java.time.ZonedDateTime

import domain.model.ID
import domain.model.user.User
import io.circe.Encoder
import io.circe.generic.semiauto._

case class Task(
    id: ID[Task],
    userId: ID[User],
    title: String,
    description: Option[String],
    deadline: Option[ZonedDateTime],
    estimate: Option[Int]
)

object Task {
  implicit val encodeZonedDateTime: Encoder[ZonedDateTime] =
    Encoder.encodeString.contramap[ZonedDateTime](_.toString)
  implicit def idEncoder[T]: Encoder[ID[T]] =
    Encoder.encodeString.contramap[ID[T]](_.value.toString)
  implicit val taskEncoder: Encoder[Task] = deriveEncoder[Task]
}
