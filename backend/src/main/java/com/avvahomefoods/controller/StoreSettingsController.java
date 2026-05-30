package com.avvahomefoods.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avvahomefoods.model.StoreSettings;
import com.avvahomefoods.repository.StoreSettingsRepository;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin(origins = "*")
public class StoreSettingsController {

    @Autowired
    private StoreSettingsRepository storeSettingsRepository;

    // Public GET — used by user-side order page & admin to load current values
    @GetMapping
    public ResponseEntity<StoreSettings> getSettings() {
        List<StoreSettings> all = storeSettingsRepository.findAll();
        if (all.isEmpty()) {
            // Return safe defaults if DB has no record yet
            return ResponseEntity.ok(new StoreSettings(50, 500));
        }
        return ResponseEntity.ok(all.get(0));
    }

    // Admin PUT — saves/updates the single settings document
    @PutMapping
    public ResponseEntity<StoreSettings> updateSettings(@RequestBody StoreSettings incoming) {
        List<StoreSettings> all = storeSettingsRepository.findAll();
        StoreSettings settings;
        if (all.isEmpty()) {
            settings = new StoreSettings(incoming.getDeliveryCharge(), incoming.getFreeDeliveryThreshold());
        } else {
            settings = all.get(0);
            settings.setDeliveryCharge(incoming.getDeliveryCharge());
            settings.setFreeDeliveryThreshold(incoming.getFreeDeliveryThreshold());
        }
        return ResponseEntity.ok(storeSettingsRepository.save(settings));
    }
}
