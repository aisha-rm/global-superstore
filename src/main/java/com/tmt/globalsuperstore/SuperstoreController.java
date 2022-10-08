package com.tmt.globalsuperstore;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SuperstoreController {
    //creating data store
    private List<Item> items = new ArrayList<Item>();
    
    //mapping GET request to handler methods below
    @GetMapping("/")
    public String getForm(Model model, @RequestParam(required = false) String id){
        //add categories for drop down menu in form
        model.addAttribute("categories", Constants.CATEGORIES);

        //create item object that will be bound to the form 
        //check if item exists in data store so the form will prepopulate it, else create new item object 
        Item item;
        int index = getItemIndex(id);  //use the fxn to get the index of item whose id was passed in the update button's GET request
        if (index == Constants.NOT_FOUND) {
            item = new Item();
        }else {
            item = items.get(index);
        }
        
        model.addAttribute("item", item);
        return "form";
    }

    //handler method to intercept POST request from the form
    //and also redirect to the inventory page after form submission
    @PostMapping("/submitItem")
    public String handleSubmit(Item item){
        
        //check if item exists in the datastore using its id to get its index, so it is updated or new item created if not
        int index = getItemIndex(item.getId());
        if (index == Constants.NOT_FOUND) {
            items.add(item);
        }else {
            items.set(index, item);
        }
        
        return "redirect:/inventory";
    }

    @GetMapping("/inventory")
    public String getInventory(Model model){
        model.addAttribute("items", items);
        return "inventory";
    }
    
    
    public int getItemIndex(String id){
        //function that gets the index of the item object whose id 
        //that is in order to appropriately query the datastore
        for (int i=0; i<items.size(); i++){
            if (items.get(i).getId().equals(id)) return i;
             }
        return Constants.NOT_FOUND; //returned if the index is not found
    }
}
