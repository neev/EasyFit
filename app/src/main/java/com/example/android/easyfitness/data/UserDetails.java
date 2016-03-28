package com.example.android.easyfitness.data;


import android.os.Parcel;
import android.os.Parcelable;

public class UserDetails implements Parcelable {

    private String fullName;
    private String email;
    private int age;
    private int weight;
    private int goalWeight;





    private String imageName;
    private byte[] image;





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
    public String getImageName() {
        return imageName;
    }

    public int getGoalWeight() {
        return goalWeight;
    }

    public void setGoalWeight(int goalWeight) {
        this.goalWeight = goalWeight;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
    public UserDetails() {}
    public UserDetails(String fullName, String email,int age, int weight, int goalWeight) {
        this.fullName = fullName;
        this.email = email;
        this.age = age;
        this.weight = weight;
        this.goalWeight = goalWeight;
    }

    public UserDetails(String fullName, String email, int age, int weight, int goalWeight, String
            imageName, byte[]
            image) {
        this.fullName = fullName;
        this.email = email;
        this.age = age;
        this.weight = weight;
        this.goalWeight = goalWeight;
        this.imageName = imageName;
        this.image = image;
    }


    // for pracelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullName);
        dest.writeString(email);
        dest.writeInt(age);
        dest.writeInt(weight);
        dest.writeInt(goalWeight);
        dest.writeString(imageName);
        // dest.writeInt(image.length);
        // dest.writeByteArray(image);
    }

    private void readFromParcel(Parcel in) {
        fullName = in.readString();
        email=in.readString();
        age=in.readInt();
        weight=in.readInt();
        goalWeight=in.readInt();
        //imageName=in.readString();
        //image = new byte[in.readInt()];
        // in.readByteArray(image);
    }
    public UserDetails(Parcel in){
        readFromParcel(in);
    }
    public static final Creator<UserDetails> CREATOR = new Creator<UserDetails>() {

        @Override
        public UserDetails createFromParcel(Parcel source) {
            return new UserDetails(source);
        }
        @Override
        public UserDetails[] newArray(int size) {
            return new UserDetails[size];
        }

    };

}