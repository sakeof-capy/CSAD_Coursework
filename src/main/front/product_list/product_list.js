const form_open_button = document.getElementById("form_open_button");
const closeFormButton = document.getElementById("closeFormButton");
const manageGroupsBtn = document.getElementById("manageGroupsBtn");

function openForm() {
    document.getElementById("form_popup").style.display = "block";
}

function closeForm() {
    document.getElementById("form_popup").style.display = "none";
}

function switchToManagingCategories() {
    window.location.href = "../category_list/category_list.html";
}

form_open_button.addEventListener("click", openForm);
closeFormButton.addEventListener("click", closeForm);
manageGroupsBtn.addEventListener("click", switchToManagingCategories);

manageGroupsBtn.addEventListener("click", );