package controllers.base

import play.api.mvc.Result

trait ResultImplicit {
  implicit class RichEither(either: Either[Result, Result]) {
    def toResult = either.fold(fa => fa, fb => fb)
  }

}
