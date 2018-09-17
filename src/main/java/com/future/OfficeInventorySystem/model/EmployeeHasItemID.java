package com.future.OfficeInventorySystem.model;

import java.util.Objects;

public class EmployeeHasItemID {
    protected Employee employee;
    protected Item item;

    public EmployeeHasItemID() {}

    public EmployeeHasItemID(Employee employee, Item item) {
        this.employee = employee;
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeHasItemID employeeHasItemID = (EmployeeHasItemID) o;
        if (employee != employeeHasItemID.employee) return false;
        return item == employeeHasItemID.item;
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee, item);
    }
}
