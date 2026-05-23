package com.padosol.ecommerce.shipment

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

enum class ShipmentStatus {
	READY,
	SHIPPING,
	DELIVERED,
	CANCELLED,
}

data class CreateShipmentRequest(
	@field:NotBlank
	val orderId: String,
	@field:NotBlank
	val recipientName: String,
	@field:NotBlank
	val address: String,
)

data class ShipmentResponse(
	val shipmentId: String,
	val orderId: String,
	val trackingNumber: String,
	val status: ShipmentStatus,
)

@Service
class ShipmentService {
	fun createShipment(request: CreateShipmentRequest): ShipmentResponse =
		ShipmentResponse(
			shipmentId = "shipment-${UUID.randomUUID()}",
			orderId = request.orderId,
			trackingNumber = "tracking-${UUID.randomUUID()}",
			status = ShipmentStatus.READY,
		)
}

@RestController
@RequestMapping("/api/shipments")
class ShipmentController(
	private val shipmentService: ShipmentService,
) {
	@PostMapping
	fun createShipment(@Valid @RequestBody request: CreateShipmentRequest): ShipmentResponse =
		shipmentService.createShipment(request)
}
