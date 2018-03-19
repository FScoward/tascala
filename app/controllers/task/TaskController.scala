package controllers.task

import javax.inject.Inject

import domain.model.ID
import domain.model.task.TaskRepository
import domain.model.user.User
import domain.repository.user.UserRepository
import play.api._
import play.api.mvc._
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.libs.circe.Circe
import io.circe.syntax._

class TaskController @Inject()(
    cc: ControllerComponents,
    repository: TaskRepository,
    userRepository: UserRepository
) extends AbstractController(cc)
    with Circe {

  def add(userId: Long) = Action(circe.json[TaskCreateRequest]) {
    implicit request =>
      val taskCreateRequest = request.body

      val x = for {
        user <- userRepository
          .findBy(ID[User](userId))
          .toRight(BadRequest("")) // todo Either
      } yield {
        val task = user.addTask(taskCreateRequest.title,
                                taskCreateRequest.description,
                                taskCreateRequest.deadline,
                                taskCreateRequest.estimate)
        repository.store(task)

        Ok(task.asJson)
      }

      x.fold(fa => fa, fb => fb)

  }

}
