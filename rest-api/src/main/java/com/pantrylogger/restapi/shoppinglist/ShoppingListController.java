package com.pantrylogger.restapi.shoppinglist;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pantrylogger.domain.shoppinglist.ShoppingList.ShoppingListUUID;
import com.pantrylogger.domain.shoppinglist.ShoppingListItem.ShoppingListItemUUID;
import com.pantrylogger.domain.shoppinglist.create.CreateShoppingListCommand;
import com.pantrylogger.domain.shoppinglist.create.CreateShoppingListUseCase;
import com.pantrylogger.domain.shoppinglist.delete.DeleteShoppingListUseCase;
import com.pantrylogger.domain.shoppinglist.get.GetAllShoppingListsForUserUseCase;
import com.pantrylogger.domain.shoppinglist.item.add.AddItemToShoppingListCommand;
import com.pantrylogger.domain.shoppinglist.item.add.AddItemToShoppingListUseCase;
import com.pantrylogger.domain.shoppinglist.item.got.MarkItemAsGotOnShoppingListUseCase;
import com.pantrylogger.domain.shoppinglist.update.UpdateShoppingListCommand;
import com.pantrylogger.domain.shoppinglist.update.UpdateShoppingListUseCase;
import com.pantrylogger.restapi.SuccessResponse;
import com.pantrylogger.restapi.security.CustomUserDetails;
import com.pantrylogger.restapi.shoppinglist.dto.ShoppingListDto;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("shopping-list")
public class ShoppingListController {
    private static final Logger LOGGER = LoggerFactory.getLogger(
            ShoppingListController.class);
    private final GetAllShoppingListsForUserUseCase getAllShoppingListsForUserUseCase;
    private final CreateShoppingListUseCase createShoppingListUseCase;
    private final UpdateShoppingListUseCase updateShoppingListUseCase;
    private final AddItemToShoppingListUseCase addItemToShoppingListUseCase;
    private final MarkItemAsGotOnShoppingListUseCase markItemAsGotOnShoppingListUseCase;
    private final DeleteShoppingListUseCase deleteShoppingListUseCase;

    public ShoppingListController(
            GetAllShoppingListsForUserUseCase getAllShoppingListsForUserUseCase,
            CreateShoppingListUseCase createShoppingListUseCase,
            UpdateShoppingListUseCase updateShoppingListUseCase,
            AddItemToShoppingListUseCase addItemToShoppingListUseCase,
            MarkItemAsGotOnShoppingListUseCase markItemAsGotOnShoppingListUseCase,
            DeleteShoppingListUseCase deleteShoppingListUseCase
    ) {
        this.getAllShoppingListsForUserUseCase = getAllShoppingListsForUserUseCase;
        this.createShoppingListUseCase = createShoppingListUseCase;
        this.updateShoppingListUseCase = updateShoppingListUseCase;
        this.addItemToShoppingListUseCase = addItemToShoppingListUseCase;
        this.markItemAsGotOnShoppingListUseCase = markItemAsGotOnShoppingListUseCase;
        this.deleteShoppingListUseCase = deleteShoppingListUseCase;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ShoppingListDto>> findAllShoppingListsForUser(
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        LOGGER.debug("Getting all Shopping Lists for user");

        return new ResponseEntity<>(
                this.getAllShoppingListsForUserUseCase.execute(principal.getUser())
                        .stream()
                        .map(ShoppingListDto::new)
                        .toList(), HttpStatus.OK
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<ShoppingListDto> createNewShoppingList(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody CreateShoppingListCommand createShoppingListCommand
    ) {
        LOGGER.debug(
                "Creating new Shopping List {}",
                createShoppingListCommand.name()
        );

        return new ResponseEntity<>(
                new ShoppingListDto(this.createShoppingListUseCase.execute(
                        principal.getUser(),
                        createShoppingListCommand
                )),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{shoppingListUUID}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ShoppingListDto> updateShoppingList(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable ShoppingListUUID shoppingListUUID,
            @RequestBody UpdateShoppingListCommand updateShoppingListCommand
    ) {
        LOGGER.debug("Updating Shopping List {}", shoppingListUUID);

        return new ResponseEntity<>(
                new ShoppingListDto(
                        this.updateShoppingListUseCase.execute(
                                principal.getUser(),
                                shoppingListUUID,
                                updateShoppingListCommand
                        )
                ), HttpStatus.OK
        );
    }

    @PatchMapping("/{shoppingListUUID}/items")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ShoppingListDto> addItemToShoppingList(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable ShoppingListUUID shoppingListUUID,
            @RequestBody AddItemToShoppingListCommand addItemToShoppingListCommand
    ) {
        LOGGER.debug(
                "Adding ingredient ({}) to shopping list ({})",
                addItemToShoppingListCommand.ingredientUUID(),
                shoppingListUUID.uuid()
        );

        return new ResponseEntity<>(
                new ShoppingListDto(
                        this.addItemToShoppingListUseCase.execute(
                                principal.getUser(),
                                shoppingListUUID,
                                addItemToShoppingListCommand
                        )
                ), HttpStatus.OK
        );
    }

    @PatchMapping("/{shoppingListUUID}/items/{shoppingListItemUUID}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ShoppingListDto> markItemAsGotOnShoppingList(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable ShoppingListUUID shoppingListUUID,
            @PathVariable ShoppingListItemUUID shoppingListItemUUID
    ) {
        LOGGER.debug(
                "marking item ({}) as got on shopping list ({})",
                shoppingListItemUUID.uuid(),
                shoppingListUUID.uuid()
        );

        return new ResponseEntity<>(
                new ShoppingListDto(
                        this.markItemAsGotOnShoppingListUseCase.execute(
                                principal.getUser(),
                                shoppingListUUID,
                                shoppingListItemUUID
                        )
                ), HttpStatus.OK
        );
    }

    @DeleteMapping("/{shoppingListUUID}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<SuccessResponse> deleteShoppingList(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable ShoppingListUUID shoppingListUUID
    ) {
        LOGGER.debug("deleting recipe {}", shoppingListUUID.uuid());

        this.deleteShoppingListUseCase.execute(
                principal.getUser(),
                shoppingListUUID
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("Shopping List deleted"));
    }
}
