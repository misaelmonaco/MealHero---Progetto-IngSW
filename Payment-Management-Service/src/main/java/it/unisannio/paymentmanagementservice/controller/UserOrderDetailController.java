package it.unisannio.paymentmanagementservice.controller;

import it.unisannio.paymentmanagementservice.entity.UserOrderDetail;
import it.unisannio.paymentmanagementservice.service.UserOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userOrderDetail")
public class UserOrderDetailController {

    @Autowired
    private UserOrderDetailService userOrderDetailService;


    @PostMapping("/add")
    public ResponseEntity add(@RequestBody UserOrderDetail userOrderDetail) {
        return userOrderDetailService.add(userOrderDetail);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        return userOrderDetailService.delete(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Integer id) {
        if(userOrderDetailService.findById(id) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(userOrderDetailService.findById(id));
    }

    @GetMapping("/all")
    public ResponseEntity getAll() {
        if(!userOrderDetailService.findAll().iterator().hasNext())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(userOrderDetailService.findAll());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity getByUserId(@PathVariable Integer id) {
        if(!userOrderDetailService.findByUserId(id).iterator().hasNext())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(userOrderDetailService.findByUserId(id));
    }
}
