package com.erginus.lifedonor.Model;


public class BloodBankModel
{
    public  String name, id,  desc, address,contact,website,city;
    private String sortKey;

    public String getContact() {
        return contact;
    }

    public void setContact(String name) {
        this.contact = name;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String desc) {
        this.address = desc;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String desc) {
        this.website = desc;
    }
    public String get_city_name() {

        return city;
    }

    public void set_city_name(String desc) {
        this.city = desc;
    }

}
