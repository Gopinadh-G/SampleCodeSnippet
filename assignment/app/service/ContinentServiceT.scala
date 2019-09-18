package service

import model.ContinentResponseAPI

import scala.concurrent.Future

trait ContinentServiceT{
  def getContinentForCountry(name:String):Future[ContinentResponseAPI]
}