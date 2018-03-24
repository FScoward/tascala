package domain.model.task

import java.time.ZonedDateTime

import domain.model.ID
import domain.model.user.User

case class Doing(id: ID[Task],
                 userId: ID[User],
                 title: String,
                 description: Option[String],
                 deadline: Option[ZonedDateTime],
                 estimate: Option[Int],
                 start: ZonedDateTime,
                 end: ZonedDateTime)
    extends Task {
  val status = "DOING"

  override def update(title: String,
                      description: Option[String],
                      deadline: Option[ZonedDateTime],
                      estimate: Option[Int]): Task =
    this.update(title, description, deadline, estimate)
}
