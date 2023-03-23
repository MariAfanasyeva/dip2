package serialization;

//сериализация для создания пользователя
public class User {

    //ключи из JSON как поля класса
    private String email;
    private String password;
    private String name;

    //конструктор без параметров
    public User() {
    }

    //конструктор с параметрами из полей класса
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    //конструктор для логина (без поля Имя)
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    //геттеры и сеттеры
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
