package com.finger.agent.scrap.parser;

import com.finger.agent.scrap.entity.Entity;
import com.finger.agent.scrap.entity.ReceiveEntity;
import com.finger.agent.scrap.exception.ScrapingByException;

abstract public class DataHandler {

    protected Entity entity;
    abstract public ReceiveEntity receiveEntity() throws ScrapingByException;

}