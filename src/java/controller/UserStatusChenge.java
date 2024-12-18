package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import entity.User;
import entity.UserStatus;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Session;

@WebServlet(name = "UserStatusChenge", urlPatterns = {"/UserStatusChenge"})
public class UserStatusChenge extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        JsonObject responseObject = new JsonObject();

        try {

            JsonObject requestObject = (JsonObject) gson.fromJson(req.getReader(), JsonObject.class);

            responseObject.addProperty("status", false);

            if (requestObject != null) {

                if (!requestObject.get("id").getAsString().isEmpty() && !requestObject.get("status").getAsString().isEmpty()) {

                    User user = (User) session.get(User.class, requestObject.get("id").getAsInt());

                    if (user != null) {

                        UserStatus status = (UserStatus) session.get(UserStatus.class, requestObject.get("status").getAsInt());

                        user.setUserStatus(status);

                        session.update(user);

                        session.beginTransaction().commit();

                        responseObject.addProperty("status", true);

                    }

                }

            }

            session.close();

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
