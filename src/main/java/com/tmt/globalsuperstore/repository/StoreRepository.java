package com.tmt.globalsuperstore.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.tmt.globalsuperstore.Item;

@Repository
public class StoreRepository {
    //creating data store
    private List<Item> items = new ArrayList<Item>();

    //adding crud operations
    public Item getItem(int index){
        return items.get(index);
    }

    public void addItem(Item item){
        items.add(item);
    }

    public void updateItem(Item item, int index){
        items.set(index, item);
    }

    public List<Item> getItems(){
        return items;
    }
}
