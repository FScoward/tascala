package repository.task

import domain.model.ID
import domain.model.task.{Done, DoneRepository, Task}
import domain.model.user.User
import repository.models.{Dones, Iceboxs, Tasks}
import scalikejdbc.DBSession
import scalikejdbc._

import scala.util.Try

class DoneRepositoryImpl extends DoneRepository {
  override def store(done: Done): Try[Unit] = Try {
    Dones.create(
      done.id.value,
      done.requiredTime,
      done.doneDateTime
    )
  }

  override def findBy(taskId: ID[Task])(
      implicit session: DBSession): Try[Option[Done]] = Try {
    withSQL {
      selectFrom(Dones as Dones.d)
        .leftJoin(Tasks as Tasks.t)
        .on(Dones.d.taskId, Tasks.t.taskId)
        .where
        .eq(Dones.d.taskId, taskId.value)
    }.map(rs => (Tasks(Tasks.t)(rs), Dones(Dones.d)(rs))).first.apply.map {
      case (t, d) =>
        Done(
          id = ID[Task](t.taskId),
          userId = ID[User](t.userId),
          title = t.title,
          description = t.description,
          deadline = t.deadline,
          estimate = t.estimate,
          requiredTime = d.requiredTime,
          doneDateTime = d.doneDateTime
        )
    }
  }

  override def listBy(userId: ID[User])(
      implicit session: DBSession
  ): Try[List[Task]] = Try {
    withSQL {
      selectFrom(Dones as Dones.d)
        .leftJoin(Tasks as Tasks.t)
        .on(Dones.d.taskId, Tasks.t.taskId)
        .where
        .eq(Tasks.t.userId, userId.value)
    }.map(rs => (Tasks(Tasks.t)(rs), Dones(Dones.d)(rs))).list.apply.map {
      case (t, d) =>
        Done(
          id = ID[Task](t.taskId),
          userId = ID[User](t.userId),
          title = t.title,
          description = t.description,
          deadline = t.deadline,
          estimate = t.estimate,
          requiredTime = d.requiredTime,
          doneDateTime = d.doneDateTime
        )
    }
  }

  override def delete(task: Task)(implicit session: DBSession): Try[Unit] =
    Try {
      withSQL {
        deleteFrom(Dones).where
          .eq(Dones.d.taskId, task.id.value)
      }.update.apply
    }

}
