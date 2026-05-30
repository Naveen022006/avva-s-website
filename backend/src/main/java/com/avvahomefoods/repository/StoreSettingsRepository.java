package com.avvahomefoods.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.avvahomefoods.model.StoreSettings;

public interface StoreSettingsRepository extends MongoRepository<StoreSettings, String> {
}
