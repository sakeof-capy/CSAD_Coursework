const form_open_button = document.getElementById("form_open_button");
const manageGroupsBtn = document.getElementById("manageGroupsBtn");
const productTableBody = document.getElementById("productTableBody");
const form_popup = document.getElementById("form_popup");
const update_form_popup = document.getElementById("update_form_popup");

//Create form:
const nameInputCreate = document.getElementById("nameInputCreate");
const categoryInputCreate = document.getElementById("categoryInputCreate");
const descriptionInputCreate = document.getElementById("descriptionInputCreate");
const stockInputCreate = document.getElementById("stockInputCreate");
const priceInputCreate = document.getElementById("priceInputCreate");
const producerInputCreate = document.getElementById("producerInputCreate");

const submitCreateFormButton = document.getElementById("submitCreateFormButton");
const closeFormButton = document.getElementById("closeFormButton");

//Update form:
const nameInputUpdate = document.getElementById("nameInputUpdate");
const categoryInputUpdate = document.getElementById("categoryInputUpdate");
const descriptionInputUpdate = document.getElementById("descriptionInputUpdate");
const stockInputUpdate = document.getElementById("stockInputUpdate");
const priceInputUpdate = document.getElementById("priceInputUpdate");
const producerInputUpdate = document.getElementById("producerInputUpdate");

const submitUpdateFormButton = document.getElementById("submitUpdateFormButton");
const closeUpdateFormButton = document.getElementById("closeUpdateFormButton");

//Search form:
const search_form_popup = document.getElementById("search_form_popup");

const nameInputSearch = document.getElementById("nameInputSearch");
const categoryInputSearch = document.getElementById("categoryInputSearch");
const descriptionInputSearch = document.getElementById("descriptionInputSearch");
const stockInputSearch = document.getElementById("stockInputSearch");
const priceInputSearch = document.getElementById("priceInputSearch");
const producerInputSearch = document.getElementById("producerInputSearch");

const submitSearchFormButton = document.getElementById("submitSearchFormButton");
const closeSearchFormButton = document.getElementById("closeSearchFormButton");
const search_form_open_button = document.getElementById("search_form_open_button");
const refresh_search_button = document.getElementById("refresh_search_button");

const notification = document.getElementById("notification");

const API = "http://localhost:8000/api";
const notification_time = 1300;

function hide(elem) {
    elem.style.display = "none";
}

function show(elem) {
    elem.style.display = "flex";
}

function notify(text)
{
    notification.innerText = text;
    show(notification);
    const tm = setTimeout(() => {
        hide(notification);
        clearTimeout(tm);
    }, notification_time);
}

/*
    <tr>
      <td>Product 1</td>
      <td>Category 1</td>
      <td class="description">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus tristique lacus a justo iaculis hendrerit. Fusce consequat pulvinar mauris, et auctor odio pharetra eu. Nam tincidunt ex eget pulvinar.</td>
      <td class="stock-low">3</td>
      <td class="price-highlight align-right">$9.99</td>
      <td class="align-right">Producer 1</td>
      <td class="btn-container"><button class="btn btn-update">Update</button></td>
      <td class="btn-container"><button class="btn btn-delete">Delete</button></td>
    </tr>
*/

let productsCounter = 0;

function createTrFromProduct(product) {
    const generalPrice = product.productPrice * product.productStock;
    const tr = document.createElement("tr");
    const trId = "product-id-" + (productsCounter++);
    tr.setAttribute("id", trId);
    const tdName = createTdWithText(product.productName);
    const tdCategory = createTdWithText(product.categoryName);
    const tdDescription = createTdWithText(product.productDescription);
    const tdStock = createTdWithText(product.productStock);
    const tdPrice = createTdWithText('$' + product.productPrice);
    const tdGenPrice = createTdWithText('$' + generalPrice.toFixed(2));
    const tdProducer = createTdWithText(product.productProducer);
    const tdUpdate = createTdWithUpdateButton();
    const tdDelete = createTdWithDeleteButton();
    const tdPlus = createTdWithPlusButton();
    const tdMinus = createTdWithMinusButton();
    [tdPlus, tdMinus,tdName, tdCategory, tdDescription, tdStock, tdPrice, tdGenPrice, tdProducer, tdUpdate, tdDelete]
        .forEach(elem => tr.appendChild(elem));
    return tr;
}

function addProduct(product) {
    const tr = createTrFromProduct(product);
    productTableBody.appendChild(tr);
}

function newProduct(productName0, categoryName0, productDescription0, 
    productStock0, productPrice0, productProducer0) {
        return {
            product_name : productName0,
            category_name : categoryName0,
            product_description : productDescription0,
            in_stock : productStock0,
            price : productPrice0,
            producer : productProducer0,
        };
    }

function readProduct(product) {
    return {
        productName  : product.product_name,
        categoryName : product.category_name,
        productDescription : product.product_description,
        productStock : product.in_stock,
        productPrice : product.price,
        productProducer : product.producer,
    };
}

function createTdWithText(txt) {
    const td = document.createElement("td");
    td.innerText = txt;
    return td;
}

function createTdWithUpdateButton() {
    const td = document.createElement("td");
    td.setAttribute("class", "btn-container");

    const button = document.createElement("button");
    button.setAttribute("class", "btn btn-update");
    button.innerText = "Update";
    button.addEventListener("click", openUpdateForm);
    
    td.appendChild(button);
    return td;
}

function createTdWithPlusButton() {
    const td = document.createElement("td");
    td.setAttribute("class", "btn-container");

    const button = document.createElement("button");
    button.setAttribute("class", "btn btn-update");
    button.innerText = "+";
    // button.addEventListener("click", openUpdateForm);
    
    td.appendChild(button);
    return td;
}

function createTdWithMinusButton() {
    const td = document.createElement("td");
    td.setAttribute("class", "btn-container");

    const button = document.createElement("button");
    button.setAttribute("class", "btn btn-delete");
    button.innerText = "-";
    // button.addEventListener("click", openUpdateForm);
    
    td.appendChild(button);
    return td;
}

function createTdWithDeleteButton() {
    const td = document.createElement("td");
    td.setAttribute("class", "btn-container");

    const button = document.createElement("button");
    button.setAttribute("class", "btn btn-delete");
    button.innerText = "Delete";
    button.addEventListener("click", deleteButtonEventListener);
    
    td.appendChild(button);
    return td;
}

function clearCreateFormFields() {
    nameInputCreate.value = "";
    categoryInputCreate.value = "";
    descriptionInputCreate.value = "";
    stockInputCreate.value = "";
    priceInputCreate.value = "";
    producerInputCreate.value = "";
}

function clearUpdateFormFields() {
    nameInputUpdate.value = "";
    categoryInputUpdate.value = "";
    descriptionInputUpdate.value = "";
    stockInputUpdate.value = "";
    priceInputUpdate.value = "";
    producerInputUpdate.value = "";
}

function clearSearchFormFields() {
    nameInputSearch.value = "";
    categoryInputSearch.value = "";
    descriptionInputSearch.value = "";
    stockInputSearch.value = "";
    priceInputSearch.value = "";
    producerInputSearch.value = "";
}

function openSearchForm() {
    clearSearchFormFields();
    search_form_popup.style.display = "block";
}

function closeSearchForm() {
    search_form_popup.style.display = "none";
    clearSearchFormFields();
}

function openForm() {
    clearCreateFormFields();
    form_popup.style.display = "block";
}

function closeForm() {
    form_popup.style.display = "none";
    clearCreateFormFields();
}

function openUpdateForm(event) {
    const button = event.target;
    const row = button.closest('tr');
    const productName = row.querySelector('td:first-child').textContent;
    nameOfProductBeingUpdated = productName;
    const cells = row.children;
    nameInputUpdate.value = cells[0].textContent;
    categoryInputUpdate.value = cells[1].textContent;
    descriptionInputUpdate.value = cells[2].textContent;
    stockInputUpdate.value = cells[3].textContent;
    priceInputUpdate.value = cells[4].textContent.substring(1);
    producerInputUpdate.value = cells[5].textContent;
    
    update_form_popup.style.display = "block";
}

function closeUpdateForm() {
    update_form_popup.style.display = "none";
    clearUpdateFormFields();
}

async function sendSearchingProduct() {
    const res = await sendRequest("/products", "POST", 
    newProduct(nameInputSearch.value,
        categoryInputSearch.value, descriptionInputSearch.value, stockInputSearch.value,
        priceInputSearch.value, producerInputSearch.value), "Invalid input!");
    return res;
}

async function onSearchFormSubmitted(event) {
    event.preventDefault();
    const res = await sendSearchingProduct();
    if(res) {
        const data = await res.json();
        clearAllProductsViewed();
        data.map(readProduct).forEach(addProduct);
        closeSearchForm();
    }
}

async function sendUpdatingProduct(productName) {
    const res = await sendRequest("/product?product_name=" + productName, "POST", 
    newProduct(nameInputUpdate.value,
        categoryInputUpdate.value, descriptionInputUpdate.value, stockInputUpdate.value,
        priceInputUpdate.value, producerInputUpdate.value), "Invalid input!");
    return res;
}

let nameOfProductBeingUpdated = undefined;

async function onUpdateFormSubmitted(event) {
    event.preventDefault();
    console.log("Product to be updated:", nameOfProductBeingUpdated);
    const res = await sendUpdatingProduct(nameOfProductBeingUpdated);
    await refreshAllProductsData();
    console.log("Updation res:", res);
    if(res) {
        closeUpdateForm();
        clearUpdateFormFields();
    }
}

async function sendCreatingProduct() {
    const res = await sendRequest("/product", "PUT", newProduct(nameInputCreate.value,
        categoryInputCreate.value, descriptionInputCreate.value, stockInputCreate.value,
        priceInputCreate.value, producerInputCreate.value), "Invalid input!");
    console.log(newProduct(nameInputCreate.value,
        categoryInputCreate.value, descriptionInputCreate.value, stockInputCreate.value,
        priceInputCreate.value, producerInputCreate.value));
    return res;
}

async function onFormSubmitted(event) {
    event.preventDefault();
    const res = await sendCreatingProduct();
    await refreshAllProductsData();
    console.log("Creation res:", res);
    if(res) {
        closeForm();
        clearCreateFormFields();
    }
}

function switchToManagingCategories() {
    window.location.href = "../category_list/category_list.html";
}

async function sendRequest(url, queryType, data, errorText)
{
    try {
        console.log("URL:", API + url);
        const res = await fetch(API + url, {
            method: queryType, 
            headers: {"content-type": "application/json"}, 
            body: JSON.stringify(data)
        });
        
        if(res.ok)
        {
            return res;
        }
        else
        {
            notify(errorText ? errorText : "Error occured!");
            return false;
        }

    } catch (err) {
        notify(err.message);
        return false;
    }
}

async function refreshAllProductsData() {
    const res = await sendRequest("/products", "GET");
    if(res) {
        const data = await res.json();
        clearAllProductsViewed();
        data.map(readProduct).forEach(addProduct);
        
    } else {
        console.log("Error request.");
    }
}

async function deleteProductWithName(name) {
    const res = await sendRequest("/product?product_name=" + name, "DELETE");
    if(!res) console.log("Server error deleting the good: " + name);
    refreshAllProductsData();
}

async function deleteButtonEventListener(event) {
    const button = event.target;
    const rowToDelete = button.closest('tr');
    const productName = rowToDelete.querySelector('td:first-child').textContent;
    deleteProductWithName(productName);
}

function clearAllProductsViewed() {
    while (productTableBody.firstChild) {
        productTableBody.removeChild(productTableBody.firstChild);
    }
}

async function main() {
    await refreshAllProductsData();
    form_open_button.addEventListener("click", openForm);
    closeFormButton.addEventListener("click", closeForm);
    closeUpdateFormButton.addEventListener("click", closeUpdateForm);
    submitCreateFormButton.addEventListener("click", onFormSubmitted);
    submitUpdateFormButton.addEventListener("click", onUpdateFormSubmitted);
    manageGroupsBtn.addEventListener("click", switchToManagingCategories);
    search_form_open_button.addEventListener("click", openSearchForm);
    submitSearchFormButton.addEventListener("click", onSearchFormSubmitted);
    closeSearchFormButton.addEventListener("click", closeSearchForm);
    refresh_search_button.addEventListener("click", refreshAllProductsData);
}

main();

