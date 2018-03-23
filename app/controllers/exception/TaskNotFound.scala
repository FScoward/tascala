package controllers.exception

import domain.model.ID
import domain.model.task.Task

case class TaskNotFound (taskId: ID[Task]) extends Exception {
  override def getMessage: String = s"task not found. taskId = ${taskId.value}"
}
