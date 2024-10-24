package it.unisannio.paymentmanagementservice.service;

import it.unisannio.paymentmanagementservice.model.AESEncryption;
import it.unisannio.paymentmanagementservice.model.entity.CreditCard;
import it.unisannio.paymentmanagementservice.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class CreditCardService {

    @Autowired
    private CreditCardRepository creditCardRepository;

    public ResponseEntity addCreditCard(CreditCard creditCard) {
        creditCardRepository.save(creditCard);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity deleteCreditCard(Integer id) {
        if(!creditCardRepository.existsById(id))
            return ResponseEntity.notFound().build();
        creditCardRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity updateCreditCard(int id, CreditCard creditCard) throws Exception {
        CreditCard existingCard = creditCardRepository.findById(id).orElse(null);
        if(existingCard == null)
            return ResponseEntity.notFound().build();

        existingCard.setCardNumber(creditCard.getCardNumber());
        existingCard.setExpirationDate(creditCard.getExpirationDate());
        existingCard.setCvv(creditCard.getCvv());
        existingCard.setBalance(creditCard.getBalance());
        creditCardRepository.save(existingCard);
        return ResponseEntity.ok().build();
    }

    public CreditCard getCreditCardById(Integer id) {
        return creditCardRepository.findById(id).orElse(null);
    }

    public Iterable<CreditCard> getAllCreditCards() {
        return creditCardRepository.findAll();
    }

    public CreditCard getByUserId(Integer userId) {
        return creditCardRepository.findByUserId(userId).orElse(null);
    }

    public CreditCard getCreditCardByCardNumber(String cardNumber) throws Exception {
        return creditCardRepository.getByCardNumber(AESEncryption.encrypt(Base64.getEncoder().encodeToString(cardNumber.getBytes()))).orElse(null);
    }

    public Iterable<CreditCard> getCreditCardByHolderName(String holderName) throws Exception {
        return creditCardRepository.getByHolderName(AESEncryption.encrypt(Base64.getEncoder().encodeToString(holderName.getBytes())));
    }
}
