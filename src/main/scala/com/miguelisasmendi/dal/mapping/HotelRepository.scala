package com.miguelisasmendi.dal.mapping

import com.miguelisasmendi.dal.connection.{DBComponent, MySqlDBComponent}
import scala.concurrent.Future
import com.miguelisasmendi.entities.Hotel
import com.miguelisasmendi.resources.HotelSearchParameters

trait HotelRepository extends HotelTable { this: DBComponent =>

  import driver.api._

  def getById(id: Long): Future[Option[Hotel]] = db.run { hotelTableQuery.filter(_.id === id).result.headOption }
  
  def getAll(searchParameters: HotelSearchParameters): Future[List[Hotel]] = db.run {

    val query = hotelTableQuery
      .filter(_.cityId === searchParameters.cityId.orNull)
      .sortBy(tableQueryConf => {
          searchParameters.priceSort.orNull match {
            case "DESC" => tableQueryConf.price.desc
            case "ASC" => tableQueryConf.price.asc
            case _ => tableQueryConf.id.asc
          }
        }
      )
    
    query.to[List].result
  }
}

private[mapping] trait HotelTable { this: DBComponent =>

  import driver.api._

  private[HotelTable] class HotelTable(tag: Tag) extends Table[Hotel](tag, "hotel") {
    val id = column[Long]("hotelid", O.PrimaryKey, O.AutoInc)
    val cityId = column[String]("city")
    val price = column[Long]("price")
    val room = column[String]("room")

    def * = (id, cityId, price, room) <> (Hotel.tupled, Hotel.unapply)

  }

  protected val hotelTableQuery = TableQuery[HotelTable]
}

object HotelRepository extends HotelRepository with MySqlDBComponent
