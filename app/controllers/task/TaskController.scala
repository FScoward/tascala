package controllers.task

import javax.inject.Inject

import domain.model.ID
import domain.model.task.{Task, TaskRepository}
import domain.model.user.User
import domain.repository.user.UserRepository
import play.api._
import play.api.mvc._
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.libs.circe.Circe
import io.circe.syntax._
import _root_.controllers.base.ResultImplicit
import service.task.TaskService

class TaskController @Inject()(
    cc: ControllerComponents,
    repository: TaskRepository,
    userRepository: UserRepository,
    taskService: TaskService
) extends AbstractController(cc)
    with Circe
    with ResultImplicit {

  def list(userId: Long) = Action { implicit request =>
    (for {
      _ <- userRepository.findBy(ID[User](userId))
      tasks <- repository.listBy(ID[User](userId))
    } yield {
      Ok(tasks.asJson)
    }).getOrElse(NotFound(""))
  }

  def add(userId: Long) = Action(circe.json[TaskCreateRequest]) {
    implicit request =>
      val taskCreateRequest = request.body

      val result = for {
        task <- taskService.create(userId, taskCreateRequest)
      } yield task

      result.fold(
        fa =>
          fa match {
            // TODO Exceptionによって振り分ける
            case _ => BadRequest(fa.getMessage.asJson)
        },
        fb => Ok(fb.asJson)
      )
  }

  def delete(_userId: Long, _taskId: Long) = Action { implicit request =>
    val userId = ID[User](_userId)
    val taskId = ID[Task](_taskId)

    (for {
      _ <- taskService.delete(userId, taskId)
    } yield ()).fold(fa => BadRequest(fa.getMessage), _ => Ok(userId.value.asJson))

  }

}
