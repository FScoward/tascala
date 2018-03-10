package repository.task

import domain.model.ID
import domain.model.task.{Task, TaskRepository}
import repository.models.Tasks

class TaskRepositoryImpl extends TaskRepository {
  override def save(task: Task): Unit = {
    val repoTask = Tasks(
      taskId = task.id.value,
      userId = task.userId.value,
      title = task.title,
      description = task.description,
      deadline = task.deadline,
      estimate = task.estimate
    )

    repoTask.save()
  }

  override def findBy(taskId: ID[Task]): Option[Task] = ???
}
