const form_open_button = document.getElementById("form_open_button");
const closeFormButton = document.getElementById("closeFormButton");
const manageGroupsBtn = document.getElementById("manageGroupsBtn");
const productTableBody = document.getElementById("productTableBody");

const API = "http://localhost:8000/api";

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
            productName : productName0,
            categoryName : categoryName0,
            productDescription : productDescription0,
            productStock : productStock0,
            productPrice : productPrice0,
            productProducer : productProducer0,
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
    
    td.appendChild(button);
    return td;
}

function openForm() {
    document.getElementById("form_popup").style.display = "block";
}

function closeForm() {
    document.getElementById("form_popup").style.display = "none";
}

function switchToManagingCategories() {
    window.location.href = "../category_list/category_list.html";
}

async function sendRequest(url, queryType, data)
{
    try {
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
            //notify(notificationErrorText, NotificationTypes["error"]);
            return false;
        }

    } catch (err) {
        console.log(err);
        //notify("Server is dead.", NotificationTypes["error"]);
        return false;
    }
}

async function refreshAllProductsData() {
    const res = await sendRequest("/products", "GET");
    if(res) {
        const data = await res.json();
        data.map(readProduct).forEach(addProduct);
        
    } else {
        console.log("Error request.");
    }
}

form_open_button.addEventListener("click", openForm);
closeFormButton.addEventListener("click", closeForm);
manageGroupsBtn.addEventListener("click", switchToManagingCategories);

async function main() {
    await refreshAllProductsData();
}

main();

// addProduct(newProduct("GogiProduct", "NewSanzharyDevs", "Description", 132, 32.4, "The Gogi"));
// addProduct(newProduct("GogiProduct", "NewSanzharyDevs", "Description", 132, 32.4, "The Gogi"));
// addProduct(newProduct("GogiProduct", "NewSanzharyDevs", "Description", 132, 32.4, "The Gogi"));
// addProduct(newProduct("GogiProduct", "NewSanzharyDevs", "Description", 132, 32.4, "The Gogi"));
// addProduct(newProduct("GogiProduct", "NewSanzharyDevs", "Description", 132, 32.4, "The Gogi"));
// addProduct(newProduct("GogiProduct", "NewSanzharyDevs", "Description", 132, 32.4, "The Gogi"));
// addProduct(newProduct("GogiProduct", "NewSanzharyDevs", "Description", 132, 32.4, "The Gogi"));
// addProduct(newProduct("GogiProduct", "NewSanzharyDevs", "Description", 132, 32.4, "The Gogi"));