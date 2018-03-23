package repository.task

import domain.model.task.{Task, TaskRepository}
import repository.models.Tasks
import scalikejdbc._

import scala.util.Try

class TaskRepositoryImpl extends TaskRepository {
  override def delete(task: Task)(implicit session: DBSession): Try[Unit] = Try {
    withSQL {
      deleteFrom(Tasks).where
        .eq(Tasks.column.userId, task.userId.value)
        .and
        .eq(Tasks.column.taskId, task.id.value)
    }.update.apply
  }

}
