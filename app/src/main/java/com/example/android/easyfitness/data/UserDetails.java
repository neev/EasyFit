package com.example.android.easyfitness.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by neeraja on 3/5/2016.
 */
public class UserDetails implements Parcelable {

    private String fullName;
    private String email;
    private int age;
    private int weight;
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
    public UserDetails(String fullName, String email,int age, int weight) {
        this.fullName = fullName;
        this.email = email;
        this.age = age;
        this .weight = weight;
    }

    public UserDetails(String fullName, String email, int age, int weight, String imageName, byte[]
            image) {
        this.fullName = fullName;
        this.email = email;
        this.age = age;
        this.weight = weight;
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
        dest.writeString(imageName);
       // dest.writeInt(image.length);
       // dest.writeByteArray(image);
    }

    private void readFromParcel(Parcel in) {
        fullName = in.readString();
        email=in.readString();
        age=in.readInt();
        weight=in.readInt();
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
/*Firebase alanRef = ref.child("users").child("alanisawesome");
User alan = new User("Alan Turing", 1912);
alanRef.setValue(alan);*/

