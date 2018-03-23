package service.task

import java.time.ZonedDateTime
import javax.inject.{Inject, Singleton}

import controllers.exception.UserNotFound
import controllers.task.TaskCreateRequest
import domain.model.ID
import domain.model.task.{Task, TaskRepository}
import domain.model.user.User
import domain.repository.user.UserRepository
import scalikejdbc.AutoSession
import service._

@Singleton
class TaskService @Inject()(
    taskRepository: TaskRepository,
    userRepository: UserRepository
) {
  def list(_userId: Long): Either[Throwable, List[Task]] = {
    val userId = ID[User](_userId)
    for {
      _ <- userRepository.findBy(userId) ifNotExists new UserNotFound(userId)
      tasks <- taskRepository.listBy(userId).toEither
    } yield tasks
  }


  def create(_userId: Long, taskCreateRequest: TaskCreateRequest): Either[Throwable, Task] = {
    val userId = ID[User](_userId)
    for {
      user <- userRepository.findBy(userId) ifNotExists new UserNotFound(userId)
      task = user.addTask(taskCreateRequest.title, taskCreateRequest.description, taskCreateRequest.deadline, taskCreateRequest.estimate)
      _ <- taskRepository.store(task).toEither
    } yield task
  }

  def delete(userId: ID[User], taskId: ID[Task]): Either[Throwable, Unit] = {
    implicit val session = AutoSession
    for {
      user <- userRepository.findBy(userId) ifNotExists new UserNotFound(userId)
      _ <- taskRepository.delete(userId, taskId).toEither
    } yield ()
  }
}
