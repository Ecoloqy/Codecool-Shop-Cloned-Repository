package com.codecool.shop.api;

import com.codecool.shop.logic.*;
import com.codecool.shop.model.ProductInCart;
import com.codecool.shop.model.User;
import com.google.gson.Gson;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import org.json.simple.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

public class HelpServlet {
    public static final int USER_ALREADY_RESENT = -1;

    public static <T> void sendRequestForAllElementsAndCheckSortAbility(HttpServletRequest request, HttpServletResponse response, BusinessLogic<T> logic) throws IOException, ServletException {
        final int MODEL_ID_INDEX = 1;

        PrintWriter out = HelpServlet.createPrintWriterAndSetItUp(response);
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            if (logic instanceof Sortable) {
                getAllSortedElements(request, out, (Sortable<T>) logic);
            } else if (logic instanceof GetAllLogic) {
                getAllUnsortedElements((GetAllLogic<T>) logic, out);
            }
        } else {
            String[] splits = HelpServlet.getSplitUrlIfLengthIsEqual2(response, pathInfo);
            int productId = parseParameterIdToInteger(splits[MODEL_ID_INDEX]);
            createJsonFromElement(logic, productId, out);
        }
    }

    public static <T> void createInstanceAndAddElement(HttpServletRequest request, HttpServletResponse response, BusinessLogic<T> logic, Class<T> classType) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            T element = createElementFromJson(request, response, classType);
            int id = logic.addElement(element);
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print("{\"id\": \""+id+"\"}");
            response.setStatus(HttpServletResponse.SC_OK);
            out.flush();
        } else {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }

    public static <T> void createInstanceAndAddUserIfNotPresent(HttpServletRequest request, HttpServletResponse response, JSONObject userJSON) throws IOException {
        String pathInfo = request.getPathInfo();
        UserLogic userLogic = UserLogic.getInstance();

        if (pathInfo == null || pathInfo.equals("/")) {
            User user = new Gson().fromJson(userJSON.toJSONString(), User.class);
            int userId = userLogic.addElementWithOutAddress(user);
            if (userId == USER_ALREADY_RESENT) response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            else response.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public static <T> void createInstanceAndUpdateElement(HttpServletRequest request, HttpServletResponse response, BusinessLogic<T> logic, Class<T> classType) throws IOException {
        T element = createElementFromJson(request, response, classType);
        if (request.getPathInfo() != null) {
            String[] splits = getSplitUrlIfLengthIsEqual2(response, request.getPathInfo());
            logic.updateElement(element, Integer.parseInt(splits[1]));
        }
        response.setStatus(200);
    }

    public static <T> void createInstanceAndRemoveElement(HttpServletRequest request, HttpServletResponse response, BusinessLogic<T> logic, Class<T> classType) throws IOException {
        T element = createElementFromJson(request, response, classType);
        logic.removeElement(element);
        response.setStatus(200);
    }

    public static void createInstanceAndUpdateCartContent(HttpServletRequest request, HttpServletResponse response, CartLogic cartLogic) throws IOException, ServletException {
        final int modelIdIndex = 1;
        ProductInCart productInCart = createElementFromJson(request, response, ProductInCart.class);
        System.out.println(productInCart.toString());
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            String action = request.getParameter("action");
            String[] splits = HelpServlet.getSplitUrlIfLengthIsEqual2(response, pathInfo);
            int cardId = parseParameterIdToInteger(splits[modelIdIndex]);
            cartLogic.updateProductInCart(productInCart, cardId, action);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }

    private static <T> T createElementFromJson(HttpServletRequest request, HttpServletResponse response, Class<T> classType) throws IOException {
        String pathInfo = getPathInfoWhenUriContainsIdParameter(request);
        if (pathInfo != null) {
            getSplitUrlIfLengthIsEqual2(response, pathInfo);
        }
        String json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        return new Gson().fromJson(json, classType);
    }

    private static <T> void createJsonFromElement(BusinessLogic<T> businessClass, int elementId, PrintWriter out) {
        T element = businessClass.getElement(elementId);
        String output = new Gson().toJson(element);
        out.print(output);
        out.flush();
    }

    private static <T> void getAllSortedElements(HttpServletRequest request, PrintWriter out, Sortable<T> logic) {
        String sortType = HelpServlet.getParameterIfExist(request, "sort", "default");
        String sortBy = HelpServlet.getParameterIfExist(request, "by", "default");

        List<T> elements = logic.getAllElements(sortType, sortBy);
        String output = new Gson().toJson(elements);
        out.print(output);
        out.flush();
    }

    private static <T> void getAllUnsortedElements(GetAllLogic<T> businessClass, PrintWriter out) {
        List<T> elements = businessClass.getAllElements();
        String output = new Gson().toJson(elements);
        out.print(output);
        out.flush();
    }

    private static String getParameterIfExist(ServletRequest request, String paramName, String defaultValue) {
        if (request.getParameter(paramName) != null) {
            return request.getParameter(paramName);
        } else {
            return defaultValue;
        }
    }

    private static void setResponseTypeToJson(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

    private static int parseParameterIdToInteger(String element) throws ServletException {
        try {
            return Integer.parseInt(element);
        } catch (NumberFormatException e) {
            throw new ServletException("Id parameter is not valid.", e);
        }
    }

    private static PrintWriter createPrintWriterAndSetItUp(HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        HelpServlet.setResponseTypeToJson(response);
        return out;
    }

    private static String[] getSplitUrlIfLengthIsEqual2(HttpServletResponse response, String pathInfo) throws IOException {
        if (pathInfo.contains("/")) {
            String[] splits = pathInfo.split("/");
            if (splits.length != 2) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
            return splits;
        }
        return null;
    }

    private static String getPathInfoWhenUriContainsIdParameter(HttpServletRequest request){
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            return null;
        }
        return pathInfo;
    }

    public static String decryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }
}