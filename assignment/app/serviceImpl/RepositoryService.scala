package controllers

import javax.inject.{Inject, Singleton}
import model.{CityInformation, CityInformationInsert, CityInsertResponse, RemoveResponse}
import play.api.libs.json.{JsObject, Json, Writes}
import play.api.mvc._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.api.commands.WriteResult
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection
import service.RepositoryServiceT
import serviceImpl.HelperService
import util.MyWorldConstants

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RepositoryService @Inject()(cc: ControllerComponents,
                                  val reactiveMongoApi:ReactiveMongoApi,
                                  helperService: HelperService)
                                 (implicit exec: ExecutionContext)
                                  extends AbstractController(cc)
                                    with MongoController with ReactiveMongoComponents with RepositoryServiceT {


def getCities(coll:JSONCollection,name:String):Future[List[CityInformation]]={
  val query = Json.obj("region"-> name)
  val cursor: Cursor[CityInformation] = coll.find(query).cursor[CityInformation](ReadPreference.Primary)
  for{
    result <-  cursor.collect[List](25, Cursor.FailOnError[List[CityInformation]]())
  }yield
    result
}

  def getAllCities(coll:JSONCollection):Future[List[CityInformation]]={
    val query = Json.obj()
    val cursor: Cursor[CityInformation] = coll.find(query).cursor[CityInformation](ReadPreference.Primary)
    for{
      res<- cursor.collect[List](30, Cursor.FailOnError[List[CityInformation]]())
    }yield res
  }

  def insertCity(coll:JSONCollection,cityInformation: CityInformation,entryType:String): Future[CityInsertResponse] = {
   for {
     cityInfoToInsert <- helperService.createInsertCityRequest(entryType,cityInformation)
     writeRes <-  coll.insert.one(cityInfoToInsert.cityInformation)
     res <-helperService.createInsertCityResponse(writeRes,cityInfoToInsert.isEditedData)
   }
  yield res

  }

  def deleteContinent(coll:JSONCollection,name:String):Future[RemoveResponse] = {
    val selector = Json.obj("region"-> name)
    for {
      removedDataResponse <- coll.remove(selector)
      finalResponse <- helperService.createRemoveContinentResponse(removedDataResponse)
    }
      yield finalResponse
  }
}
