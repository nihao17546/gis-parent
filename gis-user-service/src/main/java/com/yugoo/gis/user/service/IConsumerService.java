package com.yugoo.gis.user.service;

/**
 * @author nihao 2018/10/9
 */
public interface IConsumerService {
    void create(String name, Integer buildingId, String floor, String position, String number,
                String category, String nature, Integer peopleNum, byte[] pic, String status,
                String legal);

}
