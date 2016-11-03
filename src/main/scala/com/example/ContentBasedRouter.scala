package com.example

import akka.actor._

case class Order(id: String, orderType: String, orderItems: Map[String, OrderItem]) {
  val grandTotal: Double = orderItems.values.map(orderItem => orderItem.price).sum

  override def toString: String = {
    s"Order($id, $orderType, $orderItems, Totaling: $grandTotal)"
  }
}

case class OrderItem(id: String, itemType: String, description: String, price: Double) {
  override def toString: String = {
    s"OrderItem($id, $itemType, '$description', $price)"
  }
}

case class OrderPlaced(order: Order)

object ContentBasedRouterDriver extends CompletableApp(3) {
}

class OrderRouter extends Actor {
  val inventorySystemA = context.actorOf(Props[InventorySystemA], "inventorySystemA")
  val inventorySystemX = context.actorOf(Props[InventorySystemX], "inventorySystemX")

  def receive = {
    case orderPlaced: OrderPlaced =>
      orderPlaced.order.orderType match {
        case "TypeABC" =>
          println(s"OrderRouter: routing $orderPlaced")
          inventorySystemA ! orderPlaced
        case "TypeXYZ" =>
          println(s"OrderRouter: routing $orderPlaced")
          inventorySystemX ! orderPlaced
      }

      ContentBasedRouterDriver.completedStep()
    case _ =>
      println("OrderRouter: received unexpected message")
  }
}

class InventorySystemA extends Actor {
  def receive = {
    case OrderPlaced(order) =>
      println(s"InventorySystemA: handling $order")
      ContentBasedRouterDriver.completedStep()
    case _ =>
      println("InventorySystemA: received unexpected message")
  }
}

class InventorySystemX extends Actor {
  def receive = {
    case OrderPlaced(order) =>
      println(s"InventorySystemX: handling $order")
      ContentBasedRouterDriver.completedStep()
    case _ =>
      println("InventorySystemX: received unexpected message")
  }
}

