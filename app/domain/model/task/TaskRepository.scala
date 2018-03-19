package domain.model.task

import domain.model.ID
import repository.models.Tasks

trait TaskRepository {
  def store(task: Task): Unit
  def save(task: Task): Unit
  def findBy(taskId: ID[Task]): Option[Task]
}
