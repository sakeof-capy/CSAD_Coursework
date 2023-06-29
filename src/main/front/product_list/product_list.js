const form_open_button = document.getElementById("form_open_button");
const submitCreateFormButton = document.getElementById("submitCreateFormButton");
const closeFormButton = document.getElementById("closeFormButton");
const manageGroupsBtn = document.getElementById("manageGroupsBtn");
const productTableBody = document.getElementById("productTableBody");

//Create form:
const nameInputCreate = document.getElementById("nameInputCreate");
const categoryInputCreate = document.getElementById("categoryInputCreate");
const descriptionInputCreate = document.getElementById("descriptionInputCreate");
const stockInputCreate = document.getElementById("stockInputCreate");
const priceInputCreate = document.getElementById("priceInputCreate");
const producerInputCreate = document.getElementById("producerInputCreate");

const API = "http://localhost:8000/api";

function hide(elem) {
    elem.style.display = "none";
}

function show(elem) {
    elem.style.display = "flex";
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
    const tr = document.createElement("tr");
    const trId = "product-id-" + (productsCounter++);
    tr.setAttribute("id", trId);
    const tdName = createTdWithText(product.productName);
    const tdCategory = createTdWithText(product.categoryName);
    const tdDescription = createTdWithText(product.productDescription);
    const tdStock = createTdWithText(product.productStock);
    const tdPrice = createTdWithText('$' + product.productPrice);
    const tdProducer = createTdWithText(product.productProducer);
    const tdUpdate = createTdWithUpdateButton();
    const tdDelete = createTdWithDeleteButton();
    [tdName, tdCategory, tdDescription, tdStock, tdPrice, tdProducer, tdUpdate, tdDelete]
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
    nameInputCreate.value = ""
    categoryInputCreate.value = ""
    descriptionInputCreate.value = ""
    stockInputCreate.value = ""
    priceInputCreate.value = ""
    producerInputCreate.value = ""
}

function openForm() {
    document.getElementById("form_popup").style.display = "block";
}

function closeForm() {
    document.getElementById("form_popup").style.display = "none";
}

async function sendCreatingProduct() {
    // const res = await fetch("http://localhost:8000/api/product", {
    //     method: "PUT", 
    //     headers: {"content-type": "application/json"}, 
    //     //body: JSON.stringify(data)
    // });
    // return res;
    const res = await sendRequest("/product", "PUT", newProduct(nameInputCreate.value,
        categoryInputCreate.value, descriptionInputCreate.value, stockInputCreate.value,
        priceInputCreate.value, producerInputCreate.value));
    console.log(newProduct(nameInputCreate.value,
        categoryInputCreate.value, descriptionInputCreate.value, stockInputCreate.value,
        priceInputCreate.value, producerInputCreate.value));
    return res;
}

async function onFormSubmitted(event) {
    event.preventDefault();
    const res = await sendCreatingProduct();
    // const res = await fetch("http://localhost:8000/api/product", {
    //     method: "PUT", 
    //         headers: {"content-type": "application/json"}, 
    //         body: JSON.stringify(
    //             {
    //                 product_name : "SomeName",
    //                 category_name : "Fruits",
    //                 product_description : "description",
    //                 in_stock : "100",
    //                 price : "123.2",
    //                 producer : "producer"
    //             }
    //         )
    // })
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

async function sendRequest(url, queryType, data)
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
            console.log("Not ok!", res);
            //notify(notificationErrorText, NotificationTypes["error"]);
            return false;
        }

    } catch (err) {
        console.log("Error", err);
        //notify("Server is dead.", NotificationTypes["error"]);
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
    submitCreateFormButton.addEventListener("click", onFormSubmitted)
    manageGroupsBtn.addEventListener("click", switchToManagingCategories);
}

main();
