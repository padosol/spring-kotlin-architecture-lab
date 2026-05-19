package com.example.designpattern.promotion

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.math.RoundingMode
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class ApplyPromotionRequest(
	@field:NotBlank
	val customerId: String,
	@field:Positive
	val orderAmount: BigDecimal,
	val couponCode: String?,
)

data class PromotionResponse(
	val customerId: String,
	val couponCode: String?,
	val discountAmount: BigDecimal,
	val payableAmount: BigDecimal,
)

@Service
class PromotionService {
	fun apply(request: ApplyPromotionRequest): PromotionResponse {
		val discountAmount = if (request.couponCode?.uppercase() == WELCOME_COUPON) {
			request.orderAmount.multiply(BigDecimal("0.10")).setScale(0, RoundingMode.DOWN)
		} else {
			BigDecimal.ZERO
		}

		return PromotionResponse(
			customerId = request.customerId,
			couponCode = request.couponCode,
			discountAmount = discountAmount,
			payableAmount = request.orderAmount.subtract(discountAmount),
		)
	}

	private companion object {
		const val WELCOME_COUPON = "WELCOME10"
	}
}

@RestController
@RequestMapping("/api/promotions")
class PromotionController(
	private val promotionService: PromotionService,
) {
	@PostMapping("/apply")
	fun apply(@Valid @RequestBody request: ApplyPromotionRequest): PromotionResponse =
		promotionService.apply(request)
}
