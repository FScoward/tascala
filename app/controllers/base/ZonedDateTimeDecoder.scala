package controllers.base

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import io.circe.Decoder

trait ZonedDateTimeDecoder {
  implicit val zonedDateTime: Decoder[ZonedDateTime] =
    Decoder.decodeString.map(date => ZonedDateTime.parse(date, DateTimeFormatter.ISO_OFFSET_DATE_TIME))
}
