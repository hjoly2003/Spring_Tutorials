// This is a public sample test API key.
// Don’t submit any personally identifiable information in requests made with this key.
// Sign in to see your own test API key embedded in code samples.
// [N]:stripePublicKey]:th-variables - The stripePublicKey is defined in the WebController.java. Available through a Timeleaf variable declaration from checkout.html.
const stripe = Stripe(stripePublicKey);

var feature_request= {
  // [N]:th-variables - Available through a Timeleaf variable declaration from intex.html. It will get sent inside the feature_request object to the WebController via "/create-payment-intent".
  amount: amount,
  email: email,
  featureRequest: featureRequest
}

let elements;

initialize();
checkStatus();

document
  .querySelector("#payment-form")
  .addEventListener("submit", handleSubmit);

// Fetches a payment intent and captures the client secret
async function initialize() {
  const response = await fetch("/create-payment-intent", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(feature_request),
  });
  const { clientSecret } = await response.json();

  const appearance = {
    theme: 'stripe',
  };
  elements = stripe.elements({ appearance, clientSecret });

  const paymentElementOptions = {
    layout: "tabs",
  };

  const paymentElement = elements.create("payment", paymentElementOptions);
  paymentElement.mount("#payment-element");
}

async function handleSubmit(e) {
  e.preventDefault();
  setLoading(true);

  const { error } = await stripe.confirmPayment({
    elements,
    confirmParams: {
      // [me] Make sure to change this to your payment completion page
      return_url: "http://localhost:8080/checkout",
      receipt_email: email,
      // [me]:customizing-stripe-data-struture - Transmit the customer email to the Payment's billing details.  Doing so enables us to see, in the Stripe Dashboard, the user email associated to the payment. 
      payment_method_data: {
        billing_details: {
          email: email
        }
      }
    },
  });

  // This point will only be reached if there is an immediate error when
  // confirming the payment. Otherwise, your customer will be redirected to
  // your `return_url`. For some payment methods like iDEAL, your customer will
  // be redirected to an intermediate site first to authorize the payment, then
  // redirected to the `return_url`.
  if (error.type === "card_error" || error.type === "validation_error") {
    showMessage(error.message);
  } else {
    showMessage("An unexpected error occurred.");
  }

  setLoading(false);
}

// Fetches the payment intent status after payment submission
async function checkStatus() {
  const clientSecret = new URLSearchParams(window.location.search).get(
    "payment_intent_client_secret"
  );

  if (!clientSecret) {
    return;
  }

  const { paymentIntent } = await stripe.retrievePaymentIntent(clientSecret);

  switch (paymentIntent.status) {
    case "succeeded":
      // [me]
      document.querySelector("#payment-outcome").classList.remove("hidden");
      document.querySelector("#submit").classList.add("hidden");
      break;
    case "processing":
      showMessage("Your payment is processing.");
      break;
    case "requires_payment_method":
      showMessage("Your payment was not successful, please try again.");
      break;
    default:
      showMessage("Something went wrong.");
      break;
  }
}

// ------- UI helpers -------

function showMessage(messageText) {
  const messageContainer = document.querySelector("#payment-message");

  messageContainer.classList.remove("hidden");
  messageContainer.textContent = messageText;

  setTimeout(function () {
    messageContainer.classList.add("hidden");
    messageContainer.textContent = "";
  }, 4000);
}

// Show a spinner on payment submission
function setLoading(isLoading) {
  if (isLoading) {
    // Disable the button and show a spinner
    document.querySelector("#submit").disabled = true;
    document.querySelector("#spinner").classList.remove("hidden");
    document.querySelector("#button-text").classList.add("hidden");
  } else {
    document.querySelector("#submit").disabled = false;
    document.querySelector("#spinner").classList.add("hidden");
    document.querySelector("#button-text").classList.remove("hidden");
  }
}