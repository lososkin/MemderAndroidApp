package com.example.memder;

import java.util.List;

class Item {
    int id;
    int likes;
    int dislikes;
    String imgURL;
    String text;
}

public class ApiResponse {
    public List<Item> results;
    public int count;
    public String next;
}