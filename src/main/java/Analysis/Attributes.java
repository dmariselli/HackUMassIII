package Analysis;

public class Attributes {

    private String gender;
    private int age;

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "\"attributes\":{\"gender\":\"" + gender + "\",\"age\":" + age + "}";
    }
}
