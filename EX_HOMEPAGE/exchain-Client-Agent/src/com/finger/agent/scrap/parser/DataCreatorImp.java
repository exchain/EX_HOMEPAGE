package com.finger.agent.scrap.parser;

import com.finger.agent.scrap.entity.Entity;

public class DataCreatorImp extends DataCreator {

    public DataHandler create(Entity entity) {
        return new FWFDataHandler( entity );
    }
}