package com.codecool.shop.api;

import com.codecool.shop.logic.OrderLogic;
import com.codecool.shop.model.Order;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/api/orders/*"})
public class OrdersServlet extends ExtendedServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        OrderLogic orderLogic = OrderLogic.getInstance();
        HelpServlet.sendRequestForAllElementsAndCheckSortAbility(request, response, orderLogic);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        OrderLogic orderLogic = OrderLogic.getInstance();
        HelpServlet.createInstanceAndUpdateElement(request, response, orderLogic, Order.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        OrderLogic orderLogic = OrderLogic.getInstance();
        HelpServlet.createInstanceAndAddElement(request, response, orderLogic, Order.class);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        OrderLogic orderLogic = OrderLogic.getInstance();
        HelpServlet.createInstanceAndRemoveElement(request, response, orderLogic, Order.class);
    }

    @Override
    public void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderLogic orderLogic = OrderLogic.getInstance();
        String json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        PaidStatusOrder statusOrder = new Gson().fromJson(json, PaidStatusOrder.class);
        Order order = orderLogic.getElement(statusOrder.getOrderId());
        order.setPaid(statusOrder.getPaidStatus());
        orderLogic.updateElement(order, statusOrder.getOrderId());
    }

    static class PaidStatusOrder {

        private int orderId;
        private boolean paid;

        PaidStatusOrder(boolean paid, int orderId) {
            this.paid = paid;
            this.orderId = orderId;
        }

        public int getOrderId() {
            return orderId;
        }

        public boolean getPaidStatus() {
            return paid;
        }
    }
}
