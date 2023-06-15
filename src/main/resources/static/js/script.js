function selectVariant(element) {
    const selectedVariant = element.innerText;
    const variantDescription = element.getAttribute("data-description");
    const variantPrice = element.getAttribute("data-price");
    const variantStock = element.getAttribute("data-stock");
    const selectedVariantId = element.getAttribute("data-id");


    document.getElementById("selectedVariant").innerText = selectedVariant;
    document.getElementById("productDescription").innerText = variantDescription;
    document.getElementById("productPrice").innerText = "$" + variantPrice;
    document.getElementById("selectedVariantId").value = selectedVariantId;

    const availabilityElement = document.getElementById("productAvailability");
    if (variantStock == 0) {
        availabilityElement.innerText = "Not available";
    } else if (variantStock < 5) {
        availabilityElement.innerText = "Less than 5 available";
    } else {
        availabilityElement.innerText = "In Stock";
    }
}

// Set the first variant as the default selected variant
window.onload = function () {
    const firstVariant = document.querySelector(".dropdown-item");
    if (firstVariant) {
        selectVariant(firstVariant);
    }
};

function addToCart() {
    // Retrieve the variant ID from the "selectedVariantId" input field
    var selectedVariantId = document.getElementById("selectedVariantId").value;

    if (selectedVariantId) {
        // Show the Lottie animation
        var lottieContainer = document.getElementById("lottieContainer");
        lottieContainer.style.display = "block";

        // Send the variant ID to the server through an AJAX request
        $.ajax({
            type: "POST",
            url: "/cart/add",
            data: { variant: selectedVariantId },
            headers: {
                // Add CSRF token if required by the server
                // "X-CSRF-Token": "your-csrf-token",
            },
            success: function(response) {
                // Handle the response accordingly
                console.log(response); // This will log the "added" message in the browser console

                // Update the Thymeleaf page with the "added" message

                // Hide the Lottie animation after 2 seconds
                setTimeout(function() {
                    lottieContainer.style.display = "none";
                }, 1000);
            },
            error: function(xhr, status, error) {
                console.error(error);
                // Handle the error if needed
            }
        });
    } else {
        console.error("No variant selected");
    }
}