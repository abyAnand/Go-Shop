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

function validateEmail() {
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
}

function validateUsername() {
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
}

function validatePhoneNumber() {
  const phoneNumber = $("#phoneNumber").val();
  const phoneNumberResult = $("#phoneNumberResult");

  if (phoneNumber !== "") {
    $.ajax({
      type: "POST",
      url: "/user/check-phone",
      data: { phoneNumber: phoneNumber },
      success: function (response) {
        if (response === "available") {
          phoneNumberResult.html("<span class='tick'>&#10004;</span>");
          verifyPhoneNumberButton.prop("disabled", false);
        } else {
          phoneNumberResult.html("<span class='cross'>&#10008;</span>");
          verifyPhoneNumberButton.prop("disabled", true);
        }
        validate();
      },
    });
  } else {
    phoneNumberResult.html("");
    verifyPhoneNumberButton.prop("disabled", false);
    validate();
  }
}

function verifyPhoneNumber() {
  // In a real scenario, you would implement the verification logic here
  phoneNumberVerified = true;
  validate();
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
});