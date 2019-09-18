package model

import play.api.libs.json.{Json, Writes}

case class AuthenticationRequest(name:String,pwd:String)

object AuthenticationRequest {
  implicit val reads = Json.reads[AuthenticationRequest]
  implicit val writes: Writes[AuthenticationRequest] = Json.writes[AuthenticationRequest]
  implicit val format = Json.format[AuthenticationRequest]
}


case class AuthenticationResponse(username:String,isAuthenticated:Boolean,status:String)
object AuthenticationResponse {
  implicit val reads = Json.reads[AuthenticationResponse]
  implicit val writes: Writes[AuthenticationResponse] = Json.writes[AuthenticationResponse]
  implicit val format = Json.format[AuthenticationResponse]
}