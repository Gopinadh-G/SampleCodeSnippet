package serviceImpl

import controllers.RepositoryService
import javax.inject.{Inject, Singleton}
import model._
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json.collection.JSONCollection
import service.WorldServiceT
import util.MyWorldConstants

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class WorldService @Inject()(val reactiveMongoApi:ReactiveMongoApi,
                             repositoryService: RepositoryService,
                             authenticationService: AuthenticationService,
                             helperService: HelperService)
                            (implicit exec: ExecutionContext)
  extends  WorldServiceT {


  val futureCollection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection[JSONCollection](MyWorldConstants.CitiesDBCollection));

  def findCities(name:String):Future[List[CityInformation]]= {
    for {
      futureCollection <- futureCollection
      res <- repositoryService.getCities(futureCollection, name)
    }
      yield res
  }
  def addCity(cityInformation: CityInformation):Future[CityInsertResponse]= {
    cityInformation.name.trim() match {
      case "" => {
        for {
          result <- helperService.createEmptyFieldResponse(cityInformation, MyWorldConstants.CityText)
        } yield {
          result
        }
      }
      case _ => {
        for {
          futureCollection <- futureCollection
          res <- repositoryService.insertCity(futureCollection, cityInformation, MyWorldConstants.CityText)
        }
          yield res
      }
    }
  }

  def addContinent(cityInformation: CityInformation):Future[CityInsertResponse]= {
    cityInformation.region.trim() match {
      case "" => {
        for {
          result <- helperService.createEmptyFieldResponse(cityInformation, MyWorldConstants.ContinentText)
        } yield {
          result
        }
      }
      case _ => {
        for {
          futureCollection <- futureCollection
          res <- repositoryService.insertCity(futureCollection, cityInformation, MyWorldConstants.ContinentText)
        }
          yield res
      }
    }
  }

  def sortCities(name:String):Future[SortCityResponse]= {
    for {
      isUserAuthenticated <- isUserAuthenticatedForSortCity(name)
      finalData <- constructSortCityResponseforUser(isUserAuthenticated)
    } yield finalData
  }


  def deleteContinent(name:String):Future[RemoveResponse]= {
    for{
      futureCollection <-futureCollection
      res <-repositoryService.deleteContinent(futureCollection, name)
    }
      yield res
  }

    def getAllContinents():Future[AllContinentsResponse] ={
      for{
        coll <- futureCollection
        allContinents <- repositoryService.getAllCities(coll)
      }yield{
        val result = allContinents.map(_.region)
        new AllContinentsResponse(result.distinct)
      }
    }

  /**
   * Private methods
   */

  private def readCitiesFromDB():Future[Map[String,List[String]]]={
    for {
      coll <-futureCollection
      allCities <-repositoryService.getAllCities(coll)
    }yield{
      val mapData = allCities.groupMap(_.region)(_.name)
      mapData.transform((k, v) => v.sorted)
    }
  }

  private def constructSortCityResponseforUser(isAuthenticUser:Boolean):Future[SortCityResponse]={
    isAuthenticUser match{
      case false =>Future{new SortCityResponse(MyWorldConstants.FALSE, Map.empty)}
      case _=>{
        for{
          finalData <- readCitiesFromDB()
        }yield {new SortCityResponse(MyWorldConstants.TRUE,finalData)}
      }
    }
  }


  private def isUserAuthenticatedForSortCity(name :String):Future[Boolean] ={
    val futureAuthentication=authenticationService.isAuthenticatedUser(name)
    for{
      auth <-futureAuthentication
    }
      yield{
        auth.isAuthenticated
      }
  }
}



