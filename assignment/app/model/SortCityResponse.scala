package model

import play.api.libs.json.{Json, Reads, Writes}

case class SortCityResponse(isAuthenticated:Boolean,cityMap:Map[String,List[String]])

object SortCityResponse {
  implicit val reads : Reads[SortCityResponse] =Json.reads[SortCityResponse]
  implicit val writes: Writes[SortCityResponse] = Json.writes[SortCityResponse]

  implicit val format = Json.format[SortCityResponse]

}


case class AllContinentsResponse(continentsList:List[String])

object AllContinentsResponse {
  implicit val reads : Reads[AllContinentsResponse] =Json.reads[AllContinentsResponse]
  implicit val writes: Writes[AllContinentsResponse] = Json.writes[AllContinentsResponse]

  implicit val format = Json.format[AllContinentsResponse]

}