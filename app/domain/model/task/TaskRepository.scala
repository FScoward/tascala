package domain.model.task

import domain.model.ID

trait TaskRepository {
  def save(task: Task): Unit
  def findBy(taskId: ID[Task]): Option[Task]
}
