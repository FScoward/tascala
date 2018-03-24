package repository.task

import domain.model.ID
import domain.model.task.{Doing, DoingRepository, Task}
import domain.model.user.User
import repository.models.{Doings, Tasks}
import scalikejdbc._

import scala.util.Try

class DoingRepositoryImpl extends DoingRepository {
  override def store(task: Task): Try[Unit] = ???

  override def findBy(taskId: ID[Task], userId: ID[User])(implicit session: DBSession): Try[Option[Task]] = Try {
    withSQL {
      selectFrom(Doings as Doings.d)
        .leftJoin(Tasks as Tasks.t)
        .on(Doings.d.taskId, Tasks.t.taskId)
        .where
        .eq(Tasks.t.userId, userId.value)
    }.map(rs => (Tasks(Tasks.t)(rs), Doings(Doings.d)(rs))).first.apply.map {
      case (t, d) =>
        Doing(
          id = ID[Task](t.taskId),
          userId = ID[User](t.userId),
          title = t.title,
          description = t.description,
          deadline = t.deadline,
          estimate = t.estimate,
          start = d.start,
          end = d.end
        )
    }
  }

  override def delete(task: Task)(implicit session: DBSession): Try[Unit] =
    Try {
      withSQL {
        deleteFrom(Doings).where.eq(Doings.d.taskId, task.id.value)
      }.update.apply
    }
}
