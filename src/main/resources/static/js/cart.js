function sendCartItem(variantId) {


  $.ajax({
    type: "POST",
    url: "/cart/increase",
    data: variantId,
    success: function (response) {
      console.log(response);
      // Update relevant elements in the Thymeleaf template
      $('#successMessage').text(response);
      location.reload();
    },
    error: function (xhr, status, error) {
      console.log(error);
      // Handle error case if needed
      var errorMessage = "Failed to increase cart item.";
      sessionStorage.setItem('errorMessage', errorMessage); // Store the error message
      setTimeout(function() {
        location.reload();
      }, 2000); // Delay the page reload by 1 second
    },
    contentType: "text/plain"
  });
}

$(document).ready(function () {
  var errorMessage = sessionStorage.getItem('errorMessage');
  if (errorMessage) {
    $('#errorMessage').text(errorMessage).show(); // Display the stored error message
    sessionStorage.removeItem('errorMessage'); // Remove the error message from storage
  }
});

function sendCartItemDecr(variantId) {
  console.log(variantId);

  // Check the current quantity before the decrease
  var currentQuantity = parseInt($('#sst').val());
  if (currentQuantity === 1) {
    // Display prompt to the user
    var confirmed = window.confirm("Deleting the only item. Are you sure?");
    if (!confirmed) {
      // User canceled deletion, perform any necessary actions
      // ...
      return;
    }
  }

  $.ajax({
    type: "POST",
    url: "/cart/decrease",
    data: variantId,
    success: function(response) {
      console.log(response);
      // Update relevant elements in the Thymeleaf template
      $('#successMessage').text(response);

      location.reload();
    },
    error: function(xhr, status, error) {
      console.log(error);
      // Handle error case if needed
    },
    contentType: "text/plain"
  });
}

function deleteCartItem(variantId, quantity) {
  console.log(variantId);
  console.log(quantity);

  var requestData = {
    variantId: variantId,
    quantity: quantity
  };

  $.ajax({
    type: "POST",
    url: "/cart/delete",
    data: JSON.stringify(requestData),
    success: function (response) {
      console.log(response);
      // Update relevant elements in the Thymeleaf template
      $('#successMessage').text(response);
      location.reload();
    },
    error: function (xhr, status, error) {
      console.log(error);
      // Handle error case if needed
    },
    contentType: "application/json"
  });
}

function proceedToCheckout() {
  var paymentOption = document.getElementById('paymentMethodSelect').value;
  console.log(paymentOption);

  $.ajax({
    type: "POST",
    url: "/cart/payment",
    data: paymentOption,
    success: function (response) {
      console.log(response);
      // Update relevant elements in the Thymeleaf template
      $('#successMessage').text(response);
      window.location.href = "/cart/checkout/"; // Redirect to the checkout page
    },
    error: function (xhr, status, error) {
      console.log(error);
      // Handle error case if needed
    },
    contentType: "text/plain"
  });
}

var selectedAddressId = null; // Variable to store the selected address ID

function selectAddress(button) {
  // Remove the "selected" class from all buttons
  var addressButtons = document.querySelectorAll('.card-footer button');
  addressButtons.forEach(function (btn) {
    btn.classList.remove('selected');
  });

  // Add the "selected" class to the clicked button
  button.classList.add('selected');

  // Get the address ID from the button's data attribute
  selectedAddressId = button.dataset.addressId;

}


function proceedToBuy() {
  console.log("Selected Address ID in proceedToBuy:", selectedAddressId);

  $.ajax({
    type: "POST",
    url: "/cart/buy",
    data: selectedAddressId,
    success: function () {
      console.log("Buy request successful");
      // Redirect to the confirmation page
      window.location.href = "/cart/confirmation";
    },
    error: function (xhr, status, error) {
      console.log("Error occurred:", error);
      // Handle error case if needed
    },
    contentType: "text/plain"
  });
}

function applyCoupon() {

    var couponCode = document.getElementById("couponCodeInput").value;
    $.ajax({
        type: "POST",
        url: "/cart/apply-coupon",
        data: couponCode,
        success: function (response) {
            // Display the coupon availed message and discount percentage if the coupon is valid
            if (response.valid) {
                var successMessage = document.getElementById("successMessage");
                var discountPercentage = response.discountPercentage;
                var message = "Coupon availed! You got " + discountPercentage + "% off.";

                // Check if the coupon is product-specific or category-specific
                if (response.productSpecific) {
                    message = "Coupon availed! You got " + discountPercentage + "% off on this product.";
                } else if (response.categorySpecific) {
                    message = "Coupon availed! You got " + discountPercentage + "% off on this category.";
                }

                successMessage.innerText = message;
            } else {
                var errorMessage = document.getElementById("errorMessage");
                var message = "No coupon available.";

                // Check if the coupon code is not applicable
                if (!response.applicable) {
                    message = "Coupon not applicable.";
                }

                errorMessage.innerText = message;
            }
        },
        error: function (xhr, status, error) {
            console.log(error);
            // Handle error case if needed
        },
        contentType: "text/plain"
    });
}
function closeCoupon() {
    // Clear the coupon code input field
    document.getElementById("couponCodeInput").value = "";

    // Clear any success or error messages
    document.getElementById("successMessage").innerText = "";
    document.getElementById("errorMessage").innerText = "";
}

