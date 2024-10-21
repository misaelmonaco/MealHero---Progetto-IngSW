package it.unisannio.paymentmanagementservice.repository;

import it.unisannio.paymentmanagementservice.model.entity.PaymentDetail;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PaymentDetailRepository extends CrudRepository<PaymentDetail, Integer> {

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.paymentId =:paymentId")
    Iterable<PaymentDetail> findByPaymentId(@Param("paymentId") Integer paymentId);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.userId =:userId")
    Iterable<PaymentDetail> findByUserId(@Param("userId") Integer userId);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.userId =:userId AND pd.paymentId =:paymentId")
    PaymentDetail findByUserAndPaymentId(@Param("userId") Integer userId, @Param("paymentId") Integer paymentId);

    @Modifying
    @Transactional
    @Query("DELETE FROM PaymentDetail p WHERE p.paymentId =:paymentId")
    void deleteByPaymentId(@Param("paymentId") Integer paymentId);
}
