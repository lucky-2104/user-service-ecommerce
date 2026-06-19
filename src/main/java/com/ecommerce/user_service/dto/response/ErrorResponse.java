package com.ecommerce.user_service.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(
		String message,
		int status,
		LocalDateTime timestamp

		) {

}
