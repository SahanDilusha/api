package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import model.Validations;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

  @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        JsonObject responseObject = new JsonObject();
        
        try {
            
            JsonObject reuestObject = (JsonObject) gson.fromJson(req.getReader(), JsonObject.class);
            
            responseObject.addProperty("status", false);
            
            if (reuestObject == null) {
                responseObject.addProperty("content", "Request Data missing!");
            } else if (!Validations.isMobile(reuestObject.get("mobile").getAsString())) {
                responseObject.addProperty("content", "Invalide Mobile!");
            } else if (!Validations.isPasswordValid(reuestObject.get("password").getAsString())) {
                responseObject.addProperty("content", "Invalide Password!");
            } else {
                
                User user = (User) session.createCriteria(User.class).add(Restrictions.eq("mobile", reuestObject.get("mobile").getAsString())).add(Restrictions.eq("password", reuestObject.get("password").getAsString())).uniqueResult();
                
                if (user != null) {
                    responseObject.addProperty("status", true);
                    responseObject.add("content", gson.toJsonTree(user));
                } else {
                    responseObject.addProperty("content", "Inavalide mobile or password!");
                }
                
            }
            
            resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(responseObject));
            
        } catch (Exception e) {
            e.printStackTrace();
            responseObject.addProperty("content", "System error!");
            resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(responseObject));
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        
    }
    

}
