package model

import play.api.libs.json.{Json, Reads, Writes}

case class CityInsertResponse(status:String, error:String, errorMessage:String,displayMessage:String)

object CityInsertResponse {
  implicit val reads : Reads[CityInsertResponse] =Json.reads[CityInsertResponse]
  implicit val writes: Writes[CityInsertResponse] = Json.writes[CityInsertResponse]

  implicit val format = Json.format[CityInsertResponse]

}


case class RemoveResponse(status:String,displayMessage:String)

object RemoveResponse {
  implicit val reads : Reads[RemoveResponse] =Json.reads[RemoveResponse]
  implicit val writes: Writes[RemoveResponse] = Json.writes[RemoveResponse]

  implicit val format = Json.format[RemoveResponse]

}
