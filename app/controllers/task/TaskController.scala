package controllers.task

import javax.inject.Inject

import play.api._
import play.api.mvc._
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.libs.circe.Circe

class TaskController @Inject()(
    cc: ControllerComponents
) extends AbstractController(cc) with Circe {

  def add = Action(circe.json[TaskCreateRequest]) { implicit request =>
    println(request.body)

    Ok("")
  }

}
