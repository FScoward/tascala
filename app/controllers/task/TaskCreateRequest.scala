package controllers.task

import java.time.ZonedDateTime

import controllers.base.ZonedDateTimeDecoder
import domain.model.{ID, Id64}
import domain.model.task.{Icebox, Task}
import domain.model.user.User
import io.circe.Decoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._

case class TaskCreateRequest(
    title: String,
    description: Option[String],
    deadline: Option[ZonedDateTime],
    estimate: Option[Int]
) {
  def convert: Task =
    Icebox(ID[Task](Id64.nextAscId()),
         ID[User](0L),
         title,
         description,
         deadline,
         estimate)
}

object TaskCreateRequest extends ZonedDateTimeDecoder {
  implicit val config: Configuration =
    Configuration.default

  implicit val snakyDecoder: Decoder[TaskCreateRequest] = deriveDecoder
}
