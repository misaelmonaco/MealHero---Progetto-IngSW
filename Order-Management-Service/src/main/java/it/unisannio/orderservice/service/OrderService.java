package it.unisannio.orderservice.service;

import it.unisannio.orderservice.entity.Order;
import it.unisannio.orderservice.entity.OrderDetail;
import it.unisannio.orderservice.repository.OrderDetailRepository;
import it.unisannio.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public ResponseEntity addOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body("Ordine aggiunto con successo! Id: " + savedOrder.getId());
    }

    @Transactional
    public ResponseEntity deleteOrder(Integer id) {
        orderDetailRepository.deleteByOrderId(id);
        orderRepository.deleteById(id);
        return ResponseEntity.ok().body("Ordine rimosso con successo!");
    }

    public ResponseEntity updateOrder(Integer id, List<OrderDetail> details) {
        Order existingOrder = orderRepository.findById(id).orElse(null);
        if(existingOrder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ordine non trovato!");
        }
        details.forEach(detail -> detail.setOrderId(id));
        orderDetailRepository.saveAll(details);
        return ResponseEntity.ok().body("Ordine aggiornato con successo!");
    }

    public Iterable<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Iterable<Order> findByReservationId(Integer id) {
        return orderRepository.findByReservationId(id);
    }

    public ResponseEntity addOrderDetail(Integer id, OrderDetail orderDetail) {
        Order existingOrder = orderRepository.findById(id).orElse(null);
        if(existingOrder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ordine non trovato!");
        }
        orderDetail.setOrderId(id);
        orderDetailRepository.save(orderDetail);
        return ResponseEntity.ok().body("Ordine aggiornato con successo!");
    }

    @Transactional
    public ResponseEntity toggleOrder(int id) {
        Order existingOrder = orderRepository.findById(id).orElse(null);
        if (existingOrder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ordine non trovato!");
        }
        existingOrder.toggle();
        orderRepository.deleteById(existingOrder.getId());
        orderDetailRepository.deleteByOrderId(id);
        return ResponseEntity.ok().body("Ordine eliminato con successo");
    }

    @Transactional
    public ResponseEntity getOrderDetails(int orderId) {

       ArrayList<OrderDetail> details = (ArrayList<OrderDetail>) orderDetailRepository.findByOrderId(orderId);
       MenuItem menuItem;
       ArrayList<MenuOrderItem> menuOrderItems = new ArrayList<>();

       Client client = ClientBuilder.newClient();
       WebTarget target = client.target("http://localhost:8082/menuItem/");
       for (OrderDetail orderDetail : details) {
           WebTarget resoruce = target.path(String.valueOf(orderDetail.getMenuItemId()));

           Response response = resoruce.request().accept(MediaType.APPLICATION_JSON).get();
           if (response.getStatus() == 200) {
               menuItem = response.readEntity(MenuItem.class);
               MenuOrderItem menuOrderItem = new MenuOrderItem(menuItem.getName(), menuItem.getDescription(), orderDetail.getQuantity());
               menuOrderItems.add(menuOrderItem);
           } else {
               return ResponseEntity.status(response.getStatus()).body("Errore: " + response.getStatus());
           }
       }

       client.close();

       if(!(menuOrderItems.iterator().hasNext()))
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'ordine selezionato con id " + orderId + " è vuoto!");

       return ResponseEntity.ok().body(menuOrderItems);
    }

    public ResponseEntity selectOrdersByTimeDateAndRestaurant(int restaurantId, String time, String date) {
        try {
            LocalTime localTime = LocalTime.parse(time);
            LocalDate localDate = LocalDate.parse(date);

            ResponseEntity response = selectOrdersToBePrepared(restaurantId, localTime, localDate);

            if (!response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ordine/i non trovato/i! --> Errore: " + response.getStatusCode());
            }

            return response;

        } catch (DateTimeParseException e) {
            String errorMessage = "Errore nella formattazione della data/ora. Assicurati che il formato sia 'HH:mm' per l'ora e 'yyyy-MM-dd' per la data e di star inserendo valori corretti.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }


    /**
     *  METODI PRIVATI AD USO DELL'ORDER SERVICE
     */

    private ResponseEntity selectOrdersToBePrepared(int restaurantId, LocalTime localTime, LocalDate localDate) {
        Client client = ClientBuilder.newClient();
        WebTarget endpoint = client.target("http://localhost:8085");
        WebTarget resource = endpoint.path("/reservation/restaurant/" + restaurantId + "/date/" + localDate.toString() + "/time/" + localTime.toString());

        List<Order> orders = new ArrayList<>();
        List<Reservation> reservations = new ArrayList<>();

        Response response = resource.request().accept(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() == 200) {
            reservations = response.readEntity(new GenericType<List<Reservation>>() {});
        } else {
            return ResponseEntity.status(response.getStatus()).body("Errore: " + response.getStatus());
        }

        for(Reservation r: reservations) {
            orders.addAll((Collection<? extends Order>) orderRepository.findByReservationId(r.getId()));
        }
        client.close();
        return new ResponseEntity(orders, HttpStatus.OK);
    }
}

class Reservation {
    private int id;
    private int userId;
    private int numOfGuests;
    private int restaurantId;
    private int tableId;
    private boolean includeParkingSpot;
    private int parkingSpotId;
    private LocalDate date;
    private LocalTime time;

    public Reservation() { }

    public Reservation(int userId, int numOfGuests, int restaurantId, int tableId, int parkingSpotId, LocalDate date, LocalTime time) {
        this.userId = userId;
        this.numOfGuests = numOfGuests;
        this.restaurantId = restaurantId;
        this.tableId = tableId;
        this.parkingSpotId = parkingSpotId;
        this.date = date;
        this.time = time;
        this.includeParkingSpot = false;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getNumOfGuests() {
        return numOfGuests;
    }

    public void setNumOfGuests(int numOfGuests) {
        this.numOfGuests = numOfGuests;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public boolean isIncludeParkingSpot() {
        return includeParkingSpot;
    }

    public void setIncludeParkingSpot(boolean includeParkingSpot) {
        this.includeParkingSpot = includeParkingSpot;
    }

    public int getParkingSpotId() {
        return parkingSpotId;
    }

    public void setParkingSpotId(int parkingSpotId) {
        this.parkingSpotId = parkingSpotId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}

class MenuItem {

    private int id;
    private String name;
    private String description;
    private double price;
    private int menuId;

    protected MenuItem(){}

    public MenuItem(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }
}

class MenuOrderItem{

    private String name;
    private String description;
    private int quantity;

    public MenuOrderItem(String name, String description, int quantity) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}