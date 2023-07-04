var letRating;

function getRating(event) {

  switch (event.target.value) {
    case "5":
      letRating = 1;
      break;
    case "4":
      letRating = 2;
      break;
    case "3":
      letRating = 3;
      break;
    case "2":
      letRating = 4;
      break;
    case "1":
      letRating = 5;
      break;
    default:
      letRating = 0;
      break;
  }


}

function getVal(event) {
  event.preventDefault(); // Prevent the default form submission

  const review = document.querySelector('#review').value;
  const productId = document.getElementById('productId').value;



  const formData = {
    rating: letRating,
    review: review,
    productId: productId
  };

  console.log(formData);

  // Send the AJAX request
  sendReviewRes(formData);
}

document.addEventListener('DOMContentLoaded', function() {
  const form = document.querySelector('#reviewRes');

  form.addEventListener('submit', getVal);
});

function sendReviewRes(formData) {

console.log(formData)

if (!formData.productId) {
    alert("Please select a product");
    return;
  }

  if (!formData.rating) {
    alert("Please select a rating ");
    return;
  }

  if (!formData.review) {
      alert("Please select a review ");
      return;
    }


  // Perform the AJAX request
  $.ajax({
    type: "POST",
    url: "/cart/product/review",
    data: JSON.stringify(formData),
    success: function(response) {
      console.log(response);
      // Update relevant elements in the Thymeleaf template
      $('#successMessage').text(response);
      location.reload();
    },
    error: function(xhr, status, error) {
      console.log(xhr.responseText);
      // Handle the error response if needed
    },
    contentType: "application/json"
  });
};

