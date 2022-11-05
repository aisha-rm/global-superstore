package com.tmt.globalsuperstore.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.tmt.globalsuperstore.Constants;
import com.tmt.globalsuperstore.Item;
import com.tmt.globalsuperstore.repository.StoreRepository;

public class StoreService {

    StoreRepository storeRepo = new StoreRepository();

    //interfacing controller and repo
    public Item getItem(int index){
        return storeRepo.getItem(index);
    }

    public void addItem(Item item){
        storeRepo.addItem(item);
    }

    public void updateItem(Item item, int index){
        storeRepo.updateItem(item, index);
    }

    public List<Item> getItems(){
        return storeRepo.getItems();
    }

    //logic
    public int getItemIndex(String id){
        //function that gets the index of the item object whose id is gotten in GET request
        //that is in order to appropriately query the datastore
        for (int i=0; i<getItems().size(); i++){
            if (getItems().get(i).getId().equals(id)) return i;
             }
        return Constants.NOT_FOUND; //returned if the index is not found
    }
    
    public Item getItemById(String id){
        int index = getItemIndex(id);  //use the fxn to get the index of item 
        if (index == Constants.NOT_FOUND) {
            return new Item();
        }else {
            return getItem(index);
        }
    }

    public boolean within5Days(Date newDate, Date oldDate) {
        //returns true if two dates are within 5 days
        long diff = Math.abs(newDate.getTime() - oldDate.getTime());
        return (int) (TimeUnit.MILLISECONDS.toDays(diff)) <= 5;
    }

    public String submitItem(Item item){
        //check if item exists in the datastore using its id to get its index, so it is updated or new item created if not
        int index = getItemIndex(item.getId());
        String status = Constants.SUCCESS_STATUS;

        if (index == Constants.NOT_FOUND) {
            //add new item if it doesnt exist
            addItem(item);
        }else if (within5Days(item.getDate(), getItem(index).getDate())) {
            //if updated date is within 5 days of the original date of the item, update it
            updateItem(item, index);
        }else {
            //if item exists but updated date is not within 5 days
            status = Constants.FAILED_STATUS;
        }
        return status;
    }

}
