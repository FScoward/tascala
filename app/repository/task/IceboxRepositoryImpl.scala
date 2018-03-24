package repository.task

import domain.model.ID
import domain.model.task.{Icebox, Task, IceboxRepository}
import domain.model.user.User
import repository.models.{Iceboxs, Tasks}
import scalikejdbc._

import scala.util.Try

class IceboxRepositoryImpl extends IceboxRepository {
  override def store(task: Task): Try[Unit] = Try {
    Tasks.create(
      task.id.value,
      task.userId.value,
      task.title,
      task.description,
      task.deadline,
      task.estimate
    )

    Iceboxs.create(task.id.value)
  }
  override def save(task: Task): Try[Unit] = Try {
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

  override def findBy(taskId: ID[Task]): Try[Option[Task]] = Try {
    Tasks
      .find(taskId.value)
      .map(
        t =>
          Icebox(
            id = ID[Task](t.taskId),
            userId = ID[User](t.userId),
            title = t.title,
            description = t.description,
            deadline = t.deadline,
            estimate = t.estimate
        ))
  }

  override def listBy(userId: ID[User])(
      implicit session: DBSession): Try[List[Task]] = Try {
    withSQL {
      selectFrom(Iceboxs as Iceboxs.i)
        .leftJoin(Tasks as Tasks.t)
        .on(Iceboxs.i.taskId, Tasks.t.taskId)
        .where
        .eq(Tasks.t.userId, userId.value)
    }.map(rs => Tasks(Tasks.t)(rs)).list.apply.map { t =>
      Icebox(
        id = ID[Task](t.taskId),
        userId = ID[User](t.userId),
        title = t.title,
        description = t.description,
        deadline = t.deadline,
        estimate = t.estimate
      )

    }
  }

  override def delete(task: Task)(implicit session: DBSession): Try[Unit] =
    Try {
      withSQL {
        deleteFrom(Iceboxs).where
          .eq(Iceboxs.column.taskId, task.id.value)
      }.update.apply
    }

  override def remove(userId: ID[User], taskId: ID[Task]): Try[Unit] = Try {
    Iceboxs.destroy(Iceboxs(taskId.value))
  }
}
