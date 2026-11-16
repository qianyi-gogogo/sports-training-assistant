
package com.qianyi.sportstrainingassistant.controller;

import com.qianyi.sportstrainingassistant.dto.request.profile.BodyProfileRequest;
import com.qianyi.sportstrainingassistant.model.BodyProfile;
import com.qianyi.sportstrainingassistant.service.BodyProfileRequestMapper;
import com.qianyi.sportstrainingassistant.service.BodyProfileService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users/{userId}/profile")
public class BodyProfileController {

    private final BodyProfileService service;

    // ==================== Constructor ====================

    public BodyProfileController(
            BodyProfileService service
    ) {
        this.service = service;
    }

    // ==================== Create ====================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BodyProfile createProfile(
            @PathVariable Long userId,
            @Valid @RequestBody BodyProfileRequest request
    ) {

        BodyProfile profile =
                BodyProfileRequestMapper.toModel(request);

        return service.createProfile(userId, profile);
    }

    // ==================== Read ====================

    @GetMapping("/current")
    public BodyProfile getCurrentProfile(
            @PathVariable Long userId
    ) {

        return service.getCurrentProfile(userId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Body profile not found"
                        )
                );
    }
}
