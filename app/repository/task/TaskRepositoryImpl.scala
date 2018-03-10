package repository.task

import domain.model.ID
import domain.model.task.{Task, TaskRepository}

class TaskRepositoryImpl extends TaskRepository {
  override def save(task: Task): Int = ???

  override def findBy(taskId: ID[Task]): Option[Task] = ???
}
