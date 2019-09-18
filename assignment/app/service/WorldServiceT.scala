package service

import model.{AllContinentsResponse, CityInformation, CityInsertResponse, RemoveResponse, SortCityResponse}

import scala.concurrent.Future

trait WorldServiceT {
  def findCities(name:String):Future[List[CityInformation]]
  def addCity(cityInformation: CityInformation):Future[CityInsertResponse]
  def addContinent(cityInformation: CityInformation):Future[CityInsertResponse]
  def sortCities(name:String):Future[SortCityResponse]
  def deleteContinent(name:String):Future[RemoveResponse]
  def getAllContinents():Future[AllContinentsResponse]

}