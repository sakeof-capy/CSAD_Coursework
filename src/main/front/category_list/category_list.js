function toggleProductList(id) {
    const productList = document.getElementById("product-list-" + id);
    productList.style.display = productList.style.display === "none" ? "table" : "none";
}
  
document.getElementById("manageGroupsButton").addEventListener("click", function() {
    window.location.href = "../product_list/product_list.html";
});