package com.tmt.globalsuperstore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SuperstoreController {
    //creating data store
    private List<Item> items = new ArrayList<Item>();
    
    //mapping GET request to handler methods below
    @GetMapping("/")
    public String getForm(Model model, @RequestParam(required = false) String id){
                
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
    @PostMapping("/submitItem")
    public String handleSubmit(@Valid Item item, BindingResult result, RedirectAttributes redirectAttributes){
       
        if(item.getPrice() < item.getDiscount()){
            result.rejectValue("price", "", "Price cannot be less than the discount");
        }
        if (result.hasErrors()) return "form";

        //check if item exists in the datastore using its id to get its index, so it is updated or new item created if not
        int index = getItemIndex(item.getId());
        String status = Constants.SUCCESS_STATUS;

        if (index == Constants.NOT_FOUND) {
            //add new item if it doesnt exist
            items.add(item);
        }else if (within5Days(item.getDate(), items.get(index).getDate())) {
            //if updated date is within 5 days of the original date of the item, update it
            items.set(index, item);
        }else {
            //if item exists but updated date is not within 5 days
            status = Constants.FAILED_STATUS;
        }

         //RedirectAttributes temporarily store data like status that survives the redirect and is used in generating the redirected page 
        redirectAttributes.addFlashAttribute("status", status);
        //also redirect to the inventory page after form submission
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

    public boolean within5Days(Date newDate, Date oldDate) {
        //returns true if two dates are within 5 days
        long diff = Math.abs(newDate.getTime() - oldDate.getTime());
        return (int) (TimeUnit.MILLISECONDS.toDays(diff)) <= 5;
    }
}
