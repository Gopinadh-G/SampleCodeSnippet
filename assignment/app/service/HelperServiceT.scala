package service

import model.{CityInformation, CityInformationInsert, CityInsertResponse, ContinentResponseAPI, RemoveResponse, SiblingCountriesResponseAPI}
import reactivemongo.api.commands.WriteResult

import scala.concurrent.Future

trait HelperServiceT {
  def createSiblingResponse(firstResponse:ContinentResponseAPI,secondResponse:ContinentResponseAPI):Future[SiblingCountriesResponseAPI]
  def createInsertCityResponse(writeRes:WriteResult,isCustomisedInput:Boolean):Future[CityInsertResponse]
  def createRemoveContinentResponse(writeRes:WriteResult):Future[RemoveResponse]
  def createEmptyFieldResponse(cityInformation: CityInformation,entryType:String): Future[CityInsertResponse]
  def createInsertCityRequest(entryType:String,cityInformation: CityInformation):Future[CityInformationInsert]
}