document.addEventListener('DOMContentLoaded', function() {
    const associatedItemSelect = document.getElementById('associatedItem');
    const productSection = document.getElementById('productSection');
    const categorySection = document.getElementById('categorySection');

    associatedItemSelect.addEventListener('change', function() {
        if (associatedItemSelect.value === 'product') {
            productSection.style.display = 'block';
            categorySection.style.display = 'none';
        } else if (associatedItemSelect.value === 'category') {
            productSection.style.display = 'none';
            categorySection.style.display = 'block';
        }
    });
});
    const associatedItemSelect = document.getElementById("associatedItem");
    const productSection = document.getElementById("productSection");
    const categorySection = document.getElementById("categorySection");

    associatedItemSelect.addEventListener("change", function () {
        if (associatedItemSelect.value === "GENERAL") {
            productSection.style.display = "none";
            categorySection.style.display = "none";
        } else if (associatedItemSelect.value === "PRODUCT") {
            productSection.style.display = "block";
            categorySection.style.display = "none";
        } else if (associatedItemSelect.value === "CATEGORY") {
            productSection.style.display = "none";
            categorySection.style.display = "block";
        }
    });

    // Initial state
    if (associatedItemSelect.value === "general") {
        productSection.style.display = "none";
        categorySection.style.display = "none";
    }