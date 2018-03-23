package domain.model.task

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import domain.model.ID
import domain.model.user.User
import io.circe.Encoder
import io.circe.generic.semiauto._


case class Icebox(
    id: ID[Task],
    userId: ID[User],
    title: String,
    description: Option[String],
    deadline: Option[ZonedDateTime],
    estimate: Option[Int]
) extends Task {
  val status = "ICEBOX"
  override def update(
      title: String,
      description: Option[String],
      deadline: Option[ZonedDateTime],
      estimate: Option[Int]
  ): Icebox = this.update(
    title,
    description,
    deadline,
    estimate
  )
}

//object Icebox {
//  implicit val encodeZonedDateTime: Encoder[ZonedDateTime] =
//    Encoder.encodeString.contramap[ZonedDateTime](
//      _.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
//  implicit def idEncoder[T]: Encoder[ID[T]] =
//    Encoder.encodeString.contramap[ID[T]](_.value.toString)
//  implicit val taskEncoder: Encoder[Icebox] = deriveEncoder[Icebox]
//}
