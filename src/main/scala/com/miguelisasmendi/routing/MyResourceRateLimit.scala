package com.miguelisasmendi.routing

import akka.http.scaladsl.marshalling.{ToResponseMarshaller, ToResponseMarshallable}

import collection.mutable.HashMap
import scala.concurrent.{ExecutionContext, Future}
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.server.{Directives, Route}

import com.miguelisasmendi.serializers.JsonSupport
import com.miguelisasmendi.entities.Hotel

trait MyResourceRateLimiter extends MyResource {
  
  val rateLimitConfigMap = new HashMap[String, HashMap[String,RateLimitConfig]]();

  def dealWithRequest(particularKey:String, apiKey: String, future: Future[List[Hotel]]): Route = {
    getRateLimitFor(particularKey, apiKey).tick() match {
      case Some(elapsedTime) => complete(429, None)
      case None => complete(future)
    }
  }

  def getRateLimitFor(particularKey: String, apiKey: String): RateLimitConfig = {
      rateLimitConfigMap.get(particularKey).get(apiKey)
  }

  def addRateLimitFor(particularKey: String, apiKey: String, rateLimitConfig: RateLimitConfig) = {
    synchronized {
      rateLimitConfigMap.getOrElseUpdate(particularKey, {new HashMap[String,RateLimitConfig]()}).update(apiKey, rateLimitConfig)
    }
  }
}

class RateLimitConfig(delayMs:Int, suspentionTime: Int = 10000) {
  var last: Long = System.currentTimeMillis

  def tick(): Option[Long] = {
    val elapsed = synchronized {
      val now = System.currentTimeMillis
      val elapsed = now - last
      
      if (elapsed < delayMs)
        last = last + suspentionTime
      else
        last = now

      elapsed
    }

    Option(elapsed).filter(_ < delayMs)
  }
}