package entity;

import entity.ChatStatus;
import entity.Types;
import entity.User;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "chat")
public class Chat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @JoinColumn(name = "from_user_id", nullable = false)
    @ManyToOne
    User fromUser;

    @JoinColumn(name = "to_user_id", nullable = false)
    @ManyToOne
    User toUser;

    @Column(name = "message", nullable = false)
    String text;

    @Column(name = "date_time", nullable = false)
    Date time;

    @JoinColumn(name = "chat_status_id", nullable = false)
    @ManyToOne
    ChatStatus chatStatus;

    @JoinColumn(name = "types_id", nullable = false)
    @ManyToOne  
    Types types;

    public Chat() {
    }
    
    
    public Chat(User fromUser, User toUser, String text, Date time, ChatStatus chatStatus, Types types) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.text = text;
        this.time = time;
        this.chatStatus = chatStatus;
        this.types = types;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public ChatStatus getChatStatus() {
        return chatStatus;
    }

    public void setChatStatus(ChatStatus chatStatus) {
        this.chatStatus = chatStatus;
    }

    public Types getTypes() {
        return types;
    }

    public void setTypes(Types types) {
        this.types = types;
    }
    
    
    
    
    

}
