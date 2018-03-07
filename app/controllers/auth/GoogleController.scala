package controllers.auth

import javax.inject._

import play.api._
import play.api.mvc._
import play.api.Configuration
import service.auth.GoogleOAuthService
import io.circe.syntax._
import play.api.libs.json.Json

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class GoogleController @Inject()(
    cc: ControllerComponents,
    config: Configuration,
    googleOAuthService: GoogleOAuthService
) extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def request() = Action { implicit request: Request[AnyContent] =>
    Redirect(googleOAuthService.redirectUrl)
  }

  def callback() = Action { implicit request =>
    val result = googleOAuthService.callback(request.getQueryString("code").get)

    Ok(Json.toJson(result))
  }
}
