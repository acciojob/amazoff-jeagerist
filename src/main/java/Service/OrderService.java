package com.driver.Service;

import com.driver.DeliveryPartner;
import com.driver.Order;
import com.driver.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;


    public void addOrder(Order order){

        String orderId =order.getId();
        int time = order.getDeliveryTime();

        orderRepository.addOrder(orderId,order);

    }

    public void addPartner( String partnerId){
        DeliveryPartner partner = new DeliveryPartner(partnerId);
        orderRepository.addPartner(partner);

    }

    public void addOrderPartnerPair( String orderId,  String partnerId){
        orderRepository.addOrderPartnerPair(orderId,partnerId);
    }

    public Order getOrderById( String orderId){

        return orderRepository.getOrderById(orderId);
    }

    public DeliveryPartner getPartnerById( String partnerId){
        return orderRepository.getPartnerById(partnerId);
    }

    public int getOrderCountByPartnerId( String partnerId){

        return orderRepository.getOrderCountByPartnerId(partnerId);
    }

    public List<String> getOrdersByPartnerId( String partnerId){

        return orderRepository.getOrdersByPartnerId(partnerId);
    }

    public List<String> getAllOrders(){
        return orderRepository.getAllOrders();
    }

    public int getCountOfUnassignedOrders(){

        return orderRepository.getCountOfUnassignedOrders();
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId( String time,  String partnerId){

        return orderRepository.getOrdersLeftAfterGivenTimeByPartnerId(time,partnerId);
    }

    public String getLastDeliveryTimeByPartnerId( String partnerId){

        return orderRepository.getLastDeliveryTimeByPartnerId(partnerId);
    }

    public void deletePartnerById( String partnerId){

        //Delete the partnerId
        //And push all his assigned orders to unassigned orders.

        orderRepository.deletePartnerById(partnerId);
    }

    public void deleteOrderById( String orderId){

        //Delete an order and also
        // remove it from the assigned order of that partnerId

        orderRepository.deleteOrderById(orderId);

    }

}