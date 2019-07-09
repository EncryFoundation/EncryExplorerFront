package models

case class Node(ip: String,
                status: Boolean,
                lastFullBlock: String,
                lastFullHeight: Int)
