function sendCartItem(variantId) {
  console.log(variantId);

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

      // Reload the page
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

//function proceedToBuy() {
//  // Check if an address is selected
//  console.log("Selected Address ID in proceedToBuy:", selectedAddressId);
//  if (selectedAddressId) {
//    // Send the selected address ID to the backend
//    $.ajax({
//      type: "POST",
//      url: "/cart/buy",
//      data:selectedAddressId, // Send address ID as JSON payload
//      success: function (response) {
//        console.log(response);
//        // Redirect to the checkout page or perform any other actions
//        window.location.href = "/cart/checkout";
//      },
//      error: function (xhr, status, error) {
//        console.log(error);
//        // Handle error case if needed
//      },
//      contentType: "text/plain"
//    });
//  } else {
//    // Handle the case when no address is selected
//    console.log("Please select an address");
//  }
//}
