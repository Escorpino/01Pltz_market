package com.platzi.market.persistence;

import com.platzi.market.domain.Product;
import com.platzi.market.domain.Purchase;
import com.platzi.market.domain.repository.PurchaseRepository;
import com.platzi.market.persistence.crud.CompraCrudRepository;
import com.platzi.market.persistence.entity.Compra;
import com.platzi.market.persistence.entity.Producto;
import com.platzi.market.persistence.mapper.PurchaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CompraRepository implements PurchaseRepository {

    @Autowired
    private CompraCrudRepository compraCrudRepository;

    @Autowired
    private PurchaseMapper mapper;

    @Override
    public List<Purchase> getAll() {
        List<Compra> compras = (List<Compra>) compraCrudRepository.findAll();
        return mapper.toPurchases(compras);
    }

    @Override
    public Optional<Purchase> getPurchase(int purchaseId) {

        return compraCrudRepository.findById(purchaseId)
                .map(compra -> mapper.toPurchase(compra));
    }

    @Override
    public Optional<List<Purchase>> getByClient(String clientId) {
        return compraCrudRepository.findByIdCliente(clientId)
                .map(compras -> mapper.toPurchases(compras));
    }

    @Override
    public Purchase save(Purchase purchase) {
        Compra compra = mapper.toCompra(purchase);
        //Garantizamos que se guarde en cascada, compra conoce a los productos
        // y los productos conocen la compra
        compra.getProductos().forEach(producto -> producto.setCompra(compra));

        return mapper.toPurchase(compraCrudRepository.save(compra));
    }

    @Override
    public void delete(int purchaseId) {
        compraCrudRepository.deleteById(purchaseId);
    }
}