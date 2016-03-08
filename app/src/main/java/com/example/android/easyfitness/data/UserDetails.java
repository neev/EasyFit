package com.example.android.easyfitness.data;

/**
 * Created by neeraja on 3/5/2016.
 */
public class UserDetails {

    private String fullName;
    private String email;
    private int age;
    private int weight;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserDetails() {}
    public UserDetails(String fullName, String email,int age, int weight) {
        this.fullName = fullName;
        this.email = email;
        this.age = age;
        this .weight = weight;
    }

}
/*Firebase alanRef = ref.child("users").child("alanisawesome");
User alan = new User("Alan Turing", 1912);
alanRef.setValue(alan);*/

