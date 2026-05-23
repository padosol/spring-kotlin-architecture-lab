package com.padosol.ecommerce.catalog

import java.math.BigDecimal
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

enum class ProductStatus {
	ON_SALE,
	SOLD_OUT,
	DISCONTINUED,
}

data class ProductResponse(
	val productId: String,
	val name: String,
	val price: BigDecimal,
	val status: ProductStatus,
)

@Service
class CatalogService {
	private val products = listOf(
		ProductResponse(
			productId = "product-1",
			name = "Kotlin Architecture Book",
			price = BigDecimal("12000"),
			status = ProductStatus.ON_SALE,
		),
		ProductResponse(
			productId = "product-2",
			name = "Spring Boot Practice Kit",
			price = BigDecimal("18000"),
			status = ProductStatus.ON_SALE,
		),
		ProductResponse(
			productId = "product-sold-out",
			name = "Limited Edition Keyboard",
			price = BigDecimal("99000"),
			status = ProductStatus.SOLD_OUT,
		),
	)

	fun listProducts(): List<ProductResponse> = products

	fun getProduct(productId: String): ProductResponse =
		products.firstOrNull { it.productId == productId }
			?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found: $productId")
}

@RestController
@RequestMapping("/api/catalog/products")
class CatalogController(
	private val catalogService: CatalogService,
) {
	@GetMapping
	fun listProducts(): List<ProductResponse> =
		catalogService.listProducts()

	@GetMapping("/{productId}")
	fun getProduct(@PathVariable productId: String): ProductResponse =
		catalogService.getProduct(productId)
}
