package com.pantrylogger.domain.shoppinglist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.pantrylogger.domain.ShoppingListFixture;
import com.pantrylogger.domain.UserFixture;
import com.pantrylogger.domain.user.Username;

class ShoppingListMembersTest {

    @Test
    void addMemberTest() {
        Username member = UserFixture.basicTestUser().getUsername();
        ShoppingList shoppingList = ShoppingListFixture.emptyShoppingList(Set.of(
                member));
        Username memberToAdd = UserFixture.anotherBasicTestUser().getUsername();

        assertEquals(1, shoppingList.getMembers().size());
        shoppingList.addMember(memberToAdd);
        assertEquals(2, shoppingList.getMembers().size());
        assertTrue(
                shoppingList.getMembers().contains(memberToAdd)

        );
    }
}
