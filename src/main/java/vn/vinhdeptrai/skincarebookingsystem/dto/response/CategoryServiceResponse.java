package vn.vinhdeptrai.skincarebookingsystem.dto.response;

import java.util.Set;

public class CategoryServiceResponse {
    int id;
    String name;
    String description;
    boolean signature;
    Set<ServiceResponse> services;
 }
