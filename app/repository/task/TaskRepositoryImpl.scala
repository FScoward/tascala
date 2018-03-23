package repository.task

import domain.model.ID
import domain.model.task.{Task, TaskRepository}
import domain.model.user.User
import repository.models.Tasks
import scalikejdbc._

import scala.util.Try

class TaskRepositoryImpl extends TaskRepository {
  override def store(task: Task): Try[Unit] = Try{
    Tasks.create(
      task.id.value,
      task.userId.value,
      task.title,
      task.description,
      task.deadline,
      task.estimate
    )
  }
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

  override def listBy(userId: ID[User]): Try[List[Task]] = Try {
    Tasks
      .findAllBy(SQLSyntax.eq(Tasks.t.userId, userId.value))
      .map(
        t =>
          Task(ID[Task](t.taskId),
               ID[User](t.userId),
               t.title,
               t.description,
               t.deadline,
               t.estimate))
  }

  override def delete(userId: ID[User], taskId: ID[Task])(implicit session: DBSession): Try[Unit] = Try {
    withSQL {
      deleteFrom(Tasks).where
        .eq(Tasks.column.userId, userId.value)
        .and
        .eq(Tasks.column.taskId, taskId.value)
    }.update.apply

  }
}
