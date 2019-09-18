package serviceImpl

import reactivemongo.play.json.collection.JSONCollection
import util.MyWorldConstants

import scala.concurrent.Future
import scala.language.postfixOps
class WorldServiceSpec extends DefaultTestClass {

  val futureCollection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection[JSONCollection](MyWorldConstants.CitiesDBCollection));

  /***
   * findCities test cases start here

  "The findCities for both a city Name" should {
    "return a positive response " in {

      var response: List[CityInformation] =

        Await.result( worldService.findCities("Asia")
          , 500 millis)
      println(response.toSeq)
    }
  }
****/


}
