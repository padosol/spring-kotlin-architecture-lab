package com.example.designpattern.customer

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

enum class CustomerStatus {
	ACTIVE,
	LOCKED,
	DISABLED,
}

data class CustomerResponse(
	val customerId: String,
	val email: String,
	val status: CustomerStatus,
)

@Service
class CustomerService {
	private val customers = listOf(
		CustomerResponse(
			customerId = "customer-1",
			email = "user@example.com",
			status = CustomerStatus.ACTIVE,
		),
		CustomerResponse(
			customerId = "customer-locked",
			email = "locked@example.com",
			status = CustomerStatus.LOCKED,
		),
	)

	fun getCustomer(customerId: String): CustomerResponse =
		customers.firstOrNull { it.customerId == customerId }
			?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found: $customerId")
}

@RestController
@RequestMapping("/api/customers")
class CustomerController(
	private val customerService: CustomerService,
) {
	@GetMapping("/{customerId}")
	fun getCustomer(@PathVariable customerId: String): CustomerResponse =
		customerService.getCustomer(customerId)
}
