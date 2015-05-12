package com.herokuapp.loginandregister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingList {
    String author;
    String listName;
    List<String> subscribers;
    HashMap<String,Boolean> items;

    public ShoppingList(String author, String listName){
        this.author = author;
        this.listName = listName;
        this.subscribers = new ArrayList<>();
        this.items = new HashMap<>();
    }

    public void changeListName(String listName){
        this.listName = listName;
    }

    public void addSubscriber(String subscriber) {
        subscribers.add(subscriber);
    }

    public void removeSubscriber(String subscriber){
        int i = subscribers.indexOf(subscriber);
        subscribers.remove(i);
    }

    public void addItem (String item){
        items.put(item,false);
    }

    public void removeItem(String item){
        items.remove(item);
    }

    public void checked (String item){
        boolean checked = items.get(item);
        if(checked){
            items.put(item,false);
        } else{
            items.put(item, true);
        }
    }
}
