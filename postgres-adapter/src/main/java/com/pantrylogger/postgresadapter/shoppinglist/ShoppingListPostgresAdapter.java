package com.pantrylogger.postgresadapter.shoppinglist;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.pantrylogger.domain.shoppinglist.ShoppingList;
import com.pantrylogger.domain.shoppinglist.ShoppingList.ShoppingListUUID;
import com.pantrylogger.domain.shoppinglist.ShoppingListRepositoryPort;
import com.pantrylogger.domain.user.User;

@Service
public class ShoppingListPostgresAdapter implements ShoppingListRepositoryPort {

    private final ShoppingListJpaEntityRepository shoppingListJpaEntityRepository;

    public ShoppingListPostgresAdapter(ShoppingListJpaEntityRepository shoppingListJpaEntityRepository) {
        this.shoppingListJpaEntityRepository = shoppingListJpaEntityRepository;
    }

    @Override
    public Optional<ShoppingList> getByUUID(ShoppingListUUID uuid) {
        return this.shoppingListJpaEntityRepository.findById(uuid.uuid())
                .map(ShoppingListJpaEntity::toShoppingList);
    }

    @Override
    public List<ShoppingList> getAllForUser(User user) {
        return this.shoppingListJpaEntityRepository.findByMembersContaining(user.getUsername()
                        .toString())
                .stream()
                .map(ShoppingListJpaEntity::toShoppingList)
                .toList();
    }

    @Override
    public ShoppingList save(ShoppingList shoppingList) {
        ShoppingListJpaEntity shoppingListJpaEntity = new ShoppingListJpaEntity(
                shoppingList);

        shoppingListJpaEntity = this.shoppingListJpaEntityRepository.save(
                shoppingListJpaEntity);

        return shoppingListJpaEntity.toShoppingList();
    }

    @Override
    public void delete(ShoppingList shoppingList) {
        try {
            this.shoppingListJpaEntityRepository.deleteById(shoppingList.getUuid()
                    .uuid());
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalStateException(
                    String.format(
                            "Invariant violated: Shopping List with UUID %s was expected to exist but was not found",
                            shoppingList.getUuid().uuid()
                    ), e
            );
        }
    }
}
