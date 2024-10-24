package it.unisannio.paymentmanagementservice.repository;

import it.unisannio.paymentmanagementservice.model.entity.UserOrderDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOrderDetailRepository extends CrudRepository<UserOrderDetail, Integer> {
    @Query("SELECT o FROM UserOrderDetail o WHERE o.userId =:userId")
    Iterable<UserOrderDetail> findByUserId(@Param("userId") Integer userId);

    @Query("SELECT o FROM UserOrderDetail o WHERE o.orderId =:orderId")
    Iterable<UserOrderDetail> findByOrderId(@Param("orderId") int orderId);

    @Query("SELECT o FROM UserOrderDetail o WHERE o.userId =:userId AND o.orderId =:orderId")
    Iterable<UserOrderDetail> findByUserIdAndOrderId(@Param("userId") Integer userId, @Param("orderId") Integer orderId);

    @Query("SELECT o FROM UserOrderDetail o WHERE o.orderId =:orderId AND o.menuItemId =:menuItemId")
    Iterable<UserOrderDetail> findByOrderIdAndMenuItemId(@Param("orderId") int orderId, @Param("menuItemId") int menuItemId);
}
