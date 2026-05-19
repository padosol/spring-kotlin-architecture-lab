package com.example.designpattern.order

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

enum class OrderStatus {
	CREATED,
	PAYMENT_PENDING,
	PAID,
	CANCELLED,
	REFUNDED,
}

data class OrderItemRequest(
	@field:NotBlank
	val productId: String,
	@field:Positive
	val quantity: Int,
	@field:Positive
	val unitPrice: BigDecimal,
)

data class CreateOrderRequest(
	@field:NotBlank
	val customerId: String,
	@field:Valid
	@field:Size(min = 1)
	val items: List<OrderItemRequest>,
)

data class OrderItemResponse(
	val productId: String,
	val quantity: Int,
	val unitPrice: BigDecimal,
	val lineAmount: BigDecimal,
)

data class OrderResponse(
	val orderId: String,
	val customerId: String,
	val items: List<OrderItemResponse>,
	val totalAmount: BigDecimal,
	val status: OrderStatus,
)

@Service
class OrderService {
	fun createOrder(request: CreateOrderRequest): OrderResponse {
		val items = request.items.map { item ->
			OrderItemResponse(
				productId = item.productId,
				quantity = item.quantity,
				unitPrice = item.unitPrice,
				lineAmount = item.lineAmount(),
			)
		}

		return OrderResponse(
			orderId = "order-${UUID.randomUUID()}",
			customerId = request.customerId,
			items = items,
			totalAmount = items.fold(BigDecimal.ZERO) { total, item -> total.add(item.lineAmount) },
			status = OrderStatus.PAYMENT_PENDING,
		)
	}

	private fun OrderItemRequest.lineAmount(): BigDecimal =
		unitPrice.multiply(BigDecimal.valueOf(quantity.toLong()))
}

@RestController
@RequestMapping("/api/orders")
class OrderController(
	private val orderService: OrderService,
) {
	@PostMapping
	fun createOrder(@Valid @RequestBody request: CreateOrderRequest): OrderResponse =
		orderService.createOrder(request)
}
