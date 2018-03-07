package domain.model.task

import java.time.ZonedDateTime

import domain.model.ID
import domain.model.user.User

case class Task(
    id: ID[Task],
    userId: ID[User],
    title: String,
    description: Option[String],
    deadline: ZonedDateTime,
    estimate: Int
)
