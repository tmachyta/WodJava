package app.training.dto.stripe;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConfirmPaymentRequestDto {
    private String paymentIntentId;
}
