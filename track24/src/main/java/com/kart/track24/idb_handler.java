package com.kart.track24;

public interface idb_handler {
    public void addContact(track contact);
    public track getContact(int id);
    //  public List<track> getAllContacts();
    public int getContactsCount();
    public int updateContact(track contact);
    public void deleteContact(track contact);
    public void deleteAll();
}