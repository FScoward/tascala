package domain.model.task

import java.time.ZonedDateTime

import domain.model.ID
import domain.model.user.User

trait Task {
  val id: ID[Task]
  val userId: ID[User]
  val title: String
  val description: Option[String]
  val deadline: Option[ZonedDateTime]
  val estimate: Option[Int]

  val status: String

  def done(
      requiredTime: Int,
      doneDateTime: ZonedDateTime = ZonedDateTime.now()
  ) = Done(
    id = id,
    userId = userId,
    title = title,
    description = description,
    deadline = deadline,
    estimate = estimate,
    requiredTime = requiredTime,
    doneDateTime = doneDateTime
  )

  def update(title: String, description: Option[String], deadline: Option[ZonedDateTime], estimate: Option[Int]): Task
}
