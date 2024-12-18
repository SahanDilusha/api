package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import entity.Chat;
import entity.ChatStatus;
import entity.Types;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import model.Validations;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "Save", urlPatterns = {"/Save"})
public class Save extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        JsonObject responseObject = new JsonObject();

        try {

            JsonObject requestObject = (JsonObject) gson.fromJson(req.getReader(), JsonObject.class);

            responseObject.addProperty("status", false);

            if (requestObject != null) {

                if (!requestObject.get("fromUser").getAsString().isEmpty() && !requestObject.get("toUser").getAsString().isEmpty() && !requestObject.get("text").getAsString().isEmpty()) {

                    User fromUser = (User) session.get(User.class, requestObject.get("fromUser").getAsInt());
                    User toUser = (User) session.get(User.class, requestObject.get("toUser").getAsInt());

                    if (fromUser != null && toUser != null) {

                        Types types = (Types) session.get(Types.class, 1);
                        ChatStatus chatStatus = (ChatStatus) session.get(ChatStatus.class, 2);

                        if (types == null) {
                            System.out.println("null types");
                            return;
                        }

                        Chat chat = new Chat();

                        chat.setFromUser(fromUser);
                        chat.setToUser(toUser);
                        chat.setText(requestObject.get("text").getAsString());
                        chat.setChatStatus(chatStatus);
                        chat.setTypes(types);
                        chat.setTime(new Date());

                        session.save(chat);

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
