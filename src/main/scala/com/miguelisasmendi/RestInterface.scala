package com.miguelisasmendi

import scala.concurrent.ExecutionContext

import akka.http.scaladsl.server.Route

import com.miguelisasmendi.resources.HotelResource
import com.miguelisasmendi.services.HotelService

trait RestInterface extends Resources {

  implicit def executionContext: ExecutionContext

  lazy val hotelService = new HotelService

  val routes: Route = questionRoutes

}

trait Resources extends HotelResource