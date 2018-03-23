package domain.model.task

import java.time.ZonedDateTime

import domain.model.ID
import domain.model.user.User

case class Done(
    id: ID[Task],
    userId: ID[User],
    title: String,
    description: Option[String],
    deadline: Option[ZonedDateTime],
    estimate: Option[Int],
    requiredTime: Int,
    doneDateTime: ZonedDateTime
) extends Task {
  val status = "DONE"
  override def update(
      title: String,
      description: Option[String],
      deadline: Option[ZonedDateTime],
      estimate: Option[Int]
  ): Task = this.update(
    title,
    description,
    deadline,
    estimate
  )
}
