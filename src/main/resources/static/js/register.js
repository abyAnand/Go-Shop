let phoneNumberVerified = false;

function validateRegex(text) {
  // Letters, numbers, underscores, and periods.
  const usernameRegex = /^[a-z0-9_.]+$/;
  return usernameRegex.test(text);
}

function validate() {
  const firstName = $("#firstName").val();
  const lastName = $("#lastName").val();
  const username = $("#username").val();
  const phoneNumber = $("#phoneNumber").val();
  const verifyPhoneNumberButton = $("#verifyPhoneNumberButton");
  const otpInput = $("#otp");
  const registerButton = $("#registerButton");
  const errorMsg = $("#errorMsg");

  if (
    !firstName ||
    !lastName ||
    !username ||
    !phoneNumber ||
    !verifyPhoneNumberButton ||
    !otpInput ||
    !registerButton ||
    !errorMsg
  ) {
    return;
  }

  if (firstName === "" || lastName === "" || username === "" || phoneNumber === "") {
    console.log("Validation Failed");
    verifyPhoneNumberButton.prop("disabled", true);
    registerButton.prop("disabled", true);
    errorMsg.html("Please fill in all required fields.").show();
  } else if (!validateRegex(username)) {
    console.log("Validation Failed");
    verifyPhoneNumberButton.prop("disabled", true);
    registerButton.prop("disabled", true);
    errorMsg.html("Invalid username format.").show();
  } else if (!phoneNumberVerified) {
    console.log("Validation Failed");
    registerButton.prop("disabled", true);
    errorMsg.html("Please verify your phone number.").show();
  } else if (otpInput.val() === "") {
    console.log("Validation Failed");
    registerButton.prop("disabled", true);
    errorMsg.html("Please enter the OTP.").show();
  } else {
    console.log("Validation Passed");
    registerButton.prop("disabled", false);
    errorMsg.html("").hide();
  }
}

let emailTimer; // Timer for email validation
let usernameTimer; // Timer for username validation

function validateEmail() {
  clearTimeout(emailTimer); // Clear the previous timer

  emailTimer = setTimeout(function () {
    const email = $("#email").val();
    const emailResult = $("#emailResult");

    if (email !== "") {
      $.ajax({
        type: "POST",
        url: "/user/check-email",
        data: { email: email },
        success: function (response) {
          if (response === "available") {
            emailResult.html("<span class='tick'>&#10004;</span>");
          } else {
            emailResult.html("<span class='cross'>&#10008;</span>");
          }
          validate();
        },
      });
    } else {
      emailResult.html("");
      validate();
    }
  }, 2000); // Set a delay of 2 seconds (2000 milliseconds)
}

function validateUsername() {
  clearTimeout(usernameTimer); // Clear the previous timer

  usernameTimer = setTimeout(function () {
    const username = $("#username").val();
    const usernameResult = $("#usernameResult");

    if (username !== "") {
      $.ajax({
        type: "POST",
        url: "/user/check-username",
        data: { username: username },
        success: function (response) {
          if (response === "available") {
            usernameResult.html("<span class='tick'>&#10004;</span>");
          } else {
            usernameResult.html("<span class='cross'>&#10008;</span>");
          }
          validate();
        },
      });
    } else {
      usernameResult.html("");
      validate();
    }
  }, 2000); // Set a delay of 2 seconds (2000 milliseconds)
}

function validatePhoneNumber() {
  const phoneNumber = $("#phoneNumber").val();
  const phoneNumberResult = $("#phoneNumberResult");
  const phoneExistError = $("#phoneExistError");

  if (phoneNumber.length === 10) {
    $.ajax({
      type: "POST",
      url: "/user/check-phone",
      data: { phoneNumber: phoneNumber },
      success: function (response) {
        if (response === "available") {
          phoneNumberResult.html("<span class='tick'>&#10004;</span>");
          phoneExistError.html("");
          enableGetOTPButton();
        } else {
          phoneNumberResult.html("<span class='cross'>&#10008;</span>");
          phoneExistError.html("Phone number already exists").show();
          disableGetOTPButton();
        }
      },
    });
  } else {
    phoneNumberResult.html("<span class='cross'>&#10008;</span>");
    phoneExistError.html("Please enter a 10-digit phone number").show();
    disableGetOTPButton();
  }
}

function sendOTP(phoneNumber) {
  $.ajax({
    type: "POST",
    url: "/send-otp",
    data: { phoneNumber: phoneNumber },
    success: function (response) {
      enableVerifyOTPButton();
    },
  });
}

function enableGetOTPButton() {
  const getOTPButton = $("#getOTPButton");
  getOTPButton.prop("disabled", false);
}

function disableGetOTPButton() {
  const getOTPButton = $("#getOTPButton");
  getOTPButton.prop("disabled", true);
}

function enableVerifyOTPButton() {
  const verifyOTPButton = $("#verifyOTPButton");
  verifyOTPButton.prop("disabled", false);
}

function disableVerifyOTPButton() {
  const verifyOTPButton = $("#verifyOTPButton");
  verifyOTPButton.prop("disabled", true);
}

function verifyPhoneNumber() {
  // In a real scenario, you would implement the verification logic here
  phoneNumberVerified = true;
  validate();
}

function verifyOTP() {
  const otpInput = $("#otp").val();
  // Add logic to verify the OTP with the server-side API

  // If the OTP is verified, enable the register button
  enableRegisterButton();
}

function enableRegisterButton() {
  const registerButton = $("#registerButton");
  registerButton.prop("disabled", false);
}

$(document).ready(function () {
  $("#firstName, #lastName, #username, #phoneNumber, #otp").on("input", function () {
    validate();
  });

  $("#email").on("input", function () {
    validateEmail();
  });

  $("#username").on("input", function () {
    validateUsername();
  });

  $("#phoneNumber").on("input", function () {
    validatePhoneNumber();
  });

  $("#verifyPhoneNumberButton").on("click", function () {
    verifyPhoneNumber();
  });

  $("#getOTPButton").on("click", function () {
    const phoneNumber = $("#phoneNumber").val();

    if (phoneNumber.length === 10) {
      sendOTP(phoneNumber);
    }
  });

  $("#verifyOTPButton").on("click", function () {
    verifyOTP();
  });
});
