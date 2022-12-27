package com.tmt.globalsuperstore.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.globalsuperstore.Item;
import com.tmt.globalsuperstore.service.StoreService;

@Controller
public class SuperstoreController {

    @Autowired
    StoreService storeService;
        
    //mapping GET request to handler methods below
    @GetMapping("/")
    public String getForm(Model model, @RequestParam(required = false) String id){
        //new or existing item returned in form based on logic in service class
        model.addAttribute("item", storeService.getItemById(id));
        return "form";
    }

    //handler method to intercept POST request from the form
    @PostMapping("/submitItem")
    public String handleSubmit(@Valid Item item, BindingResult result, RedirectAttributes redirectAttributes){
       
        if(item.getPrice() < item.getDiscount()){
            result.rejectValue("price", "", "Price cannot be less than the discount");
        }
        if (result.hasErrors()) return "form";

        String status = storeService.submitItem(item);
         //RedirectAttributes temporarily store data like status that survives the redirect and is used in generating the redirected page 
        redirectAttributes.addFlashAttribute("status", status);
        //also redirect to the inventory page after form submission
        return "redirect:/inventory";
    }

    @GetMapping("/inventory")
    public String getInventory(Model model){
        model.addAttribute("items", storeService.getItems());
        return "inventory";
    }
}
