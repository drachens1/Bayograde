package org.drachens;

import net.minestom.server.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

import static org.drachens.util.ServerUtil.setupAll;

public class Main {
    public static void main(String[] args) {
        ItemStack[] e = {};
        setupAll(new ArrayList<>(), 60, new HashMap<>(), e);
    }
}