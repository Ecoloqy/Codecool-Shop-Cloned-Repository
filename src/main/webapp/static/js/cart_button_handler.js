import {dataHandler} from "./data_handler.js";

const setSameButton = document.querySelector("#set-same-button");
const submitButton = document.querySelector("#form-submit-button");

const countryBill = document.querySelector("#country");
const cityBill = document.querySelector("#city");
const zipCodeBill = document.querySelector("#zip-code");
const streetBill = document.querySelector("#street");
const localBill = document.querySelector("#local");

const countryShip = document.querySelector("#ship-country");
const cityShip = document.querySelector("#ship-city");
const zipCodeShip = document.querySelector("#ship-zip-code");
const streetShip = document.querySelector("#ship-street");
const localShip = document.querySelector("#ship-local");

export const cartButtonHandler = {
    init: (cart) => {
        setUpAllFieldsFromDatabase(cart.user.shippingAddress.id, cart.user.billingAddress.id);
        setSameButton.addEventListener("click", setAllFieldsWithSameValues);
        submitButton.addEventListener("click", (cart) => {
            checkFieldsAndCreateNewOrder(cart.user.shippingAddress.id, cart.id);
        });
    }
}

const setUpAllFieldsFromDatabase = (shipId, billId) => {
    dataHandler.getAddress(shipId, setUpShipAddress);
    dataHandler.getAddress(billId, setUpBillAddress);
}

const setUpShipAddress = data => {
    countryShip.setAttribute("value", data.country);
    cityShip.setAttribute("value", data.city);
    zipCodeShip.setAttribute("value", data.zipCode);
    streetShip.setAttribute("value", data.street);
    localShip.setAttribute("value", data.local);
}

const setUpBillAddress = data => {
    countryBill.setAttribute("value", data.country);
    cityBill.setAttribute("value", data.city);
    zipCodeBill.setAttribute("value", data.zipCode);
    streetBill.setAttribute("value", data.street);
    localBill.setAttribute("value", data.local);
}

const setAllFieldsWithSameValues = () => {
    countryShip.setAttribute("value", countryBill.value);
    cityShip.setAttribute("value", cityBill.value);
    zipCodeShip.setAttribute("value", zipCodeBill.value);
    streetShip.setAttribute("value", streetBill.value);
    localShip.setAttribute("value", localBill.value);
}

const checkFieldsAndCreateNewOrder = (addressId, cartId) => {
    const address = `{"country": "`+countryShip.value+`", "city": `+cityShip.value+`, "zipCode": "`+cityShip.value+`", "street": "`+cityShip.value+`", "localNumber": `+cityShip.value+`, "id": `+addressId+`}`;
    dataHandler.updateAddress(address, response => {});
    dataHandler.getCart(cartId, response => {
        generateNewOrder(response);
    })
}

const generateNewOrder = response => {
    const newOrder = `{"paid": false, "cart": `+ response +`"}`;
    dataHandler.postOrder(newOrder, response => {
        redirectToOrderDetails(response);
    })
}

const redirectToOrderDetails = orderId => {

}