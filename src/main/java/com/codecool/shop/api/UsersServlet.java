package com.codecool.shop.api;

import com.codecool.shop.logic.UserLogic;
import com.codecool.shop.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/api/users/*"})
public class UsersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UserLogic userLogic = UserLogic.getInstance();
        HelpServlet.sendRequestForAllElementsAndCheckSortAbility(request, response, userLogic, User.class);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserLogic userLogic = UserLogic.getInstance();
        HelpServlet.createInstanceAndUpdateElement(request, response, userLogic, User.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserLogic userLogic = UserLogic.getInstance();
        HelpServlet.createInstanceAndAddElement(request, response, userLogic, User.class);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserLogic userLogic = UserLogic.getInstance();
        HelpServlet.createInstanceAndRemoveElement(request, response, userLogic, User.class);
    }
}
