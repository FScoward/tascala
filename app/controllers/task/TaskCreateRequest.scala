package controllers.task

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import domain.model.ID
import domain.model.task.Task
import domain.model.user.User
import io.circe.Decoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._

case class TaskCreateRequest(
    title: String,
    description: Option[String],
    deadline: ZonedDateTime,
    estimate: Int
) {
  def convert: Task = Task(ID[Task](0L), ID[User](0L), title, description, deadline, estimate)
}

object TaskCreateRequest {
  implicit val config: Configuration =
    Configuration.default

  implicit val zonedDateTime: Decoder[ZonedDateTime] =
    Decoder.decodeString.map(date => ZonedDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss zzz")))

  implicit val snakyDecoder: Decoder[TaskCreateRequest] = deriveDecoder
}