package entity;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Expose
    private int id;

    @Column(name = "mobile", nullable = false, length = 100)
    @Expose
    private String mobile;

    @Column(name = "name", nullable = false, length = 45)
    @Expose
    private String name;

   
    @Column(name = "otp", nullable = false, length = 6)
    private String otp;

    @Column(name = "password", nullable = false, length = 20)
    private String password;

    @ManyToOne
    @JoinColumn(name = "user_status_id", nullable = false)
    @Expose
    private UserStatus userStatus;

    public User() {

    }

    public User(String mobile, String name, String otp, String password, UserStatus userStatus) {
        this.mobile = mobile;
        this.name = name;
        this.otp = otp;
        this.password = password;
        this.userStatus = userStatus;
    }
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

  

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }
   
}
