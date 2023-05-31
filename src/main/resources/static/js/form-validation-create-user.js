document.addEventListener("DOMContentLoaded", function() {
  // Get references to the password and confirm password fields
  var passwordInput = document.getElementById("password");
  var confirmPasswordInput = document.getElementById("confirm_password");

  // Add an event listener to the form on submit
  document.querySelector("form").addEventListener("submit", function(event) {
    // Check if the password and confirm password fields have matching values
    if (passwordInput.value !== confirmPasswordInput.value) {
      // Passwords do not match, prevent form submission and show an error message
      event.preventDefault();
      alert("Passwords do not match!");
    }
  });

  function updateUserRole(role) {
    document.getElementById('userRole').value = role;
  }

  // Display the count of numbers entered by the user dynamically
  var phoneNumberInput = document.getElementById("phonenumber");
  var phoneNumberCount = document.getElementById("phoneNumberCount");
  var phoneNumberError = document.getElementById("phoneNumberError");

  phoneNumberInput.addEventListener("input", function() {
    var phoneNumber = phoneNumberInput.value.trim();
    var numberCount = phoneNumber.replace(/\D/g, "").length;
    phoneNumberCount.textContent = "Number Count: " + numberCount;

    // Check if the phone number is a 10-digit number
    var phoneNumberPattern = /^\d{10}$/;

    if (!phoneNumberPattern.test(phoneNumber)) {
      phoneNumberError.textContent = "Please enter a 10-digit phone number.";
    } else {
      phoneNumberError.textContent = ""; // Clear the error message
    }
  });
});