package org.openpay.client.dto;

import java.util.ArrayList;
import lombok.Data;


@Data
public class Events{
    private String available;
    private String returned;
    private String collectionURI;
    private ArrayList<Item> items;
}
