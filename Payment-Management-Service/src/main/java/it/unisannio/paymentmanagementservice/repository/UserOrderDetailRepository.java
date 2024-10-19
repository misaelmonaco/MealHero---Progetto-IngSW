package it.unisannio.paymentmanagementservice.repository;

import it.unisannio.paymentmanagementservice.model.entity.UserOrderDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOrderDetailRepository extends CrudRepository<UserOrderDetail, Integer> {
    @Query("SELECT o FROM UserOrderDetail o WHERE o.userId =: userId")
    Iterable<UserOrderDetail> findByUserId(@Param("userId") Integer userId);

    @Query("SELECT o FROM UserOrderDetail o WHERE o.orderId =: orderId")
    Iterable<UserOrderDetail> findByOrderId(@Param("orderId") int orderId);
}
