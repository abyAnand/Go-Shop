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
  },
  contentType: "text/plain"
});
}

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

function deleteCartItem(variantId) {
  console.log(variantId);

$.ajax({
  type: "POST",
  url: "/cart/delete",
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
  },
  contentType: "text/plain"
});
}