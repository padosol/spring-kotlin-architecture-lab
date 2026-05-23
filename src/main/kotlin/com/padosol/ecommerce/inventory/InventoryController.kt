package com.padosol.ecommerce.inventory

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

enum class InventoryReservationStatus {
	RESERVED,
	REJECTED,
	RELEASED,
}

data class ReserveInventoryRequest(
	@field:NotBlank
	val productId: String,
	@field:Positive
	val quantity: Int,
)

data class InventoryReservationResponse(
	val reservationId: String,
	val productId: String,
	val quantity: Int,
	val status: InventoryReservationStatus,
)

@Service
class InventoryService {
	fun reserve(request: ReserveInventoryRequest): InventoryReservationResponse =
		InventoryReservationResponse(
			reservationId = "inventory-${UUID.randomUUID()}",
			productId = request.productId,
			quantity = request.quantity,
			status = InventoryReservationStatus.RESERVED,
		)
}

@RestController
@RequestMapping("/api/inventory")
class InventoryController(
	private val inventoryService: InventoryService,
) {
	@PostMapping("/reservations")
	fun reserve(@Valid @RequestBody request: ReserveInventoryRequest): InventoryReservationResponse =
		inventoryService.reserve(request)
}
