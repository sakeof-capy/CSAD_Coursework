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

function createTrFromProduct(product) {
    const tr = document.createElement("tr");
    const tdName = createTdWithText(product.productName);
    const tdDescription = createTdWithText(product.productDescription);
    const tdPrice = createTdWithText('$' + product.productPrice);
    const tdStock = createTdWithText(product.productStock);
    const tdUpdate = createTdWithUpdateButton();
    const tdDelete = createTdWithDeleteButton();
    [tdName, tdDescription, tdPrice, tdStock, tdUpdate, tdDelete]
        .forEach(elem => tr.appendChild(elem));
    return tr;
}

function createProductList(id, categoryName, products) {
    const div = document.createElement("div");
    div.setAttribute("id", id);
    div.setAttribute("class", "product-list");
    div.setAttribute("style", "display: none;");

    const categoryNameDiv = document.createElement("div");
    categoryNameDiv.setAttribute("class", "category-name");
    categoryNameDiv.innerText = categoryName;

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

    div.appendChild(categoryNameDiv);
    div.appendChild(productTable);
    productListContainer.appendChild(div);
}

function createCategory(name, products) {
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
    createProductList(id, name, products);
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

function getCategoryNames(products) {
    return [...new Set(products.map(product => product.categoryName))];
}

function getCategoryNamesToProducts(categoryNames, allProducts) {
    let res = {};
    categoryNames.forEach(categoryName => {
        const productsOfCategory = allProducts
            .filter(product => product.categoryName === categoryName);
        res[categoryName] = productsOfCategory;
    });

    return res;
}

async function refreshAllProductsData() {
    const res = await sendRequest("/products", "GET");
    if(res) {
        const data = await res.json();
        const products = data.map(readProduct);
        const categoryNames = getCategoryNames(products);
        const catsToProds = getCategoryNamesToProducts(categoryNames, products);
        Object.entries(catsToProds)
            .forEach(([catName, products]) => createCategory(catName, products));
    } else {
        console.log("Error request.");
    }
}

async function main() {
    await refreshAllProductsData();
}

main();
  
manageGroupsButton.addEventListener("click", switchToManagingProductList);

// createCategory("CategoryNEW", [
//     newProduct("Good1", "CategoryNew", "Some wonderful and big description that Gogi would like", 123, 13.42, "Gogi"),
//     newProduct("Good1", "CategoryNew", "Some wonderful and big description that Gogi would like", 123, 13.42, "Gogi"),
//     newProduct("Good1", "CategoryNew", "Some wonderful and big description that Gogi would like", 123, 13.42, "Gogi"),
//     newProduct("Good1", "CategoryNew", "Some wonderful and big description that Gogi would like", 123, 13.42, "Gogi"),
//     newProduct("Good1", "CategoryNew", "Some wonderful and big description that Gogi would like", 123, 13.42, "Gogi"),
//     newProduct("Good1", "CategoryNew", "Some wonderful and big description that Gogi would like", 123, 13.42, "Gogi"),
// ]);