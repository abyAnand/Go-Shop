document.addEventListener("DOMContentLoaded", function() {
  // Update the user role value
  function updateUserRole(role) {
    document.getElementById('userRole').value = role;
  }

  // Enable password fields and validate the form on button click
  document.getElementById("passwordEditButton").addEventListener("click", function(event) {
    var passwordInput = document.getElementById("password");
    var confirmPasswordInput = document.getElementById("confirm_password");

    if (passwordInput.readOnly) {
      passwordInput.readOnly = false;
      confirmPasswordInput.readOnly = false;
      event.target.textContent = "Cancel";
    } else {
      passwordInput.readOnly = true;
      confirmPasswordInput.readOnly = true;
      event.target.textContent = "Edit Password";
    }
  });

  // Validate the form on submit
  document.querySelector("form").addEventListener("submit", function(event) {
    var phoneNumberInput = document.getElementById("phonenumber");
    var phoneNumberError = document.getElementById("phoneNumberError");

    // Check if the phone number is a 10-digit number
    var phoneNumber = phoneNumberInput.value.trim();
    var phoneNumberPattern = /^\d{10}$/;

    if (!phoneNumberPattern.test(phoneNumber)) {
      event.preventDefault();
      phoneNumberError.textContent = "Please enter a 10-digit phone number.";
    } else {
      phoneNumberError.textContent = ""; // Clear the error message
    }

    // Check if the password fields are editable
    var passwordInput = document.getElementById("password");
    var confirmPasswordInput = document.getElementById("confirm_password");

    if (!passwordInput.readOnly && !confirmPasswordInput.readOnly) {
      // Check if the password and confirm password fields are empty
      if (passwordInput.value.trim() === "" || confirmPasswordInput.value.trim() === "") {
        event.preventDefault();
        alert("Please fill in both password fields.");
        return;
      }

      // Check if the password and confirm password fields have matching values
      if (passwordInput.value !== confirmPasswordInput.value) {
        event.preventDefault();
        alert("Passwords do not match!");
      }
    }
  });

  // Display the count of numbers entered by the user dynamically
  var phoneNumberInput = document.getElementById("phonenumber");
  var phoneNumberCount = document.getElementById("phoneNumberCount");

  phoneNumberInput.addEventListener("input", function() {
    var phoneNumber = phoneNumberInput.value.trim();
    var numberCount = phoneNumber.replace(/\D/g, "").length;
    phoneNumberCount.textContent = "Number Count: " + numberCount;
  });
});