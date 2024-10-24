package it.unisannio.orderservice.repository;

import it.unisannio.orderservice.entity.OrderDetail;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends CrudRepository<OrderDetail, Integer> {

    @Query("SELECT od FROM OrderDetail od WHERE od.orderId = :orderId")
    Iterable<OrderDetail> findByOrderId(@Param("orderId") int orderId);

    @Query("SELECT od FROM OrderDetail od WHERE od.menuItemId = :menuItemId AND od.orderId =:orderId")
    OrderDetail findByMenuItemIdAndOrderId(@Param("menuItemId") int menuItemId, @Param("orderId") int orderId);

    @Modifying
    @Query("DELETE FROM OrderDetail od WHERE od.orderId = :orderId")
    void deleteByOrderId(@Param("orderId") int orderId);
}
