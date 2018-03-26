package controllers.auth

import javax.inject.Inject

import play.api._
import play.api.mvc._
import play.api.Configuration

import com.mohiva.play.silhouette.api.Silhouette

class AuthenticateController @Inject()(
    cc: ControllerComponents,
    config: Configuration,
    silhouette: Silhouette[JWTEnv]
) extends AbstractController(cc) {
  def signIn = silhouette.SecuredAction(WithProvider[JWTEnv#A]("twitter")) { implicit request =>

    Ok("ok")
  }
}
