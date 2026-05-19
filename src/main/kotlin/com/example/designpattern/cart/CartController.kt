package com.example.designpattern.cart

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

data class CartItemRequest(
	@field:NotBlank
	val productId: String,
	@field:Positive
	val quantity: Int,
	@field:Positive
	val unitPrice: BigDecimal,
)

data class CartPreviewRequest(
	@field:NotBlank
	val customerId: String,
	@field:Valid
	@field:Size(min = 1)
	val items: List<CartItemRequest>,
)

data class CartItemResponse(
	val productId: String,
	val quantity: Int,
	val unitPrice: BigDecimal,
	val lineAmount: BigDecimal,
)

data class CartPreviewResponse(
	val cartId: String,
	val customerId: String,
	val items: List<CartItemResponse>,
	val totalAmount: BigDecimal,
)

@Service
class CartService {
	fun preview(request: CartPreviewRequest): CartPreviewResponse {
		val items = request.items.map { item ->
			CartItemResponse(
				productId = item.productId,
				quantity = item.quantity,
				unitPrice = item.unitPrice,
				lineAmount = item.lineAmount(),
			)
		}

		return CartPreviewResponse(
			cartId = "cart-${UUID.randomUUID()}",
			customerId = request.customerId,
			items = items,
			totalAmount = items.fold(BigDecimal.ZERO) { total, item -> total.add(item.lineAmount) },
		)
	}

	private fun CartItemRequest.lineAmount(): BigDecimal =
		unitPrice.multiply(BigDecimal.valueOf(quantity.toLong()))
}

@RestController
@RequestMapping("/api/carts")
class CartController(
	private val cartService: CartService,
) {
	@PostMapping("/preview")
	fun preview(@Valid @RequestBody request: CartPreviewRequest): CartPreviewResponse =
		cartService.preview(request)
}
