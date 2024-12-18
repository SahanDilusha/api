package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import entity.Chat;
import entity.User;
import java.io.File;
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
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "GetMainData", urlPatterns = {"/GetMainData"})
public class GetMainData extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        JsonObject responseObject = new JsonObject();

        try {

            JsonObject requestObject = (JsonObject) gson.fromJson(req.getReader(), JsonObject.class);

            responseObject.addProperty("status", false);

            if (requestObject != null) {

                if (!requestObject.get("id").getAsString().isEmpty()) {

                    User user = (User) session.get(User.class, requestObject.get("id").getAsInt());

                    if (user != null) {

                        Criteria criteria = session.createCriteria(User.class)
                                .add(Restrictions.ne("id", user.getId()));

                        if (requestObject.get("text") != null && !requestObject.get("text").getAsString().trim().isEmpty()) {
                            criteria.add(
                                    Restrictions.like("name", requestObject.get("text").getAsString().trim() + "%")   
                            );
                        }

                        List<User> userList = (List<User>) criteria.list();

                        List<JsonObject> list = new ArrayList<>();

                        for (User contact : userList) {

                            List<Chat> chatList = (List<Chat>) session.createCriteria(Chat.class)
                                    .add(Restrictions.or(
                                            Restrictions.and(
                                                    Restrictions.eq("fromUser", user),
                                                    Restrictions.eq("toUser", contact)
                                            ),
                                            Restrictions.and(
                                                    Restrictions.eq("fromUser", contact),
                                                    Restrictions.eq("toUser", user)
                                            )
                                    )).addOrder(Order.asc("id")).list();

                            JsonObject object = new JsonObject();

                            object.addProperty("toUser", contact.getId());
                            object.addProperty("name", contact.getName());

                            if (new File(req.getServletContext().getRealPath("") + File.separator + "AvatarImages" + File.separator + contact.getMobile() + ".png").exists()) {
                                object.addProperty("image", true);
                            } else {
                                object.addProperty("image", false);
                            }

                            object.addProperty("status", user.getUserStatus().getId());

                            JsonObject chatObject = new JsonObject();

                            int count = 0;

                            if (!chatList.isEmpty()) {

                                for (Chat c : chatList) {

                                    if (c.getToUser().equals(user) && c.getChatStatus().getId() == 2) {
                                        count++;
                                    }

                                }

                                Chat chat = chatList.get(0);

                                System.out.println(chat.getId());

                                chatObject.addProperty("msg", chat.getText());
                                chatObject.addProperty("fromUser", chat.getFromUser().getId());
                                chatObject.addProperty("time", new SimpleDateFormat("yyyy-mm-dd hh:mm a").format(chat.getTime()));
                                chatObject.addProperty("status", chat.getChatStatus().getId());

                            }

                            object.addProperty("count", count);

                            object.add("lastChat", gson.toJsonTree(chatObject));

                            list.add(object);

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
