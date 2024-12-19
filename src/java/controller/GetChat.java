package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import entity.Chat;
import entity.ChatStatus;
import entity.User;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "GetChat", urlPatterns = {"/GetChat"})
public class GetChat extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        JsonObject responseObject = new JsonObject();

        try {

            JsonObject requestObject = (JsonObject) gson.fromJson(req.getReader(), JsonObject.class);

            responseObject.addProperty("status", false);

            if (requestObject != null) {

                if (!requestObject.get("fromUser").getAsString().isEmpty() && !requestObject.get("toUser").getAsString().isEmpty()) {

                    User fromUser = (User) session.get(User.class, requestObject.get("fromUser").getAsInt());
                    User toUser = (User) session.get(User.class, requestObject.get("toUser").getAsInt());

                    if (fromUser != null && toUser != null) {

                        List<Chat> chatList = (List<Chat>) session.createCriteria(Chat.class)
                                .add(Restrictions.or(
                                        Restrictions.and(
                                                Restrictions.eq("fromUser", fromUser),
                                                Restrictions.eq("toUser", toUser)
                                        ),
                                        Restrictions.and(
                                                Restrictions.eq("fromUser", toUser),
                                                Restrictions.eq("toUser", fromUser)
                                        )
                                )).addOrder(Order.asc("id")).list();

                        List<JsonObject> list = new ArrayList<>();

                        if (!chatList.isEmpty()) {

                            ChatStatus status = (ChatStatus) session.get(ChatStatus.class, 1);

                            for (Chat chat : chatList) {

                                if (chat.getToUser().equals(fromUser) && !chat.getChatStatus().equals(status)) {

                                    chat.setChatStatus(status);

                                    session.update(chat);
                                    session.beginTransaction().commit();
                                }

                                JsonObject chatListObject = new JsonObject();

                                chatListObject.addProperty("id", chat.getId());
                                chatListObject.addProperty("fromUser", chat.getFromUser().getId());
                                chatListObject.addProperty("msg", chat.getText());
                                chatListObject.addProperty("time", new SimpleDateFormat("yyyy-mm-dd hh:mm a").format(chat.getTime()));
                                chatListObject.addProperty("status", chat.getChatStatus().getId());

                                list.add(chatListObject);

                            }

                        }

                        responseObject.addProperty("status", true);
                        responseObject.add("content", gson.toJsonTree(list));
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
