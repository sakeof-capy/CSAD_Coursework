/*
<div class="category" onclick="toggleProductList(1)">
      <div class="category-name">Category 1</div>
</div>
*/

/*
<div id="product-list-1" class="product-list" style="display: none;">
      <div class="category-name">Category 1</div>
      <table class="product-table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Price</th>
            <th>Stock</th>
            <th></th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>Product 1</td>
            <td>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</td>
            <td>$19.99</td>
            <td>10</td>
            <td class="btn-container"><button class="btn btn-update">Update</button></td>
            <td class="btn-container"><button class="btn btn-delete">Delete</button></td>
          </tr>
          <tr>
            <td>Product 2</td>
            <td>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</td>
            <td>$29.99</td>
            <td>5</td>
            <td class="btn-container"><button class="btn btn-update">Update</button></td>
            <td class="btn-container"><button class="btn btn-delete">Delete</button></td>
          </tr>
        </tbody>
      </table>
    </div>
*/

const manageGroupsButton = document.getElementById("manageGroupsButton");
const categoryContainer = document.getElementById("categoryContainer");
const productListContainer = document.getElementById("productListContainer");

const API = "http://localhost:8000/api";
let productListsCounter = 0;

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

function readCategory(cat) {
    return {
        categoryName : cat.category_name,
        categoryDescription : cat.category_description
    };
}

function createTh(innerText) {
    const thName = document.createElement("th");
    thName.innerText = innerText;
    return thName;
}

function createTdWithText(txt) {
    const td = document.createElement("td");
    td.innerText = txt;
    return td;
}

function createUpdateButton() {
    const button = document.createElement("button");
    button.setAttribute("class", "btn btn-update");
    button.innerText = "Update";
    return button;
}

function createDeleteButton() {
    const button = document.createElement("button");
    button.setAttribute("class", "btn btn-delete");
    button.innerText = "Delete";
    button.addEventListener("click", onCategoryDelete);
    return button;
}

function createTrFromProduct(product) {
    const tr = document.createElement("tr");
    const tdName = createTdWithText(product.productName);
    const tdDescription = createTdWithText(product.productDescription);
    const tdPrice = createTdWithText('$' + product.productPrice);
    const tdStock = createTdWithText(product.productStock);
    [tdName, tdDescription, tdPrice, tdStock]
        .forEach(elem => tr.appendChild(elem));
    return tr;
}

function createProductList(id, categoryName, products, categoryDescription) {
    const div = document.createElement("div");
    div.setAttribute("id", id);
    div.setAttribute("class", "product-list");
    div.setAttribute("style", "display: none;");

    const categoryNameDiv = document.createElement("div");
    categoryNameDiv.setAttribute("class", "category-name");
    categoryNameDiv.innerText = categoryName;

    const categoryDescriptionDiv = document.createElement("div");
    categoryDescriptionDiv.setAttribute("class", "category-description");
    categoryDescriptionDiv.innerText = categoryDescription;

    const productTable = document.createElement("table");
    productTable.setAttribute("class", "product-table");

    //thead
    const tableHead = document.createElement("thead");
    const headTr = document.createElement("tr");

    const thName = createTh("Name");
    const thDescription = createTh("Description");
    const thPrice = createTh("Price");
    const thStock = createTh("Stock");
    const emptyTh1 = document.createElement("th");
    const emptyTh2 = document.createElement("th");
    
    headTr.appendChild(thName)
    headTr.appendChild(thDescription)
    headTr.appendChild(thPrice);
    headTr.appendChild(thStock);
    headTr.appendChild(emptyTh1);
    headTr.appendChild(emptyTh2);

    tableHead.appendChild(headTr);
    //thead end

    //tbody
    const tableBody = document.createElement("tbody");
    products.forEach(product => {
        const productTr = createTrFromProduct(product);
        tableBody.appendChild(productTr);
    });
    //tbody end
    productTable.appendChild(tableHead);
    productTable.appendChild(tableBody);

    //Update Delete buttons
    const updateButton = createUpdateButton();
    const deleteButton = createDeleteButton();
    const buttonContainer = document.createElement("div");
    buttonContainer.appendChild(updateButton);
    buttonContainer.appendChild(deleteButton);

    div.appendChild(categoryNameDiv);
    div.appendChild(categoryDescriptionDiv);
    div.appendChild(productTable);
    div.appendChild(buttonContainer);
    productListContainer.appendChild(div);
}

function createCategory(name, products, categoryDescription) {
    const number = productListsCounter++;
    const id = "product-list-" + number;
    const div = document.createElement("div");
    div.setAttribute("class", "category");
    div.addEventListener("click", () => toggleProductList(id));
    const nameDiv = document.createElement("div");
    nameDiv.setAttribute("class", "category-name");
    nameDiv.innerText = name;
    div.appendChild(nameDiv)
    categoryContainer.appendChild(div);
    createProductList(id, name, products, categoryDescription);
}

function toggleProductList(id) {
    const productList = document.getElementById(id);
    productList.style.display = productList.style.display === "none" ? "table" : "none";
}

function switchToManagingProductList() {
    window.location.href = "../product_list/product_list.html";
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

async function getCategories() {
    const res = await sendRequest("/categories", "GET");
    const data = await res.json();
    console.log("Categories:", data);
    return data.map(readCategory);
}

function clearAllGroups() {
    while (categoryContainer.firstChild) {
        categoryContainer.removeChild(categoryContainer.firstChild);
    }
    while (productListContainer.firstChild) {
        productListContainer.removeChild(productListContainer.firstChild);
    }
}

async function refreshAllProductsData() {
    const res = await sendRequest("/products", "GET");
    if(res) {
        const data = await res.json();
        const products = data.map(readProduct);
        const categories = await getCategories();
        clearAllGroups();
        categories.forEach(category => {
            const prodsOfCat = products.filter(product => product.categoryName === category.categoryName);
            console.log("cat:", category.categoryDescription);
            createCategory(category.categoryName, prodsOfCat, category.categoryDescription);
        });
    } else {
        console.log("Error request.");
    }
}

async function deleteCategory(categoryName) {
    await sendRequest("/category?category_name=" + categoryName, "DELETE");
    await refreshAllProductsData();
}

async function onCategoryDelete(event) {
    const button = event.target;
    const productList = button.parentNode.parentNode;
    const categoryName = productList.getElementsByClassName("category-name")[0].textContent;
    await deleteCategory(categoryName);
}

async function main() {
    await refreshAllProductsData();
    manageGroupsButton.addEventListener("click", switchToManagingProductList);
}

main();

// createCategory("CategoryNEW", [
//     newProduct("Good1", "CategoryNew", "Some wonderful and big description that Gogi would like", 123, 13.42, "Gogi"),
//     newProduct("Good1", "CategoryNew", "Some wonderful and big description that Gogi would like", 123, 13.42, "Gogi"),
//     newProduct("Good1", "CategoryNew", "Some wonderful and big description that Gogi would like", 123, 13.42, "Gogi"),
//     newProduct("Good1", "CategoryNew", "Some wonderful and big description that Gogi would like", 123, 13.42, "Gogi"),
//     newProduct("Good1", "CategoryNew", "Some wonderful and big description that Gogi would like", 123, 13.42, "Gogi"),
//     newProduct("Good1", "CategoryNew", "Some wonderful and big description that Gogi would like", 123, 13.42, "Gogi"),
// ]);