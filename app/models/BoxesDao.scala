package models

import javax.inject.Inject
import models.database.DBService
import scala.concurrent.ExecutionContext

class BoxesDao @Inject()(dBService: DBService)(implicit ec: ExecutionContext){

}
