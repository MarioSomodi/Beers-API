package com.msomodi.beersapi;

import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.List;

public class CustomActiveMQConnectionFactory extends ActiveMQConnectionFactory {
    public void setTrustedPackages(String trustedPackages) {
        super.setTrustedPackages(List.of(trustedPackages));
    }
}