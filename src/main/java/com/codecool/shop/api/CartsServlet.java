package com.codecool.shop.api;

import com.codecool.shop.logic.CartLogic;
import com.codecool.shop.model.Cart;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/api/carts/*"})
public class CartsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        CartLogic cartLogic = CartLogic.getInstance();
        HelpServlet.sendRequestForAllElementsAndCheckSortAbility(request, response, cartLogic);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CartLogic cartLogic = CartLogic.getInstance();
        HelpServlet.createInstanceAndUpdateElement(request, response, cartLogic, Cart.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CartLogic cartLogic = CartLogic.getInstance();
        HelpServlet.createInstanceAndAddElement(request, response, cartLogic, Cart.class);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CartLogic cartLogic = CartLogic.getInstance();
        HelpServlet.createInstanceAndRemoveElement(request, response, cartLogic, Cart.class);
    }
}
