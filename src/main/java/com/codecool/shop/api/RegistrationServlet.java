package com.codecool.shop.api;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.ModifyDao;
import com.codecool.shop.logic.OrderLogic;
import com.codecool.shop.logic.UserLogic;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.User;
import org.json.simple.JSONObject;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/registration")
public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext context = new WebContext(request, response, request.getServletContext());
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        engine.process("user/registration.html", context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HelpServlet.createInstanceAndAddUserIfNotPresent(request, response);

        PrintWriter out = response.getWriter();
        if (response.getStatus() == HttpServletResponse.SC_CREATED) {
            out.print("{\"status\": 201}");
        } else {
            out.print("{\"status\": 202}");
        }
        out.flush();
    }

    private JSONObject createJSONObjectFromParameters(HttpServletRequest request) {
        String name = request.getParameter("user-name");
        String email = request.getParameter("user-email");
        String password = request.getParameter("password");
        String hashedPassword = HelpServlet.decryptPassword(password);

        JSONObject userDetails = new JSONObject();
        userDetails.put("name", name);
        userDetails.put("email", email);
        userDetails.put("password", hashedPassword);
        return userDetails;
    }
}
