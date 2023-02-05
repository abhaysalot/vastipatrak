package com.aviyan.vastipatrak.constant;

import lombok.ToString;

@ToString
public enum RoleName {
    //ADMIN - Should have all rights - Change group details, Create/Maintain Parties, products, Create Transactions, View everything
    //MANAGER - Should have rights to Create/Maintain Parties, products, Create Transactions, View everything
    //EXECUTOR - Should have rights to Create Transactions, View everything
    //VIEWER - should only be able to view everything
    ADMIN, MANAGER, EXECUTOR, VIEWER
}
