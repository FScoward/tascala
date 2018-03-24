package controllers.task

import javax.inject.Inject

import domain.model.ID
import domain.model.task.Task
import domain.model.user.User
import play.api._
import play.api.mvc._
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.libs.circe.Circe
import io.circe.syntax._
import _root_.controllers.base.ResultImplicit
import appservice.task.{TaskDoneRequest, TaskService}

class TaskController @Inject()(
    cc: ControllerComponents,
    taskService: TaskService
) extends AbstractController(cc)
    with Circe
    with ResultImplicit {

  /** タスク一覧を取得する */
  def list(userId: Long) = Action { implicit request =>
    taskService
      .list(userId)
      .fold(
        fa => BadRequest(fa.getMessage),
        fb => {
          val result = fb.map(t => {
            TaskResponse(
              id = t.id.value,
              userId = t.userId.value,
              title = t.title,
              description = t.description,
              deadline = t.deadline,
              estimate = t.estimate,
              status = t.status
            )
          })
          Ok(result.asJson)
        }
      )
  }

  /**
    * タスクをIceboxへ追加する
    * */
  def add(userId: Long) = Action(circe.json[TaskCreateRequest]) {
    implicit request =>
      val taskCreateRequest = request.body

      val result = for {
        task <- taskService.create(userId, taskCreateRequest)
      } yield task

      result.fold(
        {
          // TODO Exceptionによって振り分ける
          case fa => BadRequest(fa.getMessage.asJson)
        },
        fb => {
          val response = TaskResponse(
            id = fb.id.value,
            userId = fb.userId.value,
            title = fb.title,
            description = fb.description,
            deadline = fb.deadline,
            estimate = fb.estimate,
            status = fb.status
          )
          Ok(response.asJson)
        }
      )
  }

  /**
    * タスク内容の更新
    * */
  def update(userId: Long, taskId: Long) =
    Action(circe.json[TaskUpdateRequest]) { implicit request =>
      taskService
        .update(userId, taskId, request.body)
        .fold(
          fa => BadRequest(fa.getMessage),
          fb => {
            val response = TaskResponse(
              id = fb.id.value,
              userId = fb.userId.value,
              title = fb.title,
              description = fb.description,
              deadline = fb.deadline,
              estimate = fb.estimate,
              status = fb.status
            )
            Ok(response.asJson)
          }
        )
    }

  /** タスクの削除 */
  def delete(_userId: Long, _taskId: Long) = Action { implicit request =>
    val userId = ID[User](_userId)
    val taskId = ID[Task](_taskId)

    (for {
      _ <- taskService.delete(userId, taskId)
    } yield
      ()).fold(fa => BadRequest(fa.getMessage), _ => Ok(userId.value.asJson))
  }

  /**
    * タスクを完了状態にする
    * */
  def done(_userId: Long, _taskId: Long) = Action(circe.json[TaskDoneRequest]) {
    implicit request =>
      val userId = ID[User](_userId)
      val taskId = ID[Task](_taskId)

      taskService
        .done(userId, taskId, request.body)
        .fold(
          fa => BadRequest(fa.getMessage),
          fb => {
            val response = TaskResponse(
              id = fb.id.value,
              userId = fb.userId.value,
              title = fb.title,
              description = fb.description,
              deadline = fb.deadline,
              estimate = fb.estimate,
              status = fb.status
            )
            Ok(response.asJson)
          }
        )
  }

}
