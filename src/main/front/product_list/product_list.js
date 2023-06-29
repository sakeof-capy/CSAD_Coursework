document.getElementById("manageGroupsBtn").addEventListener("click", function() {
    window.location.href = "../category_list/category_list.html";
  });

  function openForm() {
    document.getElementById("form_popup").style.display = "block";
  }

  function closeForm() {
    document.getElementById("form_popup").style.display = "none";
  }

  // Event listener to open the form popup on button click
  document.getElementById("form_open_button").addEventListener("click", openForm);