package model

import play.api.libs.json.{JsObject, JsValue, Json, Reads, Writes}




case class CountryInformationAPI(name:String, region:String, subregion:String)
object CountryInformationAPI {
  implicit val reads = Json.reads[CountryInformationAPI]
  implicit val format = Json.format[CountryInformationAPI]

}

case class CountryNameAPI(countryName:String)
object CountryNameAPI {
  implicit val reads = Json.reads[CountryNameAPI]
  implicit val format = Json.format[CountryNameAPI]
}

case class CountryNotFoundAPI (status:Int,message:String)
object CountryNotFoundAPI{
  implicit val reads:Reads[CountryNotFoundAPI]= Json.reads[CountryNotFoundAPI]
  implicit val writes: Writes[CountryNotFoundAPI] = Json.writes[CountryNotFoundAPI]
  implicit val format = Json.format[CountryNotFoundAPI]
  implicit val nullWriter: Writes[CountryNotFoundAPI] = new Writes[CountryNotFoundAPI] {
    override def writes(o: CountryNotFoundAPI): JsValue = {
      JsObject(Seq(
        "error" -> Json.toJson(o.status),
        "data" -> Json.toJson(o.message)
        // Additional fields go here.
      ))
    }
  }
}

case class ContinentResponseAPI(status:String, error:CountryNotFoundAPI, errorMessage:String,data:CountryInformationAPI,displayMessage:String)
object ContinentResponseAPI {
  implicit val reads :Reads[ContinentResponseAPI]= Json.reads[ContinentResponseAPI]
  implicit val writes: Writes[ContinentResponseAPI] = Json.writes[ContinentResponseAPI]
  implicit val format = Json.format[ContinentResponseAPI]



  implicit val nullWriter: Writes[ContinentResponseAPI] = new Writes[ContinentResponseAPI] {
    override def writes(o: ContinentResponseAPI): JsValue = {
      JsObject(Seq(
        "error" -> Json.toJson(o.error),
      "data" -> Json.toJson(o.data)
        // Additional fields go here.
      ))
    }
  }
}

case class SiblingCountriesResponseAPI(areSiblings:Boolean, displayMessage:String)
object SiblingCountriesResponseAPI {
  implicit val reads = Json.reads[SiblingCountriesResponseAPI]
  implicit val writes: Writes[SiblingCountriesResponseAPI] = Json.writes[SiblingCountriesResponseAPI]
  implicit val format = Json.format[SiblingCountriesResponseAPI]
}