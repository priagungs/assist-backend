package com.future.OfficeInventorySystem.model;

import java.util.Objects;

public class UserHasItemID {
    protected User user;
    protected Item item;

    public UserHasItemID() {}

    public UserHasItemID(User user, Item item) {
        this.user = user;
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserHasItemID userHasItemID = (UserHasItemID) o;
        if (user != userHasItemID.user) return false;
        return item == userHasItemID.item;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, item);
    }
}
