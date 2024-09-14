package org.drachens;

import java.util.ArrayList;
import java.util.HashMap;

import static org.drachens.util.ServerUtil.setupAll;

public class Main {
    public static void main(String[] args) {
        setupAll(new ArrayList<>(),60, new HashMap<>());
    }
}