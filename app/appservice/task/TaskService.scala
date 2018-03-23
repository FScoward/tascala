package appservice.task

import javax.inject.{Inject, Named, Singleton}

import controllers.exception.{TaskNotFound, UserNotFound}
import controllers.task.{TaskCreateRequest, TaskUpdateRequest}
import domain.model.ID
import domain.model.task._
import domain.model.user.User
import domain.repository.user.UserRepository
import scalikejdbc.AutoSession
import appservice._

@Singleton
class TaskService @Inject()(
    taskRepository: TaskRepository,
    iceboxRepository: IceboxRepository,
    userRepository: UserRepository,
    doneRepository: DoneRepository
) {
  def list(_userId: Long): Either[Throwable, List[Task]] = {
    implicit val session = AutoSession
    val userId = ID[User](_userId)
    for {
      _ <- userRepository.findBy(userId) ifNotExists new UserNotFound(userId)
      iceboxTasks <- iceboxRepository.listBy(userId).toEither
      doneTasks <- doneRepository.listBy(userId).toEither
    } yield iceboxTasks ++ doneTasks
  }

  def create(
      _userId: Long,
      taskCreateRequest: TaskCreateRequest
  ): Either[Throwable, Task] = {
    val userId = ID[User](_userId)
    for {
      user <- userRepository.findBy(userId) ifNotExists UserNotFound(userId)
      task = user.addTask(
        taskCreateRequest.title,
        taskCreateRequest.description,
        taskCreateRequest.deadline,
        taskCreateRequest.estimate
      )
      _ <- iceboxRepository.store(task).toEither
    } yield task
  }

  def update(
      _userId: Long,
      _taskId: Long,
      taskUpdateRequest: TaskUpdateRequest
  ): Either[Throwable, Task] = {
    val userId = ID[User](_userId)
    val taskId = ID[Task](_taskId)
    for {
      user <- userRepository.findBy(userId) ifNotExists UserNotFound(userId)
      iceboxTask <- iceboxRepository.findBy(taskId) ifNotExists TaskNotFound(
        taskId)
      updated = user.updateTask(
        iceboxTask,
        taskUpdateRequest.title,
        taskUpdateRequest.description,
        taskUpdateRequest.deadline,
        taskUpdateRequest.estimate
      )
      _ <- iceboxRepository.save(updated).toEither
    } yield updated
  }

  /** タスクの削除 */
  def delete(userId: ID[User], taskId: ID[Task]): Either[Throwable, Unit] = {
    implicit val session = AutoSession
    for {
      _ <- userRepository.findBy(userId) ifNotExists UserNotFound(userId)
      iceboxTask <- iceboxRepository.findBy(taskId) ifNotExists TaskNotFound(
        taskId)
      _ <- iceboxRepository.delete(iceboxTask).toEither
      _ <- taskRepository.delete(iceboxTask).toEither
      // TODO delete other relational tables
    } yield ()
  }

  /**
    * タスクを完了にする
    * */
  def done(
      userId: ID[User],
      taskId: ID[Task],
      taskDoneRequest: TaskDoneRequest
  ): Either[Throwable, Done] = {
    implicit val session = AutoSession
    for {
      _ <- userRepository.findBy(userId) ifNotExists UserNotFound(userId)
      iceboxTask <- iceboxRepository.findBy(taskId) ifNotExists TaskNotFound(
        taskId)
      done = iceboxTask.done(taskDoneRequest.requiredTime,
                             taskDoneRequest.doneDateTime)
      _ <- doneRepository.store(done).toEither
      _ <- iceboxRepository.remove(userId, taskId).toEither
    } yield done
  }
}
