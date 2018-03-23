import scala.util.Try

package object service {
  implicit class TryOptionOps[T](t: Try[Option[T]]) {
    def ifNotExists(e: Exception) = {
      t.fold(fa => Left(fa), fb => fb.toRight(e))
    }
  }

}
