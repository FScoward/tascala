package controllers.base

import java.sql.SQLException

import play.api._
import play.api.mvc._
import play.api.mvc.Results._

trait ResultImplicit {
  val logger: Logger
  implicit class RichEither(either: Either[Result, Result]) {
    def toResult = either.fold(fa => fa, fb => fb)
  }

  implicit class ErrorHandler(e: Throwable) {
    def errorHandler = e match {
      case e: SQLException => {
        logger.error("SQLException", e)
        InternalServerError
      }
      case e => BadRequest(e.getMessage)
    }
  }

}
