package model
import play.api.libs.json.{Json, OFormat, OWrites, Reads}

case class CityInformation(name:String, country:String, region:String)
object CityInformation {

  implicit val reads :Reads[CityInformation]= Json.reads[CityInformation]
  implicit val writes: OWrites[CityInformation] = Json.writes[CityInformation]
  implicit val format:OFormat[CityInformation] = Json.format[CityInformation]
}

case class CityInformationInsert(isEditedData:Boolean,cityInformation: CityInformation)
object CityInformationInsert {

  implicit val reads :Reads[CityInformationInsert]= Json.reads[CityInformationInsert]
  implicit val writes: OWrites[CityInformationInsert] = Json.writes[CityInformationInsert]
  implicit val format:OFormat[CityInformationInsert] = Json.format[CityInformationInsert]
}