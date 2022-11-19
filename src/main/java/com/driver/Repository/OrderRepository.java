package com.driver.Repository;

import com.driver.DeliveryPartner;
import com.driver.Order;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    private HashMap<String, Order> orderHashMap = new HashMap<>();
    private HashMap<Order, DeliveryPartner> orderPartnerMap = new HashMap<>();

    private HashMap<String, DeliveryPartner> partnerHashMap = new HashMap<>();

    private HashMap<String, Set<Order>> partnerOrders =  new HashMap<>();



    public void addOrder(String orderId, Order order) {
        if(!orderHashMap.containsKey(orderId) ) {
            orderHashMap.put(orderId,order);
        }
    }

    public void addPartner( DeliveryPartner partner) {
        if(!partnerHashMap.containsKey(partner.getId())) {
            partnerHashMap.put(partner.getId(), partner);
        }
    }

    public void addOrderPartnerPair( String orderId,  String partnerId){
        Order order = orderHashMap.get(orderId);
        DeliveryPartner partner = partnerHashMap.get(partnerId);

        if(!orderPartnerMap.containsKey(order)) {
            partner.setNumberOfOrders(partner.getNumberOfOrders() + 1);
            orderPartnerMap.put(order, partner);
            if(!partnerOrders.containsKey(partnerId)){
                partnerOrders.put(partnerId, new HashSet<>());
            }
            partnerOrders.get(partnerId).add(order);
        }
    }

    public Order getOrderById( String orderId){
        if(orderHashMap.containsKey(orderId)){
            return orderHashMap.get(orderId);
        }
        return null;
    }

    public DeliveryPartner getPartnerById( String partnerId){

        if(partnerHashMap.containsKey(partnerId)){
            return partnerHashMap.get(partnerId);
        }
        return null;
    }

    public int getOrderCountByPartnerId( String partnerId){

        Integer orderCount = 0;
        if(!partnerOrders.containsKey(partnerId)){
            return 0;
        }

        return partnerOrders.get(partnerId).size();
    }

    public List<String> getOrdersByPartnerId( String partnerId){
        List<String> orders = null;

        if(partnerOrders.containsKey(partnerId)){
            orders = new ArrayList<>();

            for(Order order : partnerOrders.get(partnerId)){
                orders.add(order.getId());
            }

        }

        return orders;
    }

    public List<String> getAllOrders(){
        List<String> orders = null;

        if(orderHashMap.size() > 0){
            orders = new ArrayList<>();
            for(String orderId : orderHashMap.keySet()){
                orders.add(orderId);
            }
        }
        return orders;
    }

    public int getCountOfUnassignedOrders(){

        int totalOrders = orderHashMap.size();
        int assignedOrders = orderPartnerMap.size();

        int countOfOrders = totalOrders - assignedOrders;
        return countOfOrders;
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId( String time,  String partnerId){
        int currentTime = Integer.parseInt(time.substring(0,2))*60 + Integer.parseInt(time.substring(3,5));
        int undeliveredOrders = 0;
        for(Order order : partnerOrders.get(partnerId)) {
            if(order.getDeliveryTime() > currentTime) {
                undeliveredOrders++;
            }
        }

        return undeliveredOrders;
    }

    public String getLastDeliveryTimeByPartnerId( String partnerId){

        int lastTime = 0;
        for(Order order : partnerOrders.get(partnerId)) {
            lastTime = Math.max(lastTime,order.getDeliveryTime());
        }

        int hour = lastTime/60;
        int min = lastTime%60;

        String hourTime = ((hour < 10) ? ("0"+hour): (""+hour));


        String minTime = ((min < 10) ? ("0"+min) : (""+min));

        return hourTime + ":" + minTime;
    }

    public void deletePartnerById( String partnerId){

        //Delete the partnerId
        //And push all his assigned orders to unassigned orders.

        partnerHashMap.remove(partnerId);
        for(Order order : partnerOrders.get(partnerId) ) {
            orderPartnerMap.remove(order);
        }

        partnerOrders.remove(partnerId);

    }

    public void deleteOrderById( String orderId){

        //Delete an order and also
        // remove it from the assigned order of that partnerId

        Order order = orderHashMap.get(orderId);
        orderHashMap.remove(orderId);

        DeliveryPartner partner = orderPartnerMap.get(order);
        orderPartnerMap.remove(order);


        partnerOrders.get(partner.getId()).remove(order);

    }
}