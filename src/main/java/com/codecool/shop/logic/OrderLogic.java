package com.codecool.shop.logic;

import com.codecool.shop.dao.ModifyDao;
import com.codecool.shop.dao.ShopDatabaseManager;
import com.codecool.shop.dao.implementation.OrderDaoJdbc;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.OrderHistory;
import com.codecool.shop.model.User;

import java.util.List;

public class OrderLogic implements BusinessLogic<Order> {
    private OrderDaoJdbc orderDao = ShopDatabaseManager.Instance.getOrderDao();
    private ModifyDao<User> userDao = ShopDatabaseManager.Instance.getUserDao();
    private MailSender sender = MailSender.getInstance();

    private static OrderLogic instance = null;

    public static OrderLogic getInstance() {
        if (instance == null) {
            instance = new OrderLogic();
        }
        return instance;
    }

    @Override
    public int addElement(Order order) {
        int orderId = orderDao.add(order);
//        try {
//            String userEmail = order.getCart().getUser().getEmail();
//            String emailSubject = "Order successfully created.";
//            String emailBody = "Your order " + order.getId() + " waiting for payment!";
//            sender.sendEmail(userEmail, emailSubject, emailBody);
//        } catch (MessagingException e) {
//            throw new RuntimeException("Can't connect with email service", e);
//        } catch (IOException e) {
//            throw new RuntimeException("Connection file not found.", e);
//        }
        return orderId;
    }

    @Override
    public void updateElement(Order order, int id) {
        order.setId(id);
        orderDao.update(order);
    }

    @Override
    public void removeElement(Order order) {
        orderDao.remove(order.getId());
    }

    @Override
    public Order getElement(int id) {
        return orderDao.get(id);
    }

    public OrderHistory getOrdersHistoryBy(int userId) {
        User user = userDao.get(userId);
        System.out.println(user);
        List<Order> userOrders = orderDao.getOrdersByUserId(userId);

        for(Order order: userOrders){
            System.out.println(order);
        }

        return new OrderHistory(user, userOrders);
    }
}
