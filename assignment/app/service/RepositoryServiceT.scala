package service

import model.{CityInformation, CityInsertResponse, RemoveResponse}
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future

trait RepositoryServiceT {
  def getCities(coll:JSONCollection,name:String):Future[List[CityInformation]]
  def getAllCities(coll:JSONCollection):Future[List[CityInformation]]
  def insertCity(coll:JSONCollection,cityInformation: CityInformation,entryType:String): Future[CityInsertResponse]
  def deleteContinent(coll:JSONCollection,name:String):Future[RemoveResponse]
}