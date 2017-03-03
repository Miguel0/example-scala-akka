package com.miguelisasmendi.resources

import akka.http.scaladsl.server.Route

import com.miguelisasmendi.entities.Hotel
import com.miguelisasmendi.routing.{MyResource, MyResourceRateLimiter, RateLimitConfig}
import com.miguelisasmendi.services.HotelService

trait HotelResource extends MyResourceRateLimiter {

  val hotelService: HotelService
  
  val particularKey: String = "hotel";

  addRateLimitFor(particularKey, "DefaultKey", new RateLimitConfig(10000, 5000))
  addRateLimitFor(particularKey, "CustomKey", new RateLimitConfig(10000, 2000))

  def questionRoutes: Route = pathPrefix("hotel") {
    path(Segment) { id =>
      get {
        complete(hotelService.getHotel(id.filter(_.isDigit).toLong))
      }
    } ~
    pathEnd {
      get {
        extractRequestContext { requestContext =>
          optionalHeaderValueByName("apiKey") {
              apiKey => {
              var apiKeyRetrieved = apiKey getOrElse { "DefaultKey" }

              parameters( "cityId" ?, "priceSort" ?) { (cityId: Option[String], priceSort: Option[String]) => 
                val searchParameters = new HotelSearchParameters(cityId, priceSort)

                //complete(hotelService.getHotels(searchParameters))
                dealWithRequest(particularKey, apiKeyRetrieved, hotelService.getHotels(searchParameters))
              }
            }
          }
        }
      }
    }
  }
}

