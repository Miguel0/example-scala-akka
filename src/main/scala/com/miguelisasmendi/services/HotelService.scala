package com.miguelisasmendi.services

import com.miguelisasmendi.entities.Hotel
import com.miguelisasmendi.dal.mapping.HotelRepository
import com.miguelisasmendi.resources.HotelSearchParameters
import scala.concurrent.{ExecutionContext, Future}

class HotelService(implicit val executionContext: ExecutionContext) {

  def getHotel(id: Long): Future[Option[Hotel]] = HotelRepository.getById(id)

  def getHotels(searchParameters: HotelSearchParameters = null): Future[List[Hotel]] = {
  	HotelRepository.getAll(searchParameters)
  }
}

object HotelService 