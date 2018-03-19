package domain.model.user

import java.time.ZonedDateTime

import domain.model.task.Task
import domain.model.{ID, Id64}

case class User(id: ID[User] = ID[User](Id64.nextAscId()), name: String) {
  def addTask(
      title: String,
      description: Option[String],
      deadline: Option[ZonedDateTime],
      estimate: Option[Int]
  ) = Task(
    ID[Task](Id64.nextAscId()),
    this.id,
    title,
    description,
    deadline,
    estimate
  )
}
