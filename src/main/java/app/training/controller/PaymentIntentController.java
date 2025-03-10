package app.training.controller;

import app.training.dto.stripe.ResponseDto;
import app.training.service.user.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "https://wod-warrior.online")
@RequestMapping("/auth")
public class PaymentIntentController {
    private static final Long AMOUNT = 1000L;
    private static final String CURRENCY = "USD";
    private final UserService userService;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping("/create-payment-intent")
    public ResponseDto createPaymentIntent()
            throws StripeException {
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(AMOUNT)
                        .setCurrency(CURRENCY)
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams
                                        .AutomaticPaymentMethods
                                        .builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .build();

        PaymentIntent intent = PaymentIntent.create(params);

        return new ResponseDto(intent.getId(),
                intent.getClientSecret());
    }

    @PostMapping("/webhook")
    public void handleStripeEvent(@RequestBody String payload,
                                    @RequestHeader("Stripe-Signature") String sigHeader) {
        String endpointSecret = this.endpointSecret;

        Event event;

        try {
            event = Webhook.constructEvent(
                    payload, sigHeader, endpointSecret
            );
        } catch (Exception e) {
            throw new RuntimeException("Invalid Signature");
        }

        processStripeEvent(event);
    }

    private void processStripeEvent(Event event) {
        String eventType = event.getType();
        if ("payment_intent.succeeded".equals(eventType)) {
            handlePaymentSucceededEvent(event);
        } else if ("payment_intent.payment_failed".equals(eventType)) {
            handlePaymentFailedEvent(event);
        }
    }

    private void handlePaymentSucceededEvent(Event event) {
        PaymentIntent paymentIntent = extractPaymentIntent(event);
        if (paymentIntent != null) {
            String email = paymentIntent.getReceiptEmail();
            if (email != null) {
                userService.subscribeUser(email);
            }
        }
    }

    private void handlePaymentFailedEvent(Event event) {
        PaymentIntent paymentIntent = extractPaymentIntent(event);
        if (paymentIntent != null) {
            String email = paymentIntent.getReceiptEmail();
            if (email != null) {
                userService.unSubscribeUser(email);
            }
        }
    }

    private PaymentIntent extractPaymentIntent(Event event) {
        return (PaymentIntent) event.getDataObjectDeserializer()
                .getObject()
                .orElse(null);
    }
}
