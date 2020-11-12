import {cartButtonHandler} from "./cart_button_handler.js";
import {dataHandler} from "./data_handler.js";
import {util} from "./util.js";
import {buttonHandler} from "./buttons_handler.js";

const itemsContainer = document.querySelector("#items");
const usernameTitleContainer = document.querySelector("#username-cart-title");
const removeAllButton = document.querySelector("#remove-all-from-basket-button");

const cartHandler = {
    cartId: 0,
    activate: () => {
        cartHandler.cartId = 1;
        generateItemsInBasket(cartHandler.cartId);
    }
}

const generateItemsInBasket = cartId => {
    dataHandler.getCart(cartId, response => {
        createListOfItems(response);
        cartButtonHandler.init(response);
        removeAllButton.addEventListener("click", () => {
            document.querySelectorAll(".card-details").forEach(product => {
                product.parentElement.removeChild(product);
            })
            response.products = [];
            dataHandler.removeAllProductsFromCart(response);
            updatePrices();
        });
    })
}

const createListOfItems = data => {
    let totalPrice = 0;
    for (let i = 0; i < data.products.length; i++) {
        const productDetails = data.products[i].product;
        const dataToInsert = {
            product_id: data.products[i].product.id,
            name: data.products[i].name,
            quantity: data.products[i].quantity,
            subTotalPrice: data.products[i].quantity * productDetails.unitPrice,
            currency: productDetails.currency,
            unitPrice: productDetails.unitPrice
        }
        totalPrice += data.products[i].quantity * productDetails.unitPrice;
        addItemToDisplay(dataToInsert);
    }
    usernameTitleContainer.textContent = data.user.name + "'s cart";
    let currency = "EURO";
    if (data.products[0] != null) {
        currency = data.products[0].product.currency;
    }
    addTotalPriceContainer(totalPrice + " " + currency);
}

const addItemToDisplay = dataToInsert => {
    const cardDetails = util.createElementWithClasses("div", "card-details", "item", "flex-row");
    const name = util.createElementWithClasses("p", "item-name");
    name.innerText = dataToInsert.name;

    const quantityContainer = util.createElementWithClasses("div", "quantity", "flex-row");
    quantityContainer.setAttribute("product_id", dataToInsert.product_id)

    const minusButton = util.createElementWithClasses("a", "plus-minus", "decrease-quantity");
    minusButton.setAttribute("href", "#");
    const minusIcon = util.createElementWithClasses("i", "fas", "fa-minus");
    minusButton.appendChild(minusIcon);


    minusButton.addEventListener("click", function () {
        const quantityNumber = document.querySelector(`div[product_id='${dataToInsert.product_id}'] p`);
        let quantity = parseInt(quantityNumber.textContent);
        quantity = Math.max((quantity - 1), 1);
        quantityNumber.textContent = quantity.toString();
        updatePrices();

        buttonHandler.updateProductInCart(cartHandler.cartId, dataToInsert.product_id, quantity);
    })

    const quantityValue = util.createElementWithClasses("p", "number");
    quantityValue.innerText = dataToInsert.quantity;

    const plusButton = util.createElementWithClasses("a", "plus-minus", "increase-quantity");
    plusButton.setAttribute("href", "#");
    const plusIcon = util.createElementWithClasses("i", "fas", "fa-plus");
    plusButton.appendChild(plusIcon);

    plusButton.addEventListener("click", function () {
        const quantityNumber = document.querySelector(`div[product_id='${dataToInsert.product_id}'] p`);
        let quantity = parseInt(quantityNumber.textContent);
        quantity = quantity + 1;
        quantityNumber.textContent = quantity.toString();
        updatePrices();

        buttonHandler.updateProductInCart(cartHandler.cartId, dataToInsert.product_id, quantity);
    })

    quantityContainer.appendChild(minusButton);
    quantityContainer.appendChild(quantityValue);
    quantityContainer.appendChild(plusButton);

    const subCost = util.createElementWithClasses("span", "total-cost");
    subCost.setAttribute("data-unitPrice", dataToInsert.unitPrice);

    const subCostValue = util.createElementWithClasses("span", "total-cost-value");
    subCostValue.innerText = dataToInsert.subTotalPrice;

    const subCostCurrency = util.createElementWithClasses("span", "total-cost-currency");
    subCostCurrency.innerText = dataToInsert.currency;

    const space = util.createElementWithClasses("span");
    space.innerText = " ";

    subCost.appendChild(subCostValue);
    subCost.appendChild(space);
    subCost.appendChild(subCostCurrency);

    const removeButton = util.createElementWithClasses("a", "remove-from-cart-button");
    const removeIcon = util.createElementWithClasses("i", "fas", "fa-trash-alt");
    removeButton.appendChild(removeIcon);
    removeButton.innerText = "Remove Item";
    removeButton.addEventListener("click", () => removeButtonEvent(dataToInsert, removeButton));

    cardDetails.appendChild(name);
    cardDetails.appendChild(quantityContainer);
    cardDetails.appendChild(subCost);
    cardDetails.appendChild(removeButton);

    itemsContainer.appendChild(cardDetails);
}

const removeButtonEvent = (dataToInsert, removeButton) => {
    removeButton.parentElement.parentElement.removeChild(removeButton.parentElement);
    updatePrices();
    buttonHandler.removeProductFromCart(cartHandler.cartId, dataToInsert.product_id);
}

const addTotalPriceContainer = totalPrice => {
    const totalPriceBox = util.createElementWithClasses("div", "card-total-price", "item", "flex-row");
    const number = util.createElementWithClasses("span", "number");
    const description = document.createElement("span");
    description.textContent = "Total price: ";
    const totalPriceValue = util.createElementWithClasses("span", "total-price-value");
    totalPriceValue.innerText = totalPrice;

    number.appendChild(description);
    number.appendChild(totalPriceValue);

    totalPriceBox.appendChild(number);
    itemsContainer.appendChild(totalPriceBox);
}

function updatePrices() {
    const cards = document.querySelectorAll(".card-details");

    let totalPrice = 0;

    cards.forEach(parent => {
        const quantityValue = parseInt(parent.querySelector(".quantity .number").textContent);
        const subTotalPriceElement = parent.querySelector(".total-cost");
        const unitPriceValue = parseInt(subTotalPriceElement.getAttribute("data-unitprice"));

        const subTotalPrice = quantityValue * unitPriceValue;

        subTotalPriceElement.querySelector(".total-cost-value").textContent = subTotalPrice.toString();
        totalPrice += subTotalPrice;
    })

    const totalPriceElement = document.querySelector(".total-price-value");
    totalPriceElement.textContent = totalPrice.toString() + " EURO";
}

cartHandler.activate();
util.redirectToHomePage();