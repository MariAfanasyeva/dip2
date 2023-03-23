package tools;
import org.apache.commons.lang3.RandomStringUtils;

public class UserDataGenerator {

    public String genEmail() {
        return RandomStringUtils.randomAlphabetic(12).toLowerCase() + "@yandex.ru";
    }

    public String genPassword() {
        return RandomStringUtils.randomAlphanumeric(12);
    }

    public String genName() {
        return RandomStringUtils.randomAlphabetic(1).toUpperCase() + RandomStringUtils.randomAlphabetic(10).toLowerCase();
    }
}