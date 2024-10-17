package it.unisannio.webpagesmanagementservice.service;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IndexService {

    public String findRestaurantsByNameAndLocation(String name, String location){

        Client client = ClientBuilder.newClient();
        WebTarget endpoint = client.target("http://localhost:8081/restaurant");
        WebTarget resource;

        if (name == null && location == null) {
            return "<h1>Inserisci dei dati nel campo di ricerca!</h1>";
        }

        Response response;
        List<Restaurant> restaurantList = new ArrayList<>();
        Restaurant restaurant = null;

        try {
            if (!(location == null) && name == null) {
                resource = endpoint.path("/location/" + location);
                restaurantList = resource.request()
                        .accept(MediaType.APPLICATION_JSON)
                        .get(new GenericType<List<Restaurant>>() {});
            } else if (!(name == null) && location == null) {
                resource = endpoint.path("/name/" + name);
                restaurantList = resource.request()
                        .accept(MediaType.APPLICATION_JSON)
                        .get(new GenericType<List<Restaurant>>() {});
            } else {
                resource = endpoint.path("/" + location + "/" + name);
                restaurant = resource.request()
                        .accept(MediaType.APPLICATION_JSON)
                        .get(new GenericType<Restaurant>() {});
            }
        } catch (WebApplicationException e) {
            int status = e.getResponse().getStatus();

            if (status == Response.Status.NOT_FOUND.getStatusCode()) {
                System.out.println("Risorsa non trovata: " + e.getMessage());
                return "<h2> Non è stato trovato alcun ristorante con questi dati! </h2>";
            } else if (status == Response.Status.BAD_REQUEST.getStatusCode()) {
                System.out.println("Richiesta non valida: " + e.getMessage());
                return "<h2> Richiesta non valida! </h2>";
            } else {
                System.out.println("Errore: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Si è verificato un errore: " + e.getMessage());
        }

        if (restaurant != null) {
            restaurantList.add(restaurant);
        }

        StringBuilder htmlTable = new StringBuilder();

        htmlTable.append("<table>")
                .append("<tr>")
                .append("<th>Name</th>")
                .append("<th>Address</th>")
                .append("<th>Location</th>")
                .append("<th>Phone Number</th>")
                .append("<th>Details</th>")
                .append("</tr>");

        int count = 0;
        for (Restaurant rest : restaurantList) {
            if(count == 5)
                break;
            count++;

            htmlTable.append("<tr>")
                    .append("<td>").append(rest.getName()).append("</td>")
                    .append("<td>").append(rest.getAddress()).append("</td>")
                    .append("<td>").append(rest.getLocation()).append("</td>")
                    .append("<td>").append(rest.getPhone()).append("</td>")
                    .append("<td>").append("<a href='restaurant.html?id=").append(rest.getId()).append("'>Other Details</a></td>")
                    .append("</tr>");
        }

        htmlTable.append("</table>");

        return htmlTable.toString();
    }
}

class Restaurant {

    private int id;
    private String name;
    private String address;
    private String location;
    private String phone;
    private String email;

    public Restaurant(String name, String address, String location, String phone, String email) {
        this.name = name;
        this.address = address;
        this.location = location;
        this.phone = phone;
        this.email = email;
    }

    protected Restaurant() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
