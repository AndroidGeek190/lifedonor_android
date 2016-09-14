package com.erginus.lifedonor.Model;

/**
 * Created by paramjeet on 10/9/15.
 */
public class DonorModel {
    public  String name, id,  image, address,contact,b_group, distance;

    public String getcontact() {
        return contact;
    }

    public void setcontact(String name) {
        this.contact = name;
    }

    public String getblood_group() {
        return b_group;
    }

    public void setblood_group(String name) {
        this.b_group = name;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

 public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String desc) {
        this.address = desc;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String desc) {
        this.distance = desc;
    }


}
