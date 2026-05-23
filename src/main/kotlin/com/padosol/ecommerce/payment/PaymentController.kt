package com.padosol.ecommerce.payment

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

enum class PaymentMethod {
	CARD,
	BANK_TRANSFER,
}

enum class PaymentStatus {
	APPROVED,
}

data class PaymentRequest(
	@field:NotBlank
	val orderId: String,
	@field:Positive
	val amount: BigDecimal,
	val method: PaymentMethod,
)

data class PaymentResponse(
	val orderId: String,
	val transactionId: String,
	val method: PaymentMethod,
	val status: PaymentStatus,
	val message: String,
)

interface PaymentProcessor {
	fun supports(method: PaymentMethod): Boolean
	fun pay(request: PaymentRequest): PaymentResponse
}

@Service
class CardPaymentProcessor : PaymentProcessor {
	override fun supports(method: PaymentMethod): Boolean = method == PaymentMethod.CARD

	override fun pay(request: PaymentRequest): PaymentResponse =
		PaymentResponse(
			orderId = request.orderId,
			transactionId = "card-${UUID.randomUUID()}",
			method = request.method,
			status = PaymentStatus.APPROVED,
			message = "Card payment approved",
		)
}

@Service
class BankTransferPaymentProcessor : PaymentProcessor {
	override fun supports(method: PaymentMethod): Boolean = method == PaymentMethod.BANK_TRANSFER

	override fun pay(request: PaymentRequest): PaymentResponse =
		PaymentResponse(
			orderId = request.orderId,
			transactionId = "bank-${UUID.randomUUID()}",
			method = request.method,
			status = PaymentStatus.APPROVED,
			message = "Bank transfer payment approved",
		)
}

@Service
class PaymentService(
	private val processors: List<PaymentProcessor>,
) {
	fun pay(request: PaymentRequest): PaymentResponse {
		val processor = processors.firstOrNull { it.supports(request.method) }
			?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported payment method: ${request.method}")

		return processor.pay(request)
	}
}

@RestController
@RequestMapping("/api/payments")
class PaymentController(
	private val paymentService: PaymentService,
) {
	@PostMapping
	fun pay(@Valid @RequestBody request: PaymentRequest): PaymentResponse =
		paymentService.pay(request)
}
