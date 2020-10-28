package com.codecool.shop.api;

import com.codecool.shop.logic.OrderLogic;
import com.codecool.shop.model.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/api/orders/*"})
public class OrdersServlet extends HttpServlet {

    OrderLogic orderLogic = new OrderLogic();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HelpServlet.sendRequestForAllElementsAndCheckSortAbility(request, response, orderLogic, Order.class);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HelpServlet.createInstanceAndUpdateElement(request, response, orderLogic, Order.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HelpServlet.createInstanceAndAddElement(request, response, orderLogic, Order.class);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HelpServlet.createInstanceAndRemoveElement(request, response, orderLogic, Order.class);
    }
}
