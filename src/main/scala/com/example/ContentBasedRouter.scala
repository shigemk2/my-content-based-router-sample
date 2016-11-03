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

object ContentBasedRouterDriver extends CompletableApp(3) {
}
