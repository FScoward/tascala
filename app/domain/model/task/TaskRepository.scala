package domain.model.task

import domain.model.ID

trait TaskRepository {
  def save(task: Task): Int
  def findBy(taskId: ID[Task]): Option[Task]
}
