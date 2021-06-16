package models;

public class AdminUser {
    String userName;
    String email;
    String password;
    String superuser;

    public AdminUser(String userName, String email, String password, String superuser) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.superuser = superuser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSuperuser() {
        return superuser;
    }

    public void setSuperuser(String superuser) {
        this.superuser = superuser;
    }
}
